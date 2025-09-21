package com.turog.account_management_service.api;

import com.turog.account_management_service.dto.CreateCustomerRequest;
import com.turog.account_management_service.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Customers", description = "APIs for managing customers")
@RequestMapping("/customers")
public interface CustomerApi {

    @Operation(summary = "Create a new customer", description = "Registers a new customer in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid customer data")
    })
    @PostMapping
    ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerRequest request);

    @Operation(summary = "Get customer details", description = "Fetches customer details by ID, including linked accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id);

    @Operation(summary = "List all customers", description = "Retrieves a list of all registered customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    })
    @GetMapping
    ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0", required = false) int page,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10", required = false) int size);
}
