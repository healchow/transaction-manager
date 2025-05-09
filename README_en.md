# Project Description

This project is a simple transaction management service that currently supports creating, querying, listing, updating, and deleting transaction details.

# Project Structure

The project is based on Domain-Driven Design (DDD) principles, achieving separation of the domain layer, infrastructure layer, application layer, interface layer, and web layer:

```
transaction-manager: Transaction Management Service (Main Service)
├── transaction-auth: Transaction Authentication Service (To be extended)
├── transaction-securyti: Transaction Security Service (To be extended)
└── transaction-detail: Transaction Detail Service (Basic Implementation) 
    ├── transaction-detail-domain: Domain Layer
    ├── transaction-detail-infra: Infrastructure Layer
    ├── transaction-detail-app: Application Layer
    ├── transaction-detail-interface: Interface Layer
    ├── transaction-detail-web: Web Layer
    └── transaction-detail-docker: Docker Image Service
```

# Technical Highlights

## Project Technology Stack

Runtime Environment: JDK 21

Technology Stack: SpringBoot 3.2.3

Data Persistence Layer: No database is used yet; only in-memory caching is used to store data.

## Architecture Overview

This service is developed based on the SpringBoot 3.2.3 framework, using Spring's event listening mechanism to asynchronously handle deposit, withdrawal, and transfer transactions, improving the throughput of the transaction system.

In terms of deployment tools, this service supports:

- Quick compilation and packaging using Maven, and rapid deployment of the service using the provided startup, shutdown, and restart scripts. Refer to: [Local Deployment](./transaction-detail/README.md)

- Deployment by building Docker images and running Docker containers. Refer to: [Deploying via Docker](./transaction-detail/transaction-detail-docker/README.md)

## Main Third-Party Dependencies

| Main Dependency           | Purpose                                                                 |
| ------------------------- | ----------------------------------------------------------------------- |
| spring-boot-*            | Core dependencies of Spring Boot, including auto-configuration, dependency injection, embedded Tomcat, etc. |
| spring-*                 | Core functionality of the Spring framework and Spring MVC                |
| lombok                   | Simplifies Java code through annotations, such as automatically generating Getter/Setter, constructors, etc. |
| httpclient5              | Apache HttpClient 5, providing HTTP client functionality for sending HTTP requests |
| jackson-*                | Provides JSON parsing and generation, annotations, and binding between JSON and Java objects |
| jakarta-*                | Provides Servlet API, annotation support, and Bean validation functionality |
| commons-*                | Provides commonly used string processing and collection utility classes |
| knife4j-openapi3-*       | Provides API documentation generation and display functionality         |
| swagger-annotations      | Provides Swagger annotation support for generating API documentation    |
| slf4j-api                | Provides a logging facade interface                                     |
| log4j-*                  | Provides Log4j 2 related functionality                                 |
| spring-boot-starter-test | Provides Spring Boot testing support, such as unit tests, integration tests, etc. |
| mockito-core             | Provides Mock object support for unit testing                          |

# Service Performance Test

To evaluate the main interfaces in the high-concurrency scenario, find the performance bottleneck, and provide reference for subsequent optimization and expansion, a performance test is conducted.

## Stress Test Environment

| Environment Information | Details                                                                                |
|-------------------------|---------------------------------------------------------------------------------------|
| Server Hardware         | - CPU: [Specific Model] <br/> - Memory: [Specific Capacity] <br/> - Disk: SSD          |
| Server Software         | - Operating System: [Specific System] <br/> - Web Server: Tomcat <br/> - Database: LPDDR5 Memory |
| Network Environment     | Local direct access, latency within 1ms                                               |

## Stress Test Tool

Apache JMeter is used for stress testing.

## Test Plan        

Please refer to this file: [TransactionDetailTest.jmx](./docs/pressure-test/TransactionDetailTest.jmx), which can be opened with JMeter.

## Test Results

| Interface Name | Concurrency | Max QPS   | Average Response Time (ms) | Error Rate (%) |
| -------------- | ----------- | --------- | -------------------------- | -------------- |
| Create API     | 5000        | 949       | 3                          | 0.00           |
| Query API      | 10000       | 2897      | 1340                       | 9.12           |

- Create API Result Chart:

  <img src="./docs/img/create-api-summary.png" alt="Create-Summary" width="800px" />

- Query API Result Chart:

  <img src="./docs/img/get-api-summary.png" alt="Get-Summary" width="800px" />
