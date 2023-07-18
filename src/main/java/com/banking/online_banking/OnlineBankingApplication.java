package com.banking.online_banking;

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
	CommandLineRunner run(RoleRepository roleRepository,
						  CustomerRepository customerRepository,
						  PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;

			Role adminRole = new Role();

//			Role adminRole = roleRepository.save(new Role("ADMIN"));
//			roleRepository.save(new Role("USER"));
//
//			Set<Role> roles = new HashSet<>();
//			roles.add(adminRole);
//
//			Customer admin = new Customer(1L,"rohi", passwordEncode.encode("rohi"), roles);
//			userRole.getCustomers().add(customer); // Add customer to role's customers collection
//
//
//			customerRepository.save(admin);


		};
	}

}
