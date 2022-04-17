package com.example.demo.users.services;

import com.example.demo.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "select * from users where first_name like %?1%", nativeQuery = true)
    List<User> findByFirstName(String firstName);

    List<User> findAll();
}
