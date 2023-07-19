package com.banking.online_banking.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="customer")
public class Customer implements UserDetails {

    //inversion entity

    @Id
    @NonNull
    private Long id;
    @NonNull
    @Column(unique = true)
    private String username;
    @NonNull
    private String password;

    //This field is based on the assumption that a customer could have multiple roles besides
    //the regular user role. A separate table will be created to hold the multiple roles(authorities)
    //that any particular customer has.
    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="customer_role_junction",
            joinColumns = {@JoinColumn(name="customer_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    @NonNull
    private Set<Role> authorities;
    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;
       /*
    mappedBy: This attribute specifies the field in the Account entity that owns the relationship. It
    refers to the corresponding field in the Account entity that maps the relationship back to the
    Customer entity. In this case, mappedBy = "customer" indicates that the customer field in the Account
    entity owns the relationship and is responsible for mapping it back to the Customer entity. This establishes
     the bidirectional association between Customer and Account entities.
     */

    public Customer(){
        //default constructor
    }
    public Customer(@NonNull Long id, @NonNull String username, @NonNull String password, @NonNull  Set<Role> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
