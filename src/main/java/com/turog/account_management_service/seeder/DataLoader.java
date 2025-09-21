package com.turog.account_management_service.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turog.account_management_service.dto.CustomerDto;
import com.turog.account_management_service.entity.Account;
import com.turog.account_management_service.entity.Customer;
import com.turog.account_management_service.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public DataLoader(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding test data to database");
        InputStream inputStream = getClass().getResourceAsStream("/customers.json");
        if (inputStream == null) {
            log.info("JSON file not found!");
            return;
        }

        List<CustomerDto> customerDtos = objectMapper.readValue(inputStream, new TypeReference<>() {
        });

        List<Customer> customers = customerDtos.stream().map(customerDto -> {
            Customer customer = new Customer();
            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setEmail(customerDto.getEmail());
            customer.setPhoneNumber(customerDto.getPhoneNumber());

            if (customerDto.getAccounts() != null) {
                List<Account> accounts = customerDto.getAccounts().stream().map(accountDto -> {
                    Account account = new Account();
                    account.setAccountNumber(accountDto.getAccountNumber());
                    account.setBalance(BigDecimal.valueOf(Double.valueOf(accountDto.getBalance())));
                    account.setCustomer(customer);
                    return account;
                }).toList();
                customer.setAccounts(accounts);
            }

            return customer;
        }).toList();

        customerRepository.saveAll(customers);

       log.info("Seeded " + customers.size() + " customers into the database for quick testing");
    }
}
