package com.example.demo;

import com.example.demo.models.User;
import com.example.demo.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
