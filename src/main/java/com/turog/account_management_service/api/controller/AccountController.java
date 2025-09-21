package com.turog.account_management_service.api.controller;

import com.turog.account_management_service.api.AccountApi;
import com.turog.account_management_service.dto.AccountDto;
import com.turog.account_management_service.dto.CreateAccountRequest;
import com.turog.account_management_service.dto.TransactionRequest;
import com.turog.account_management_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountDto> createAccount(CreateAccountRequest request) {
        AccountDto dto = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @Override
    public ResponseEntity<AccountDto> deposit(Long id, TransactionRequest request) {
        AccountDto dto = accountService.deposit(id, request);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AccountDto> withdraw(Long id, TransactionRequest request) {
        AccountDto dto = accountService.withdraw(id, request);
        return ResponseEntity.ok(dto);
    }
}
