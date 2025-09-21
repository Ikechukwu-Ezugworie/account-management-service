package com.turog.account_management_service.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.repository.CustomerRepository;
import com.turog.account_management_service.seeder.CustomerTestSeeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;


@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerTestSeeder customerTestSeeder;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        customerTestSeeder.seedCustomers();
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        customerTestSeeder.cleanUp();
    }

//    @Test
//    void getAllCustomers_validInput_returnsPagedDtos() {
//        // Arrange
//        int page = 0;
//        int size = 3;
//
//        // Act
//        Page<CustomerDto> result = customerService.getAllCustomers(page, size);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(3, result.getContent().size()); // Expect 3 customers per page
//        assertEquals(5, result.getTotalElements()); // Total 5 customers seeded
//        assertEquals("Customer 1", result.getContent().get(0).getFirstName());
//        assertEquals("customer1@@turog.ng", result.getContent().get(0).getEmail());
//    }

//    @Test
//    void getAllCustomers_negativePage_throwsException() {
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> customerService.getAllCustomers(-1, 10)
//        );
//        assertEquals("Page index must not be negative", exception.getMessage());
//    }
//
//    @Test
//    void getAllCustomers_zeroSize_throwsException() {
//        // Act & Assert
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> customerService.getAllCustomers(0, 0)
//        );
//        assertEquals("Page size must be greater than zero", exception.getMessage());
//    }
}