package com.turog.account_management_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Schema(description = "Request body for creating a new account")
public class CreateAccountRequest {
    @Schema(description = "Unique account number (optional, auto-generated if not provided)", nullable = true)
    private String accountNumber;

    @PositiveOrZero(message = "Balance must be zero or positive")
    @Schema(description = "Initial account balance", example = "0.00", nullable = true)
    private BigDecimal balance;

    @NotBlank(message = "Customer ID is required")
    @Schema(description = "ID of the customer who owns the account", example = "1")
    private Long customerId;
}