package com.banking.online_banking.DTO;

import jakarta.validation.constraints.*;

public record TransferRequest(@NotNull
                              @Digits(integer = 6, fraction=0, message = "account number must have exactly 6 digits.")
                              long accountNumber,
                              @NotNull
                              @Min(value = 100, message = "The amount must be at least 100.")
                              @Max(value = 15000, message = "The amount cannot exceed 15000.")
                              double amount,
                              @NotNull
                              @Digits(integer = 6, fraction=0, message = "account number must have exactly 6 digits.")
                              long toAccount) {
}
