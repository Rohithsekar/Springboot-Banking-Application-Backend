package com.banking.online_banking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {

    @Pattern(regexp ="^[a-zA-Z]+$", message="Username should only contain letters")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\s])(?=.{8,})[^\\s]*$",
            message="Invalid password. Password should contain minimum 8 characters with atleast one capital letter, " +
                    "atleast one small letter, atleast one number , atleast one non-whitespace character")
    private String password;
}
