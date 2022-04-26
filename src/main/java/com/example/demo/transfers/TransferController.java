package com.example.demo.transfers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.helpers.JwtHelper;
import com.example.demo.transfers.models.TransferRequest;
import com.example.demo.transfers.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController

public class TransferController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired @Qualifier("internalTransfer")
    TransferService internalTransferService;

    @Autowired @Qualifier("externalTransfer")
    TransferService externalTransferService;

    @Autowired
    JwtHelper jwtHelper;

    @PutMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest transferRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        int userId = 0;
        try {
            userId = Integer.parseInt(jwtHelper.getUserId(token));
        } catch (JWTVerificationException e) {
            return new ResponseEntity<>("Authorization token is invalid", HttpStatus.UNAUTHORIZED);
        }
        String sourceIban = transferRequest.getSourceIban();
        Optional<Account> sourceAccountOp = accountRepository.findByIban(sourceIban);
        if(sourceAccountOp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account sourceAccount = sourceAccountOp.get();
        if(userId != sourceAccount.getUserId()) {
            return new ResponseEntity<>("JWT doesn't match with source iban", HttpStatus.CONFLICT);
        }
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
