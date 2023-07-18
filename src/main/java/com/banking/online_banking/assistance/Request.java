package com.banking.online_banking.assistance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
    private String username;
    private String password;
}
