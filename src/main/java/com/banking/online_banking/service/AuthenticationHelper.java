package com.banking.online_banking.service;

import com.banking.online_banking.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationHelper implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final CustomerRepository customerRepository;

    public AuthenticationHelper(PasswordEncoder encoder, CustomerRepository customerRepository) {
        this.encoder = encoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }
}
