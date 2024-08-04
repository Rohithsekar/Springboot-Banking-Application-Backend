package com.banking.online_banking.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

        public record AccountCreationRequest(
                @NotNull(message = "Account type must be provided.Account type must be either 'Savings' or 'Checking'")
                @Pattern(regexp = "^(Savings|Checking)$", message = "Account type must be either 'Savings' or 'Checking'")String type,
                @DecimalMin(value = "2500",message = "Initial deposit should be atleast Rs.2500")
                @NotNull(message="Please provide a valid value for the amount field") double amount) {

        }

/*
        @NotNull(message = "Account type must be provided.Account type must be either 'Savings' or 'Checking'")
        @Pattern(regexp = "^(Savings|Checking)$", message = "Account type must be either 'Savings' or 'Checking'")String type,
        @DecimalMin(value = "2500",message = "Initial deposit should be atleast Rs.2500")
        @ValidArgumentType Double amount) {

 */