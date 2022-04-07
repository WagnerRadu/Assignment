package com.example.demo;

import com.example.demo.models.User;
import com.example.demo.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

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


}
