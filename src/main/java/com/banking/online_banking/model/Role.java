package com.banking.online_banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/*
In certain cases, using Lombok's @Data annotation can result in a circular dependency issue when generating
the toString() method. This issue occurs when the toString() method recursively calls the toString() method
of related objects, creating an infinite loop.

In your case, if you have a circular dependency between Customer and Role classes due to the toString() method,
removing the @Data annotation from one of the classes, such as Role, can help resolve the circular dependency issue.

By using @Getter and @Setter annotations instead of @Data, you have more control over the generated methods.
The @Data annotation includes @ToString by default, which generates the toString() method that can cause the
circular dependency issue. By removing @Data and using @Getter and @Setter individually, you can exclude the
toString() method from being generated and break the circular dependency.
 */

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role implements GrantedAuthority {

    public enum AuthorityLevel{
        ADMIN,
        CUSTOMER,
    }

    @Id
    @Column(name="role_id")
    @NotNull
    private Integer roleId;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthorityLevel authority;

    @ManyToMany(mappedBy = "authorities")
    private Set<Customer> customers;

    public Role(){

    }

    public Role(@NotNull Integer roleId, @NotNull AuthorityLevel authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    public Role(AuthorityLevel authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.name();
    }

    public void setAuthority(AuthorityLevel authorityLevel){
        this.authority = authorityLevel;
    }

}
