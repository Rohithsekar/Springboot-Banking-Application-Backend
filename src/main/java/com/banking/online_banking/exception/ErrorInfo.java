package com.banking.online_banking.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {

    private String message;
    private HttpStatusCode httpStatusCode;
    private LocalDateTime timestamp;
}