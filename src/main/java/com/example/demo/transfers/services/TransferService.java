package com.example.demo.transfers.services;

import com.example.demo.models.Account;

import java.util.Optional;

public interface TransferService {
    boolean transferMoney(Account sourceAccount, String destinationIban, double amount);
}
