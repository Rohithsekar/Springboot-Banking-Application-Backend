package com.banking.online_banking.repository;

import com.banking.online_banking.DAO.TransactionResponse;
import com.banking.online_banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {


}
