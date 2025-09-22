package com.turog.account_management_service.service.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.dto.AccountDto;
import com.turog.account_management_service.dto.CreateAccountRequest;
import com.turog.account_management_service.dto.TransactionRequest;
import com.turog.account_management_service.entity.Account;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.exception.NotFoundException;
import com.turog.account_management_service.repository.AccountRepository;
import com.turog.account_management_service.repository.CustomerRepository;
import com.turog.account_management_service.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static com.turog.account_management_service.utils.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public static final String ACCOUNT_NOT_FOUND_WITH_ID = "Account not found with ID: ";
    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AccountDto createAccount(CreateAccountRequest request) {
        log.info("Creating an account");
        if (request.getCustomerId() == null) {
            log.error("Customer ID is required");
            throw new IllegalArgumentException("Customer ID is required");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", request.getCustomerId());
                    return new EntityNotFoundException("Customer not found with ID: " + request.getCustomerId());
                });
        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        Account createdAccount = accountRepository.save(account);
        log.info("Account created successfully for customer with ID: {}", account.getCustomer().getId());
        return objectMapper.convertValue(createdAccount, AccountDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountDto getAccount(Long id) {
        log.info("Getting an account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_WITH_ID + id));
        return objectMapper.convertValue(account, AccountDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AccountDto deposit(Long id, TransactionRequest request) {
        log.info("Depositing {} into account with ID: {}", request.getAmount(), id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_WITH_ID + id));
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);
        log.info("Deposited successfully for customer with ID: {}", account.getCustomer().getId());
        return objectMapper.convertValue(account, AccountDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AccountDto withdraw(Long id, TransactionRequest request) {
        log.info("Withdrawing {} from account with ID: {}", request.getAmount(), id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND_WITH_ID + id));
        //Check sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            log.error("Insufficient balance in account ID {}: balance {}, requested {}",
                    id, account.getBalance(), request.getAmount());
            throw new IllegalArgumentException("Withdrawal exceeds balance. No overdraft allowed");
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);
        log.info("Withdrawn successfully for customer with ID: {}", account.getCustomer().getId());
        return objectMapper.convertValue(account, AccountDto.class);
    }


    private int parseAccountNumber(String accountNumber) {
        if (accountNumber == null || !accountNumber.startsWith(ACCOUNT_NUMBER_PREFIX)) {
            return MIN_ACCOUNT_NUMBER - 1;
        }
        try {
            return Integer.parseInt(accountNumber.substring(ACCOUNT_NUMBER_PREFIX.length()));
        } catch (NumberFormatException e) {
            log.warn("Invalid account number format: {}", accountNumber);
            return MIN_ACCOUNT_NUMBER - 1;
        }
    }


    private String generateUniqueAccountNumber() {
        log.debug("Generating unique account number");

        // Try random generation first
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int attempt = 0;

        while (attempt < MAX_RANDOM_ACCOUNT_CREATION_ATTEMPTS) {
            int randomNumber = MIN_ACCOUNT_NUMBER + random.nextInt(MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1);
            String accountNumber = ACCOUNT_NUMBER_PREFIX + String.format("%06d", randomNumber);

            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                log.debug("Generated unique account number: {}", accountNumber);
                return accountNumber;
            }

            log.debug("Account number {} already exists, retrying...", accountNumber);
            attempt++;
        }

        // Fallback: Find the next available number sequentially
        log.debug("Random attempts exhausted, falling back to sequential search");
        String lastAccountNumber = accountRepository.findMaxAccountNumber();
        int nextNumber = (lastAccountNumber != null) ? parseAccountNumber(lastAccountNumber) + 1 : MIN_ACCOUNT_NUMBER;

        while (nextNumber <= MAX_ACCOUNT_NUMBER) {
            String accountNumber = ACCOUNT_NUMBER_PREFIX + String.format("%06d", nextNumber);
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                log.debug("Generated unique account number (sequential): {}", accountNumber);
                return accountNumber;
            }
            nextNumber++;
        }

        log.error("Failed to generate a unique account number; all numbers in range exhausted");
        throw new IllegalStateException("No unique account number available; range exhausted");
    }
}
