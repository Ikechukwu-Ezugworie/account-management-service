# Customer Account Management Service

## Description

The Customer Account Management Service is a Spring Boot application for Turog Java develoepr test, designed to manage customers and their accounts. 

## How to Run the Project

1. **Prerequisites**:
   - Ensure you have Java 17+ installed.
   - Install Maven on your system.

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/Ikechukwu-Ezugworie/account-management-service.git
   cd account-management-service
   
3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   
4. **Access Swagger UI for API documentation**:
   - http://localhost:8080/swagger-ui.html


5. **Access H2 console for DB inspection**:
   
   ``` url: http://localhost:8080/h2-console
      JDBC URL: jdbc:h2:mem:testdb
      Username: sa
      Password:
   ```