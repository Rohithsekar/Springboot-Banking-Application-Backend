package com.banking.online_banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


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
        TRANSFER
    }

    //owning entity

    @Id
    @NotBlank
    private String transactionId;
    private Date sentFrom;
    private Date receivedBy;
    @NotNull
    private Double amount;
    @NotBlank
    private String status;
    @NotNull
    private LocalDate transactionDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private double closingBalance;
    @ManyToOne
    @JoinColumn(name = "account_number") //foreign key
    @NotNull
    private Account account;

    public Transaction() {
        //default constructor
    }
}
