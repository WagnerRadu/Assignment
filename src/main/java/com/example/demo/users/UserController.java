package com.example.demo.users;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.accounts.models.Account;
import com.example.demo.accounts.models.AccountRequest;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.helpers.JwtHelper;
import com.example.demo.users.models.JwtResponse;
import com.example.demo.users.models.LoginRequest;
import com.example.demo.users.models.User;
import com.example.demo.users.services.UserService;
import com.example.demo.users.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    JwtHelper jwtHelper;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userRepository.save(user);
            return new ResponseEntity<>("User succesfully created", HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>("This email is already used!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Optional<User> userOp = userRepository.findById(id);
        if(userOp.isPresent()) {
            return new ResponseEntity<>(userOp.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("No user found for this id!", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserByFirstName(@RequestParam String firstName) {
        List<User> userList = userRepository.findByFirstName(firstName);
        if(userList.isEmpty()) {
            return new ResponseEntity<>("No users found!", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountRequest, @PathVariable String id) {
        Optional<User> userOp = userRepository.findById(Integer.parseInt(id));
        if(userOp.isPresent()) {
            try {
                userService.createAccount(userOp.get(), accountRequest.getCurrency(), accountRequest.getAmount());
            } catch (DataAccessException e) {
                return new ResponseEntity<>("This currency isn't available!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Account succesfully created", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<?> getUserAccounts(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        if(!userRepository.existsById(userId)) {
            return new ResponseEntity<>("No user found for this id!", HttpStatus.NOT_FOUND);
        }
        List<Account> accountList = accountRepository.findByUserId(userId);
        if(accountList.isEmpty()) {
            return new ResponseEntity<>("No accounts found for this user!", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        }
    }

    @GetMapping("/initials")
    public ResponseEntity<?> getInitials() {
        List<String> initialsList = userService.getUserInitials();
        if(initialsList.isEmpty()) {
            return new ResponseEntity<>("No user found!", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(initialsList, HttpStatus.OK);
        }
    }

    @GetMapping("/gmailCount")
    public ResponseEntity<?> countGmailUsers() {
        return new ResponseEntity<>(userService.countGmailUsers(), HttpStatus.OK);
    }

    @GetMapping("/lastNames")
    public ResponseEntity<?> getUserUniqueLastNames() {
        List<String> lastNamesList = userService.getUserUniqueLastNames();
        if(lastNamesList.isEmpty()) {
            return new ResponseEntity<>("No user found!", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(lastNamesList, HttpStatus.OK);
        }
    }

    @GetMapping("/firstNameInitials")
    public ResponseEntity<?> getUserFirstNameInitials() {
        return new ResponseEntity<>(userService.getFirstNameInitials(), HttpStatus.OK);
    }

    @GetMapping("/A20")
    public ResponseEntity<?> countUsersWithA20() {
        return new ResponseEntity<>(userService.countUsersWithA20(), HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> userOp = userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if(userOp.isPresent()) {
            String token;
            try {
                token = jwtHelper.generateToken(userOp.get().getId());
            } catch (JWTCreationException exception) {
                return ResponseEntity.ok("Invalid signing configuration");
            }
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return new ResponseEntity<>("Credentials are wrong", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(HttpServletRequest request, @RequestBody AccountRequest accountRequest) {
        String token = request.getHeader("Authorization");
        int userId = 0;
        try {
            userId = Integer.parseInt(jwtHelper.getUserId(token));
        } catch (JWTVerificationException e) {
            return new ResponseEntity<>("Authorization token is invalid", HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findById(userId).get(); //should always be present since it's verified with the token
        userService.createAccount(user, accountRequest.getCurrency(), accountRequest.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
