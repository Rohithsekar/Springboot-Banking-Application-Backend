package com.banking.online_banking.service;

import com.banking.online_banking.DTO.IdealResponse;
import com.banking.online_banking.DTO.Request;
import com.banking.online_banking.DTO.ResponseStatus;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Role;
import com.banking.online_banking.repository.CustomerRepository;
import com.banking.online_banking.repository.RoleRepository;
import com.banking.online_banking.repository.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// Instruct Spring to use the configured MySQL database
@Import(TestConfig.class)
class AuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;


    private AuthenticationService authenticationService;

    private IdealResponse idealResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                customerRepository,
                roleRepository,
                passwordEncoder,
                authenticationManager,
                tokenService,
                idealResponse
        );

        // Set up a default IdealResponse for use in the tests
        idealResponse = new IdealResponse(ResponseStatus.SUCCESS, null, null);
    }

    @Test
    @Disabled
    void shouldRegisterCustomer() {
        // given
        String username = "testuser";
        String password = "testpassword";
        String encodedPassword = "encodedPassword"; // The encoded password after encryption
        long customerId = 123456L;

        Request request = new Request(username, password);
        Customer customer = new Customer(customerId, username, encodedPassword);

        Role customerRole = new Role(9,Role.AuthorityLevel.CUSTOMER);


        given(customerRepository.findByUsername(username)).willReturn(Optional.empty());
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(roleRepository.findByAuthority(Role.AuthorityLevel.CUSTOMER)).willReturn(Optional.of(customerRole));
        given(customerRepository.save(Mockito.any(Customer.class))).willReturn(customer);


        // when
        IdealResponse response = authenticationService.registerCustomer(request);

        // then
        assertThat(response).isEqualTo(idealResponse);
        assertThat(customer.getId()).isEqualTo(customerId);
        assertThat(customer.getUsername()).isEqualTo(username);
        assertThat(customer.getPassword()).isEqualTo(encodedPassword);
        assertThat((Set<Role>)customer.getAuthorities()).containsOnly(customerRole);
        assertThat(customerRole.getCustomers()).containsOnly(customer);
        verify(customerRepository).findByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(roleRepository).findByAuthority(Role.AuthorityLevel.CUSTOMER);
        verify(customerRepository).save(Mockito.any(Customer.class));
    }
    @Test
    void loginCustomer() {
    }
}