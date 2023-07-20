package com.banking.online_banking.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorDetails {
    private HttpStatusCode httpStatusCode;
    private Map<String, String> errors; // Use Map instead of List

    public ErrorDetails(HttpStatusCode httpStatusCode, Map<String, String> errors) {
        super();
        this.httpStatusCode = httpStatusCode;
        this.errors = errors;
    }

    public ErrorDetails(HttpStatusCode httpStatusCode, String field, String error) {
        super();
        this.httpStatusCode = httpStatusCode;
        this.errors = new HashMap<>();
        this.errors.put(field, error);
    }

}
