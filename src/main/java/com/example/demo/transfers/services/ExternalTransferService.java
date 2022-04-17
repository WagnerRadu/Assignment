package com.example.demo.transfers.services;

import com.example.demo.models.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("externalTransfer")

public class ExternalTransferService implements TransferService {
    @Override
    public boolean transferMoney(Account sourceAccount, String destinationIban, double amount) {
        sourceAccount.subtractAmount(amount);
        return true;
    }
}
