package com.turog.account_management_service.service;

import com.turog.account_management_service.dto.AccountDto;
import com.turog.account_management_service.dto.CreateAccountRequest;
import com.turog.account_management_service.dto.TransactionRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    /**
     * Create account
     * @param request account request object
     * @return account created
     */
    AccountDto createAccount(CreateAccountRequest request);

    /**
     * Get account by ID
     * @param id account ID
     * @return account found
     */
    AccountDto getAccount(Long id);

    /**
     * Deposit into account
     * @param id account ID
     * @param request deposit request object
     * @return
     */
    AccountDto deposit(Long id, TransactionRequest request);


    /**
     * Withdraw from account
     * @param id account ID
     * @param request withdrawal request object
     * @return
     */
    AccountDto withdraw(Long id, TransactionRequest request);
}
