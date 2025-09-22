package com.turog.account_management_service.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.dto.CreateCustomerRequest;
import com.turog.account_management_service.dto.CustomerDto;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.exception.BadRequestException;
import com.turog.account_management_service.exception.NotFoundException;
import com.turog.account_management_service.repository.CustomerRepository;
import com.turog.account_management_service.service.implementations.CustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private ObjectMapper objectMapper;

    private Customer customer;


    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("john@example.com");
        customer.setFirstName("John");
        customer.setLastName("Smith");
        customer.setPhoneNumber("0806621224");
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
    }

    @Test
    void createCustomer_success() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("john@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setPhoneNumber("0806621224");

        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.createCustomer(request);

        assertNotNull(result);
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals("john@example.com", savedCustomer.getEmail());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Smith", savedCustomer.getLastName());
        assertEquals("0806621224", savedCustomer.getPhoneNumber());
    }

    @Test
    void createCustomer_duplicateEmail_throwsException() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("john@example.com");

        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(request));
        verify(customerRepository, never()).save(any());
    }


    @Test
    void getAllCustomers_validInput_returnsPagedDtos() {
        // Arrange
        int page = 0;
        int size = 3;

        // Create a page with 3 customers (for page 0, size 3)
        List<Customer> customers = Arrays.asList(customer, new Customer(), new Customer());
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(page, size), 5); // Total 5 customers

        // Mock repository to return the page
        when(customerRepository.findAll(PageRequest.of(page, size))).thenReturn(customerPage);

        // Mock objectMapper for the first customer (others can be mocked similarly if needed)
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Smith");
        customerDto.setEmail("john@example.com");
        customerDto.setPhoneNumber("08012345678");
        customerDto.setAccounts(new ArrayList<>());

        // Act
        Page<CustomerDto> result = customerService.getAllCustomers(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getContent().size()); // Expect 3 customers per page
        assertEquals(5, result.getTotalElements()); // Total 5 customers seeded
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("john@example.com", result.getContent().get(0).getEmail());

        // Verify interaction
        verify(customerRepository).findAll(PageRequest.of(page, size));
    }

    @Test
    void getAllCustomers_negativePage_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.getAllCustomers(-1, 10)
        );
        assertEquals("Page index must not be negative", exception.getMessage());
    }

    @Test
    void getAllCustomers_zeroSize_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.getAllCustomers(0, 0)
        );
        assertEquals("Page size must be greater than zero", exception.getMessage());
    }

    @Test
    void getCustomer_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomer(1L);

        assertNotNull(result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomer_notFound_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getCustomer(1L));
    }
}