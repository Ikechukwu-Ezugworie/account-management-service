package com.turog.account_management_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.dto.*;
import com.turog.account_management_service.entity.Account;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.exception.NotFoundException;
import com.turog.account_management_service.repository.AccountRepository;
import com.turog.account_management_service.repository.CustomerRepository;
import com.turog.account_management_service.service.implementations.AccountServiceImpl;
import com.turog.account_management_service.service.implementations.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("john@example.com");
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setPhoneNumber("0806621224");

        account = new Account();
        account.setId(1L);
        account.setCustomer(customer);
        account.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void createAccount_success() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(objectMapper.convertValue(account, AccountDto.class)).thenReturn(new AccountDto());

        AccountDto result = accountService.createAccount(request);

        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_missingCustomerId_throwsException() {
        CreateAccountRequest request = new CreateAccountRequest();

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(request));
    }

    @Test
    void createAccount_customerNotFound_throwsException() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(request));
    }

    @Test
    void getAccount_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(objectMapper.convertValue(account, AccountDto.class)).thenReturn(new AccountDto());

        AccountDto result = accountService.getAccount(1L);

        assertNotNull(result);
    }

    @Test
    void getAccount_notFound_throwsException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    void deposit_success() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(objectMapper.convertValue(account, AccountDto.class)).thenReturn(new AccountDto());

        AccountDto result = accountService.deposit(1L, request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), account.getBalance());
    }

    @Test
    void withdraw_success() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(objectMapper.convertValue(account, AccountDto.class)).thenReturn(new AccountDto());

        AccountDto result = accountService.withdraw(1L, request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

    @Test
    void withdraw_insufficientBalance_throwsException() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(BigDecimal.valueOf(200));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(1L, request));
    }
}

