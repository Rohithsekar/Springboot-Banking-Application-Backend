package com.banking.online_banking.repository;

import com.banking.online_banking.model.Account;
import com.banking.online_banking.model.Customer;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    // Add a new query method to find the account by both customerId and accountId
    @Query(value="SELECT account_id FROM account WHERE account_number=?1", nativeQuery = true)
    Optional<Long> fetchAccountIdByAccountNumber(long accountNumber);

    Optional<Account> findByAccountNumber(long accountNumber);
    Optional<Account> findByCustomerAndAccountId(Customer customer, Long accountId);
}

