package com.banking.online_banking.exception;

public class MinimumAmountRequiredException extends RuntimeException{

    public MinimumAmountRequiredException(String message) {
        super(message);
    }
}
