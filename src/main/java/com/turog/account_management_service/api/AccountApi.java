package com.turog.account_management_service.api;

import com.turog.account_management_service.dto.AccountDto;
import com.turog.account_management_service.dto.CreateAccountRequest;
import com.turog.account_management_service.dto.TransactionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts", description = "APIs for managing customer accounts")
@RequestMapping("/accounts")
public interface AccountApi {

    @Operation(summary = "Create a new account", description = "Creates a new bank account for an existing customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping
    ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest request);

    @Operation(summary = "Get account details", description = "Fetches account details by account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<AccountDto> getAccount(@PathVariable Long id);

    @Operation(summary = "Deposit money", description = "Deposits funds into an existing account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Invalid deposit amount")
    })
    @PatchMapping("/{id}/deposit")
    ResponseEntity<AccountDto> deposit(@PathVariable Long id, @Valid @RequestBody TransactionRequest request);

    @Operation(summary = "Withdraw money", description = "Withdraws funds from an account. Overdraft is not allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid request"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PatchMapping("/{id}/withdraw")
    ResponseEntity<AccountDto> withdraw(@PathVariable Long id, @Valid @RequestBody TransactionRequest request);
}
