package com.banking.online_banking.repository;

import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// Instruct Spring to use the configured MySQL database
@Import(TestConfig.class)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {

//        // First, retrieve the Customer object with Admin authority
//        Customer adminCustomer = customerRepository.findById(1L).orElse(null);
//
//        // Check if the adminCustomer exists and has associated roles
//        if (adminCustomer != null && !adminCustomer.getAuthorities().isEmpty()) {
//
//            // Get the set of authorities (roles) for the customer
//            Set<Role> authorities = (Set<Role>)adminCustomer.getAuthorities();
//
//            // Use an iterator to iterate through the set and remove the ADMIN role if present
//            Iterator<Role> iterator = authorities.iterator();
//            while (iterator.hasNext()) {
//                Role role = iterator.next();
//                if (role.getAuthority().equals(Role.AuthorityLevel.ADMIN.toString())) {
//                    // Remove the ADMIN role
//                    iterator.remove();
//                    break; // Assuming there's only one ADMIN role, we can break out of the loop after removing it
//                }
//            }
//
//        }
//        // Save the customer to update the roles without ADMIN authority
//        customerRepository.save(adminCustomer);
//
//        // Delete the customer from the database and via the cascading relation, the ADMIN entry in the Role
//        // would now be deleted.
//        customerRepository.delete(adminCustomer);
//
//        Role adminStillExists = roleRepository.findById(1).orElseThrow();
//        System.out.println("Admin still exists after deletion" + adminStillExists);

    }

    @AfterEach
    public void teardown() {
        // Roll back any changes made during the test
    }

    @Test
    void shouldFindRoleByAuthority(){
        //Given
        Role.AuthorityLevel authorityLevel = Role.AuthorityLevel.CUSTOMER;

        //when
        Optional<Role> roleFound = roleRepository.findByAuthority(authorityLevel);

        //then
        assertThat(roleFound).isPresent();
    }

    @Test
    @Disabled
    void shouldNotFindRoleByInvalidAuthority(){
        // First, retrieve the Customer object with Admin authority
        Customer adminCustomer = customerRepository.findById(1L).orElse(null);

        // Check if the adminCustomer exists and has associated roles
        if (adminCustomer != null && !adminCustomer.getAuthorities().isEmpty()) {

            // Get the set of authorities (roles) for the customer
            Set<Role> authorities = (Set<Role>)adminCustomer.getAuthorities();

            // Use an iterator to iterate through the set and remove the ADMIN role if present
            Iterator<Role> iterator = authorities.iterator();
            while (iterator.hasNext()) {
                Role role = iterator.next();
                if (role.getAuthority().equals(Role.AuthorityLevel.ADMIN.toString())) {
                    // Remove the ADMIN role
                    iterator.remove();
                    break; // Assuming there's only one ADMIN role, we can break out of the loop after removing it
                }
            }

        }
        // Save the customer to update the roles without ADMIN authority
        customerRepository.save(adminCustomer);

        // Delete the customer from the database and via the cascading relation, the ADMIN entry in the Role
        // would now be deleted.
        customerRepository.delete(adminCustomer);

        Role adminStillExists = roleRepository.findById(1).orElseThrow(()-> new RuntimeException(("Admin still thrives")));

        //Given
        Role.AuthorityLevel authorityLevel = Role.AuthorityLevel.ADMIN;

        //when
        Optional<Role> roleFound = roleRepository.findByAuthority(authorityLevel);

        //then
        assertThat(roleFound).isEmpty();
    }







}