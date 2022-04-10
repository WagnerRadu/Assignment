package com.example.demo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
public class Account {
    @Id
    String iban;
    @Column(nullable = false)
    int userId;
    @Column(nullable = false)
    String currency;

    public Account(String iban, int userId, String currency) {
        this.iban = iban;
        this.userId = userId;
        this.currency = currency;
    }


//    public String generateIban(int userId, String currency) {
//        StringBuilder iban = new StringBuilder("RO" + userId + currency);
//
//    }
}
