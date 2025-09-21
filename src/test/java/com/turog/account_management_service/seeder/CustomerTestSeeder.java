package com.turog.account_management_service.seeder;

import com.turog.account_management_service.entity.Account;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.repository.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomerTestSeeder {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerTestSeeder(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Seeds the database with customers and their accounts from a JSON file.
     *
     * @return List of created Customer entities
     * @throws IOException if reading or parsing the JSON file fails
     */
    public List<Customer> seedCustomers() throws IOException {
        // Load JSON file from resources
        ClassPathResource resource = new ClassPathResource("customers-test-data.json");
        // Parse JSON into List<Customer>
        List<Customer> customers = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});

        // Set bidirectional relationship for accounts
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                account.setCustomer(customer);
            }
        }

        return customerRepository.saveAll(customers);
    }

    /**
     * Deletes all customers (and their accounts due to cascade) from the database.
     */
    public void cleanUp() {
        customerRepository.deleteAll();
    }
}