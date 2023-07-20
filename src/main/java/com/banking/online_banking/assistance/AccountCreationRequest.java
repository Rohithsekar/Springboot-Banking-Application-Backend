package com.banking.online_banking.assistance;

import com.banking.online_banking.configuration.ValidArgumentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public record AccountCreationRequest(
        @Pattern(regexp = "^(Savings|Checking)$", message = "Account type must be either 'Savings' or 'Checking'")String type,
        @DecimalMin(value = "2500",message = "Initial deposit should be atleast Rs.2500")
        @ValidArgumentType Double amount) {

}
