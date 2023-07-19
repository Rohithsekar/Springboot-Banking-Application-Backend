package com.banking.online_banking.service;



import com.banking.online_banking.assistance.AccountCreationRequest;
import com.banking.online_banking.assistance.AccountCreationResponse;
import com.banking.online_banking.exception.MinimumAmountRequiredException;
import com.banking.online_banking.exception.UnsupportedAccountTypeException;
import com.banking.online_banking.model.Account;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Transaction;
import com.banking.online_banking.repository.AccountRepository;
import com.banking.online_banking.repository.CustomerRepository;
import com.banking.online_banking.utilities.BankingServiceUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    public CustomerService(CustomerRepository customerRepository,
                           AccountRepository accountRepository,
                           AuthenticationManager authenticationManager){
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
    }


    @Transactional
    public AccountCreationResponse createAccount(AccountCreationRequest creationRequest) {
        if(creationRequest.amount()<2500){
            throw new MinimumAmountRequiredException("Initial deposit should be minimum Rs.2500");
        }
        else if(!(creationRequest.type().equals("Savings") || creationRequest.type().equals("Checking"))){
            throw new UnsupportedAccountTypeException("Oops.Only Savings and Checking accounts available");

        }
        Account account = new Account();
        account.setAccountNumber(BankingServiceUtilities.sixDigitRandomNumber());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        log.info("The name retrieved from the authentication object in the security context is {}",name);
        Customer loggedInCustomer = customerRepository.findByUsername(name).orElseThrow();
        account.setCustomer(loggedInCustomer);
        if (creationRequest.type().equals("Savings")) {
            account.setType(Account.AccountType.SAVINGS);
        }
        else {
            account.setType(Account.AccountType.CHECKING);
        }




        account.setBalance(creationRequest.amount());

        Transaction transaction = new Transaction();
        /*
        UUID stands for Universally Unique Identifier. It is a 128-bit value that is used to uniquely identify
        information in computer systems. UUIDs are often used as identifiers for various entities, such as database
        records, files, or network resources, where uniqueness is required.
        In Java, you can generate UUIDs using the java.util.UUID class, which provides methods for creating UUIDs
         */
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setStatus("success");
        transaction.setAmount(creationRequest.amount());
        transaction.setTransactionType(Transaction.TransactionType.INITIAL_DEPOSIT);
        transaction.setClosingBalance(creationRequest.amount());
        transaction.setTransactiondate(LocalDate.now());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        account.setTransactions(transactions);
        transaction.setAccount(account);

        accountRepository.save(account);
        return new AccountCreationResponse(account.getAccountNumber(),account.getBalance(), account.getType());
    }
}
