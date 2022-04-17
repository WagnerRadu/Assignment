package com.example.demo.transfers.services;

import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
