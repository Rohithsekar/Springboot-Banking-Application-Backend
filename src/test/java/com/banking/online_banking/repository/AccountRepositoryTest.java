package com.banking.online_banking.repository;



import com.banking.online_banking.model.Account;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Role;
import com.banking.online_banking.utilities.BankingServiceUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// Instruct Spring to use the configured MySQL database
@Import(TestConfig.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldFetchAccountIdByAccountNumber() {
        // Given
        //For testing purposes, the customer field is not considered. However, during real-time Account object creation
        //customer field should be assigned values. Since Customer field ha a dependency on Role Entity, it
        //complicates the Customer Object creation. So, for testing purposes, the foreign key fields are not assigned.
        long accountNumber = 100001L;
        Account testAccount = new Account(111111L,accountNumber,3000.0, Account.AccountType.CHECKING);
        accountRepository.save(testAccount);

        // When
        Optional<Long> foundAccountId = accountRepository.fetchAccountIdByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccountId).isPresent();
        assertThat(foundAccountId.get()).isEqualTo(testAccount.getAccountId());
    }

    @Test
    void shouldNotFetchAccountIdByInvalidAccountNumber() {
        // Given
        long accountNumber = 123456L;

        // When
        Optional<Long> foundAccountId = accountRepository.fetchAccountIdByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccountId).isEmpty();
    }


    @Test
    void shouldFindAccountByCustomerAndAccountId() {
        // Given
        long customerId = 100001L;
        String uniqueUsername = "customer_" + UUID.randomUUID();
        Customer testCustomer = new Customer(customerId, uniqueUsername, passwordEncoder.encode("customer"));
        customerRepository.save(testCustomer);
        long accountId = 200001L;
        Account account = new Account(accountId, 123456L, 3000.0, Account.AccountType.CHECKING,testCustomer);
        accountRepository.save(account);

        // When
        Optional<Account> foundAccount = accountRepository.findByCustomerAndAccountId(testCustomer, accountId);

        // Then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getAccountId()).isEqualTo(accountId);
        assertThat(foundAccount.get().getCustomer().getId()).isEqualTo(customerId);
    }

    @Test
    void shouldNotFindAccountByInvalidCustomerAndAccountId() {
        // Given
        long customerId = 100001L;
        long accountId = 200001L;
        Customer customer = new Customer(100001L, "john_doe", "password");
        // When
        Optional<Account> foundAccount = accountRepository.findByCustomerAndAccountId(customer, accountId);

        // Then
        assertThat(foundAccount).isEmpty();
    }

    @Test
    void shouldFindAccountByAccountNumber(){
        //Given
        long accountNumber = BankingServiceUtilities.sixDigitRandomNumber();
        Account testAccount = new Account(2L,accountNumber,3000.0, Account.AccountType.CHECKING);
        accountRepository.save(testAccount);


        // When
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccount).isPresent();
    }

    @Test
    void shouldNotFindAccountByInvalidAccountNumber(){
        // Given
        long accountNumber = 123486L;

        // When
        Optional<Account> foundAccountId = accountRepository.findByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccountId).isEmpty();
    }

}


