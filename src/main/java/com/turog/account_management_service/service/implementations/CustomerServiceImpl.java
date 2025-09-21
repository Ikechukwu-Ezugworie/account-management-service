package com.turog.account_management_service.service.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.dto.CreateCustomerRequest;
import com.turog.account_management_service.dto.CustomerDto;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.exception.BadRequestException;
import com.turog.account_management_service.exception.NotFoundException;
import com.turog.account_management_service.repository.CustomerRepository;
import com.turog.account_management_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public CustomerDto createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with email: {}", request.getEmail());
        if (customerRepository.existsByEmail(request.getEmail())) {
            log.info("Customer with email: {} already exists", request.getEmail());
            throw new BadRequestException(
                    String.format("Customer with email '%s' already exists", request.getEmail())
            );
        }
        Customer customer = objectMapper.convertValue(request, Customer.class);
        Customer createdCustomer = customerRepository.save(customer);
        log.info("Successfully created customer");
        return objectMapper.convertValue(createdCustomer, CustomerDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CustomerDto getCustomer(Long id) {
        log.info("Fetching customer details with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with ID: " + id));
        return objectMapper.convertValue(customer, CustomerDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CustomerDto> getAllCustomers(int page, int size) {
        log.info("Fetching all customers");
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customer -> objectMapper.convertValue(customer, CustomerDto.class));
    }
}
