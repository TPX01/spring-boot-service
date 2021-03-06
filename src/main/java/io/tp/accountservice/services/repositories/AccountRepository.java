package io.tp.accountservice.services.repositories;

import io.tp.accountservice.services.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findById( String id );
    List<Account> findAll();
}