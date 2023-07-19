package com.banking.online_banking.repository;

import com.banking.online_banking.model.Role;
import com.banking.online_banking.service.AuthenticationService;
import com.banking.online_banking.service.TokenService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByAuthority(Role.AuthorityLevel authority);

}
