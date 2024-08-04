package com.banking.online_banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.List;
//Don't use Lombok @Data annotation which would generate getters, setters, toString, and hashCode methods
//especially when you have cascading relationship between entities as it would result in stack overflow exception
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="account")
public class Account {

    //Both owning and inversion entity

    public enum AccountType{
        CHECKING,
        SAVINGS
    }

    @Id
    @NotNull
    private Long accountId; // Add a new primary key for the account

    @NotNull
    private Long accountNumber;
    @NotNull
    private Double balance;
    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountType type;
    @ManyToOne
    @JoinColumn(name = "customer_id") //foreign key

    private Customer customer; //it is not annotated with @NotNull to simplify object creation during testing

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;


    public Account(@NonNull Long accountId, @NonNull Long accountNumber, @NonNull Double balance, @NonNull AccountType type) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
    }

    public Account(@NotNull Long accountId, @NotNull Long accountNumber, @NotNull Double balance, @NotNull AccountType type, Customer customer) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
        this.customer = customer;
    }
}


/*
    mappedBy: This attribute specifies the field in the Transaction entity that owns the relationship. It refers
    to the corresponding field in the Transaction entity that maps the relationship back to the Account entity.
    In this case, mappedBy = "account" indicates that the account field in the Transaction entity owns the
     relationship and is responsible for mapping it back to the Account entity. This establishes the
     bidirectional association between Account and Transaction entities.

    cascade: This attribute defines the cascade operations to be applied to the associated Transaction
    entities when certain operations are performed on the Account entity. In this case, cascade = CascadeType.ALL
    specifies that all cascade operations, such as persist, merge, remove, refresh, and detach, should be
    propagated from the Account entity to the associated Transaction entities. This means that when a cascade
    operation is performed on an Account entity, the corresponding operation will also be performed on the
    associated Transaction entities.

    By using these attributes, the @OneToMany(mappedBy = "account", cascade = CascadeType.ALL) annotation
    establishes a one-to-many relationship between Account and Transaction entities, with the Transaction
    entity being the "many" side of the relationship. The mappedBy attribute ensures bidirectional mapping,
    and the cascade attribute defines the cascade operations to be applied when performing operations on
    the Account entity.
 */