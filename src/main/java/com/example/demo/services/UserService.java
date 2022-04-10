package com.example.demo.services;

import com.example.demo.models.Account;
import com.example.demo.models.Currency;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired AccountRepository accountRepository;

    public void createAccount(User user, Currency currency) {
        String iban = "RO" + user.getId() + (user.getAccounts().size() + 1) + currency;
        Account account = new Account(iban, user.getId(), currency.toString());
        accountRepository.save(account);
        user.getAccounts().add(account);
    }
}
