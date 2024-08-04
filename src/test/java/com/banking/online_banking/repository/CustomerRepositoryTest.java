package com.banking.online_banking.repository;

import com.banking.online_banking.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// Instruct Spring to use the configured MySQL database
@Import(TestConfig.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }


    @Test
    void shouldFindCustomerByUsername(){
        //Given
        String userName = "xyz";
        Customer testCustomer = new Customer(7L, userName, "password");
        customerRepository.save(testCustomer);

        //when
        Optional<Customer> customerFound = customerRepository.findByUsername(userName);

        //then
        assertThat(customerFound).isPresent();
    }

    @Test
    void shouldNotFindCustomerByInvalidUsername(){
        //Given
        String userName = "xyz";
        //when
        Optional<Customer> customerFound = customerRepository.findByUsername(userName);

        //then
        assertThat(customerFound).isEmpty();
    }



}