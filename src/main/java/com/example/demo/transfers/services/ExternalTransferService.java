package com.example.demo.transfers.services;

import com.example.demo.models.Account;
import com.example.demo.services.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("externalTransfer")

public class ExternalTransferService implements TransferService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean transferMoney(Account sourceAccount, String destinationIban, double amount) {
        sourceAccount.subtractAmount(amount);
        accountRepository.save(sourceAccount);
        return true;
    }
}
