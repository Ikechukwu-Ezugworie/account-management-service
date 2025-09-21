package com.turog.account_management_service.api.controller;

import com.turog.account_management_service.api.CustomerApi;
import com.turog.account_management_service.dto.CreateCustomerRequest;
import com.turog.account_management_service.dto.CustomerDto;
import com.turog.account_management_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CustomerDto> createCustomer(CreateCustomerRequest request) {
        CustomerDto dto = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomer(Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @Override
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(int page, int size) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }
}
