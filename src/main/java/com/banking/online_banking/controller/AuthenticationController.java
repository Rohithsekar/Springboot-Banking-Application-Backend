package com.banking.online_banking.controller;

import com.banking.online_banking.assistance.LoginResponse;
import com.banking.online_banking.assistance.Request;
import com.banking.online_banking.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public String register(@RequestBody Request request){
        return authenticationService.registerCustomer(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Request request){
        return authenticationService.loginCustomer(request);

    }



}
