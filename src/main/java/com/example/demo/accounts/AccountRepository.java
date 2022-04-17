package com.example.demo.accounts;

import com.example.demo.accounts.models.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    List<Account> findByUserId(int userId);

    boolean existsByIban(String iban);

    Optional<Account> findByIban(String iban);
}
