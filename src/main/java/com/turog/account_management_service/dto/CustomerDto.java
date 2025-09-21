package com.turog.account_management_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<AccountDto> accounts;
}