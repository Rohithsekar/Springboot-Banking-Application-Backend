package com.banking.online_banking.service;



import com.banking.online_banking.DAO.TransactionResponse;
import com.banking.online_banking.DTO.*;
import com.banking.online_banking.exception.CustomGeneralException;
import com.banking.online_banking.exception.MinimumAmountRequiredException;
import com.banking.online_banking.exception.UnsupportedAccountTypeException;
import com.banking.online_banking.model.Account;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Transaction;
import com.banking.online_banking.repository.AccountRepository;
import com.banking.online_banking.repository.CustomerRepository;
import com.banking.online_banking.repository.TransactionRepository;
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

    private final TransactionRepository transactionRepository;

    private IdealResponse idealResponse;

    private TransactionResponse transactionResponse;

    public CustomerService(CustomerRepository customerRepository,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           AuthenticationManager authenticationManager,
                           IdealResponse idealResponse,
                           TransactionResponse transactionResponse){
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.authenticationManager = authenticationManager;
        this.idealResponse = new IdealResponse(ResponseStatus.SUCCESS,null,null);
        this.transactionResponse = transactionResponse;
    }


    @Transactional
    public IdealResponse createAccount(AccountCreationRequest creationRequest) {
        if(creationRequest.amount()<2500){
            throw new MinimumAmountRequiredException("Initial deposit should be minimum Rs.2500");
        }
        else if(!(creationRequest.type().equals("Savings") || creationRequest.type().equals("Checking"))){
            throw new UnsupportedAccountTypeException("Oops.Only Savings and Checking accounts available");

        }
        Account account = new Account();
        account.setAccountId(BankingServiceUtilities.sixDigitRandomNumber());
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
        transaction.setTransactionDate(LocalDate.now());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        account.setTransactions(transactions);
        transaction.setAccount(account);

        accountRepository.save(account);
        AccountCreationResponse Data =new  AccountCreationResponse(account.getAccountNumber(),
                account.getBalance(), account.getType());
        idealResponse.setData(Data);
        return idealResponse;
    }

    @Transactional
    public IdealResponse transferAmount(TransferRequest transferRequest) {
        //to do: check if the logged-in user's from account number and the
        //beneficiary's to account number exists in the database. If yes, check the
        //sender has sufficient balance in his account and after the transaction, his
        //account should still have the minimum balance. IF ONLY ALL THESE CHECKS passes,
        //make the transaction
        float MINIMUM_BALANCE = 500;
        double updatedPayerBalance;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        log.info("The name retrieved from the authentication object in the security context is {}",name);
        Customer loggedInCustomer = customerRepository.findByUsername(name).orElseThrow();
        long accountId = accountRepository.fetchAccountIdByAccountNumber(transferRequest.accountNumber()).
                orElseThrow(()-> new CustomGeneralException("Account number doesn't exist."));
        Account payerAccount = accountRepository.findByCustomerAndAccountId(loggedInCustomer,accountId).
                orElseThrow(()->new CustomGeneralException("Account number does not exist in the record"));
        double payerBalance = payerAccount.getBalance();
        updatedPayerBalance = payerBalance- transferRequest.amount();
        //reject the request if either balance is insufficient or minimum balance constraint is not met
        if(payerBalance<transferRequest.amount()){
            idealResponse.setStatus(ResponseStatus.FAILURE);
            idealResponse.setError("Insufficient balance");
        }
        else if(updatedPayerBalance<MINIMUM_BALANCE){
            idealResponse.setStatus(ResponseStatus.FAILURE);
            idealResponse.setError("Minimum balance must be maintained at "+MINIMUM_BALANCE+".Please " +
                    "try with a smaller amount");
        }
        else{
           Account beneficiaryAccount = accountRepository.findByAccountNumber(transferRequest.toAccount())
                    .orElseThrow(()->new CustomGeneralException("Beneficiary account does not exist in records"));
           if(beneficiaryAccount.getAccountId()==payerAccount.getAccountId()){
               throw new CustomGeneralException("You cannot transfer amount to the same account you are sending amount from");
           }

            double updatedBeneficiaryBalance = beneficiaryAccount.getBalance()+ transferRequest.amount();


            Transaction payerTransaction = new Transaction();
            Transaction beneficiaryTransaction = new Transaction();
            //First set the values to all the common fields to both the payer and beneficiary
            //transaction objects.
            payerTransaction.setSentFrom(payerAccount.getAccountNumber());
            beneficiaryTransaction.setSentFrom(payerAccount.getAccountNumber());

            payerTransaction.setReceivedBy(beneficiaryAccount.getAccountNumber());
            beneficiaryTransaction.setReceivedBy(beneficiaryAccount.getAccountNumber());

            payerTransaction.setAmount(transferRequest.amount());
            beneficiaryTransaction.setAmount(transferRequest.amount());

            payerTransaction.setTransactionType(Transaction.TransactionType.TRANSFER);
            beneficiaryTransaction.setTransactionType(Transaction.TransactionType.TRANSFER);

            payerTransaction.setTransactionDate(LocalDate.now());
            beneficiaryTransaction.setTransactionDate(LocalDate.now());

            payerTransaction.setAccount(payerAccount);
            beneficiaryTransaction.setAccount(beneficiaryAccount);

            payerTransaction.setTransactionId(UUID.randomUUID().toString());
            beneficiaryTransaction.setTransactionId(UUID.randomUUID().toString());
            //Depending on the success/failure of payment processing, set the
            //status and balance fields
            if(BankingServiceUtilities.paymentProcessing().equals("success")){

                payerTransaction.setStatus(ResponseStatus.SUCCESS.name());
                beneficiaryTransaction.setStatus(ResponseStatus.SUCCESS.name());

                payerAccount.setBalance(updatedPayerBalance);
                beneficiaryAccount.setBalance(updatedBeneficiaryBalance);

                payerTransaction.setClosingBalance(updatedPayerBalance);
                beneficiaryTransaction.setClosingBalance(updatedBeneficiaryBalance);

                idealResponse.setStatus(ResponseStatus.SUCCESS);

            }
            else{
                payerTransaction.setStatus(ResponseStatus.FAILURE.name());
                beneficiaryTransaction.setStatus(ResponseStatus.FAILURE.name());

                payerTransaction.setClosingBalance(payerBalance);
                beneficiaryTransaction.setClosingBalance(beneficiaryAccount.getBalance());

                idealResponse.setError("Payment processing failed");
                idealResponse.setStatus(ResponseStatus.FAILURE);
            }

            //Establishing the bidirectional relationship between Account and
            //Transaction objects(entities)
            List<Transaction> payerTransactions = new ArrayList<>();
            List<Transaction> beneficiaryTransactions = new ArrayList<>();

            payerTransactions.add(payerTransaction);
            beneficiaryTransactions.add(beneficiaryTransaction);

            payerAccount.setTransactions(payerTransactions);
            beneficiaryAccount.setTransactions(beneficiaryTransactions);

            payerTransaction.setAccount(payerAccount);
            beneficiaryTransaction.setAccount(beneficiaryAccount);

            //transaction records will automatically be persisted via the cascading relationship
            accountRepository.save(payerAccount);
            accountRepository.save(beneficiaryAccount);

            //Generating the response object
            transactionResponse.setAmount(transferRequest.amount());
            transactionResponse.setClosingBalance(updatedPayerBalance);

            idealResponse.setData(transactionResponse);
        }
        return idealResponse;
    }
}
