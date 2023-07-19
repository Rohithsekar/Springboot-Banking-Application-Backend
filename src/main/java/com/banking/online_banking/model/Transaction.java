package com.banking.online_banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.time.LocalDate;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction {


    public enum TransactionType{
        INITIAL_DEPOSIT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        VIEW_BALANCE,
        ACCOUNT_STATEMENT,
        CHANGE_PASSWORD
    }

    //owning entity

    @Id
    @NonNull
    private String transactionId;
    private Date sentFrom;
    private Date receivedBy;
    @NonNull
    private Double amount;
    @NonNull
    private String status;
    @NonNull
    private LocalDate transactiondate;
    @NonNull
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @NonNull
    private double closingBalance;
    @ManyToOne
    @JoinColumn(name = "account_number") //foreign key
    private Account account;

    public Transaction() {
        //default constructor
    }
}
