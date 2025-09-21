package com.turog.account_management_service.service;

import com.turog.account_management_service.dto.CreateCustomerRequest;
import com.turog.account_management_service.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public interface CustomerService {

    /**
     * Create a customer
     * @param request
     * @return
     */
    CustomerDto createCustomer(CreateCustomerRequest request);

    /**
     * Get a customer by ID
     * @param id customer's ID
     * @return
     */
    CustomerDto getCustomer(Long id);

    /**
     * Get all customers
     * @return
     */
    Page<CustomerDto> getAllCustomers(int page, int size);
}
