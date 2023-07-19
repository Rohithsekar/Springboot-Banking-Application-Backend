package com.banking.online_banking.service;


import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;


//    @Autowired
    public TokenService(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    public String generateJwt(Authentication auth){

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        /*
        the map() function is used to transform the stream of GrantedAuthority objects into a stream of their
        corresponding authority strings. Let's break down the code to understand it:


        Here's how each part of the code works:

        auth.getAuthorities() returns a collection of GrantedAuthority objects. This collection represents the
        authorities/roles assigned to the user.

        .stream() converts the collection into a stream, which allows for sequential processing of the elements.

        .map(GrantedAuthority::getAuthority) is an intermediate operation that transforms each GrantedAuthority
         object in the stream into its corresponding authority string. Here, the method reference
         GrantedAuthority::getAuthority is used as a shorthand for invoking the getAuthority() method on
         each GrantedAuthority object.

        .collect(Collectors.joining(" ")) is a terminal operation that collects the transformed authority
        strings into a single string. The Collectors.joining(" ") part specifies that the authority strings
        should be concatenated with a space (" ") as the delimiter.

        For example, if the authorities are ["ROLE_ADMIN", "ROLE_USER"], the resulting string will be
        "ROLE_ADMIN ROLE_USER".

        So, the map() function is responsible for extracting the authority strings from the GrantedAuthority
        objects, allowing them to be concatenated into a single string using Collectors.joining().
         */

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .subject(auth.getName())
                .claim("roles", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}


