package com.example.demo.transfers.services;

import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("internalTransfer")

public class InternalTransferService implements TransferService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean transferMoney(Account sourceAccount, String destinationIban, double amount) {
        Optional<Account> destinationAccountOp = accountRepository.findByIban(destinationIban);
        if(destinationAccountOp.isEmpty()) {
            return false;
        }
        Account destinationAccount = destinationAccountOp.get();
        if(!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            return false;
        }
        sourceAccount.subtractAmount(amount);
        destinationAccount.addAmount(amount);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return true;
    }
}
