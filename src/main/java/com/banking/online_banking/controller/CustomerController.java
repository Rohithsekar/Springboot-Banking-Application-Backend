package com.banking.online_banking.controller;



import com.banking.online_banking.assistance.AccountCreationRequest;
import com.banking.online_banking.assistance.AccountCreationResponse;
import com.banking.online_banking.model.Account;
import com.banking.online_banking.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/createAccount")
    public AccountCreationResponse createAccount(@RequestBody AccountCreationRequest creationRequest){
        return customerService.createAccount(creationRequest);
    }
}
