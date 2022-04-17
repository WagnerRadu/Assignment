package com.example.demo.transfers.services;

import com.example.demo.accounts.models.Account;

public interface TransferService {
    boolean transferMoney(Account sourceAccount, String destinationIban, double amount);
}
