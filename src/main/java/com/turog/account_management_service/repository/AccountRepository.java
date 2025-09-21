package com.turog.account_management_service.repository;

import com.turog.account_management_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT MAX(a.accountNumber) FROM Account a WHERE a.accountNumber LIKE 'ACC%'")
    String findMaxAccountNumber();
}
