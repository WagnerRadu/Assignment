package com.example.demo.accounts.models;

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
    @Column(nullable = false)
    Double amount;

    public Account(String iban, int userId, String currency, double amount) {
        this.iban = iban;
        this.userId = userId;
        this.currency = currency;
        this.amount = amount;
    }

    public void subtractAmount(double amount) {
        this.amount -= amount;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }
}
