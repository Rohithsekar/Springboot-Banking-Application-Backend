package com.banking.online_banking.controller;



import com.banking.online_banking.DTO.AccountCreationRequest;
import com.banking.online_banking.DTO.IdealResponse;
import com.banking.online_banking.DTO.TransferRequest;
import com.banking.online_banking.service.CustomerService;
import jakarta.validation.Valid;
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
    public IdealResponse createAccount(@Valid @RequestBody AccountCreationRequest creationRequest){
        return customerService.createAccount(creationRequest);
    }

    @PostMapping("/transfer")
    public IdealResponse transfer(@Valid @RequestBody TransferRequest transferRequest){
        return customerService.transferAmount(transferRequest);
    }




}
