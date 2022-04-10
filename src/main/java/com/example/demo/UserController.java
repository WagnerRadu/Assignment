package com.example.demo;

import com.example.demo.models.Account;
import com.example.demo.models.Currency;
import com.example.demo.models.User;
import com.example.demo.services.AccountRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find/byId/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Optional<User> userOp = userRepository.findById(id);
        if(userOp.isPresent()) {
            return new ResponseEntity<>(userOp.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/byName/{firstName}")
    public ResponseEntity<?> getUserByFirstName(@PathVariable String firstName) {
        List<User> userList = userRepository.findByFirstName(firstName);
        if(userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<?> createAccount(@RequestBody Currency currency, @PathVariable String id) {
        Optional<User> userOp = userRepository.findById(Integer.parseInt(id));
        if(userOp.isPresent()) {
            try {
                userService.createAccount(userOp.get(), currency);
            } catch (DataAccessException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<?> getUserAccounts(@PathVariable String id) {
        List<Account> accountList = accountRepository.findByUserId(Integer.parseInt(id));
        if(accountList.isEmpty()) {
            return new ResponseEntity<>("No accounts found for this user", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        }
    }

    @GetMapping("/test")
    public void test(@RequestBody Currency currency) {
        System.out.println(currency.toString());
    }
}
