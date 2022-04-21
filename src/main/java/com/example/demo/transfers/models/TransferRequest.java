package com.example.demo.transfers.models;

import lombok.Getter;

@Getter
public class TransferRequest {
    private String sourceIban;
    private String destinationIban;
    private double amount;

    public TransferRequest(String sourceIban, String destinationIban, double amount) {
        this.sourceIban = sourceIban;
        this.destinationIban = destinationIban;
        this.amount = amount;
    }
}
