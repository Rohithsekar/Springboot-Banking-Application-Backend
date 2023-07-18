package com.banking.online_banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Date;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction {

    //owning entity

    @Id
    @NonNull
    private long transactionId;
    private Date sentFrom;
    private Date receivedBy;
    @NonNull
    private Double amount;
    @NonNull
    private String status;
    @NonNull
    private String transactionType;
    @NonNull
    private double closingBalance;
    @ManyToOne
    @JoinColumn(name = "account_number") //foreign key
    private Account account;


}
