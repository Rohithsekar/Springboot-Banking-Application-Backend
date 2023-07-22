package com.banking.online_banking.service;

import com.banking.online_banking.DTO.IdealResponse;
import com.banking.online_banking.DTO.Request;
import com.banking.online_banking.DTO.ResponseStatus;
import com.banking.online_banking.exception.UsernameAlreadyExistsException;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Role;
import com.banking.online_banking.repository.CustomerRepository;
import com.banking.online_banking.repository.RoleRepository;
import com.banking.online_banking.utilities.BankingServiceUtilities;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    private IdealResponse idealResponse;

    public AuthenticationService(CustomerRepository customerRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 TokenService tokenService,
                                 IdealResponse idealResponse) {
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.idealResponse = new IdealResponse(ResponseStatus.SUCCESS,null,null);
    }

    public IdealResponse registerCustomer(Request request) {
        Optional<Customer> existingCustomer = customerRepository.findByUsername(request.getUsername());
        if (existingCustomer.isPresent()) {
           throw new UsernameAlreadyExistsException("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Customer customer = new Customer();
        customer.setId(BankingServiceUtilities.sixDigitRandomNumber());
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Role customerRole = roleRepository.findByAuthority(Role.AuthorityLevel.CUSTOMER).get();
        Set<Role> authorities = new HashSet<>();
        Set<Customer> customers = new HashSet<>();
        authorities.add(customerRole);
        customers.add(customer);
        customerRole.setCustomers(customers);
        customer.setAuthorities(authorities);

        customerRepository.save(customer);
        String Data = "Registration successful";
        idealResponse.setData(Data);
        return idealResponse;
    }

    public IdealResponse loginCustomer(Request request) throws AuthenticationException {


        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = tokenService.generateJwt(auth);
        idealResponse.setData(token);
        return idealResponse;
    }
}
