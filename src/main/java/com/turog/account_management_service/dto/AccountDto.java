package com.turog.account_management_service.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private String balance;
    private Long customerId;
}