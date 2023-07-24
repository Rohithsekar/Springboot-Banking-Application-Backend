package com.banking.online_banking.configuration;

import com.banking.online_banking.model.Role;
import com.banking.online_banking.utilities.RSAKeyProperties;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class Security {
    private final RSAKeyProperties keys;

    private Logger log = LoggerFactory.getLogger(Security.class);

    public Security(RSAKeyProperties keys){
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        log.debug("inside password Encoder method");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        log.debug("inside authManager method");
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.debug("inside filter chain method");
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/admin/**").hasRole(Role.AuthorityLevel.ADMIN.name());
                    auth.requestMatchers("/customer/**").hasAnyRole(Role.AuthorityLevel.CUSTOMER.name(), Role.AuthorityLevel.ADMIN.name());
                    auth.anyRequest().authenticated();
                });

        /*
        .http.oauth2ResourceServer(oauth2 -> { ... }): Configures the application as an OAuth 2.0 resource server,
        enabling the validation of JSON Web Tokens (JWT) sent in the request headers for authentication.
         */

        http.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> {
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    });
        });
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        log.debug("********inside jwtDecoder method********");
        /*
        JwtDecoder is responsible for decoding JSON Web Tokens (JWT) and extracting the token's claims and information.

        NimbusJwtDecoder.withPublicKey(keys.getPublicKey()): Creates a JwtDecoder with a public key used to
        verify the JWT signature
         */
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    //JwtEncoder is responsible for encoding and signing JWTs
    @Bean
    public JwtEncoder jwtEncoder(){
        log.debug("*********inside jwtEncoder method**********");
        /*
        A JWK (JSON Web Key) is created with the keys.getPublicKey() and keys.getPrivateKey() methods. It is used to
         represent the key pair used for signing and verifying JWTs.
         */
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        //A JWKSource is created with the JWKSet containing the previously created JWK.
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        //A NimbusJwtEncoder is returned, using the JWKSource to sign the JWTs.
        return new NimbusJwtEncoder(jwks);
    }

    /*
    The JwtAuthenticationConverter is responsible for converting JWT claims into GrantedAuthority objects used
    for authentication in Spring Security.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        log.debug("*******inside jwtAuthenticationConverter method*******");

         //   A JwtGrantedAuthoritiesConverter is created, which specifies how to extract authorities from JWT claims.
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // The authority claim name is set to "roles"
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        //the authority prefix is set to "ROLE_". This means that authorities listed in the "roles" claim of the JWT
        // will be converted into GrantedAuthority objects with "ROLE_" prefix, which is the typical convention used
        // in Spring Security for role-based access control.
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // The JwtGrantedAuthoritiesConverter is set as the converter for the JwtAuthenticationConverter.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}



