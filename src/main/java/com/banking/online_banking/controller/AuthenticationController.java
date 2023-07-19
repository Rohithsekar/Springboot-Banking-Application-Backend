package com.banking.online_banking.controller;

import com.banking.online_banking.assistance.LoginResponse;
import com.banking.online_banking.assistance.Request;
import com.banking.online_banking.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> register(@RequestBody @Valid Request request){
        // Handle the valid request and return the appropriate response
        return ResponseEntity.ok(authenticationService.registerCustomer(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Request request){
        // Handle the valid request and return the appropriate response
        return ResponseEntity.ok(authenticationService.loginCustomer(request));

    }



}
