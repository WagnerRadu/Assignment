package com.example.demo.transfers.models;

import lombok.Getter;

@Getter
public class TransferRequest {
    private String sourceIban;
    private String destinationIban;
    private double amount;
}
