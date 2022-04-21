package com.example.demo.transfers;

import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.transfers.models.TransferRequest;
import com.example.demo.transfers.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController

public class TransferController {


    @Autowired
    AccountRepository accountRepository;

    @Autowired @Qualifier("internalTransfer")
    TransferService internalTransferService;

    @Autowired @Qualifier("externalTransfer")
    TransferService externalTransferService;

    @PutMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest transferRequest) {
        String sourceIban = transferRequest.getSourceIban();
        Optional<Account> sourceAccountOp = accountRepository.findByIban(sourceIban);
        if(sourceAccountOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account sourceAccount = sourceAccountOp.get();
        double amount = transferRequest.getAmount();
        if(sourceAccount.getAmount() < amount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String destinationIban = transferRequest.getDestinationIban();
        if(destinationIban.startsWith("ROWB")) {
            if(!internalTransferService.transferMoney(sourceAccount, destinationIban, amount))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else externalTransferService.transferMoney(sourceAccount, destinationIban, amount);
        return new ResponseEntity<>("Transfer succesful", HttpStatus.OK);
    }
}
