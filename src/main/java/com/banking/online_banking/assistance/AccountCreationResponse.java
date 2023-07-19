package com.banking.online_banking.assistance;

import com.banking.online_banking.model.Account;

public record AccountCreationResponse(Long accountNumber, Double balance, Account.AccountType accountType) {

}
