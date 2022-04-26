package com.example.demo.accounts.models;

import lombok.Getter;

@Getter
public class AccountRequest {
    private Currency currency;
    private double amount;

    public AccountRequest(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }
}
