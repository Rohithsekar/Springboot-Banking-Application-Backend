package com.banking.online_banking;

import com.banking.online_banking.DAO.TransactionResponse;
import com.banking.online_banking.model.Customer;
import com.banking.online_banking.model.Role;
import com.banking.online_banking.repository.CustomerRepository;
import com.banking.online_banking.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class OnlineBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingApplication.class, args);
	}

	@Bean
	public TransactionResponse transactionResponse() {
		return new TransactionResponse();
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository,
						  CustomerRepository customerRepository,
						  PasswordEncoder passwordEncode) {
		return args -> {
			if (roleRepository.findByAuthority(Role.AuthorityLevel.ADMIN).isPresent() &&
					roleRepository.findByAuthority(Role.AuthorityLevel.CUSTOMER).isPresent()) {
				return;
			}

			Set<Customer> customers = new HashSet<>();
			Set<Role> authorities = new HashSet<>();

			if (!roleRepository.findByAuthority(Role.AuthorityLevel.ADMIN).isPresent()) {
				Customer admin = new Customer();
				admin.setId(1L);
				admin.setUsername("rohi");
				admin.setPassword(passwordEncode.encode("rohi_123"));

				Role adminRole = new Role();
				adminRole.setRoleId(1);
				adminRole.setAuthority(Role.AuthorityLevel.ADMIN);

				customers.add(admin);
				adminRole.setCustomers(customers);

				authorities.add(adminRole);
				admin.setAuthorities(authorities);

				customerRepository.save(admin);
				customers.clear();
				authorities.clear();

			}

			if (!roleRepository.findByAuthority(Role.AuthorityLevel.CUSTOMER).isPresent()) {

				Customer customer = new Customer();
				customer.setId(2L);
				customer.setUsername("customer");
				customer.setPassword(passwordEncode.encode("customer_123"));

				Role customerRole = new Role();
				customerRole.setRoleId(2);
				customerRole.setAuthority(Role.AuthorityLevel.CUSTOMER);
				customers.add(customer);
				customerRole.setCustomers(customers);

				authorities.add(customerRole);
				customer.setAuthorities(authorities);
				customerRepository.save(customer);
				customers.clear();
				authorities.clear();
			}
		};
	}
}



