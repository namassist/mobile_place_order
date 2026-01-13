# Mobile Place Order API

This is a robust RESTful API designed as the backend service for a "Mobile Place Order" application. It manages products, shopping carts (as draft orders), and the order placement process, providing a seamless experience for mobile clients.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Environment Variables](#environment-variables)
- [Installation & Running](#installation--running)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

## Tech Stack

This project leverages a modern, production-ready Java stack:

- **Language:** ![Java 17](https://img.shields.io/badge/Java-17-orange)
- **Framework:** ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
- **Database:** ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Production-blue) (supports H2 for local testing)
- **ORM:** Spring Data JPA + Hibernate
- **Build Tool:** Maven

### Key Libraries

- **Lombok**: Reduces boilerplate code (Getters, Setters, Builders).
- **MapStruct**: Type-safe bean mapping between Entities and DTOs.
- **SpringDoc OpenAPI**: Automatic API documentation and Swagger UI.
- **JavaFaker**: Generates dummy data for testing purposes.

## Features

1. **Product Management**

   - Simple CRUD operations for managing the product catalog.
   - Paging support for product lists.

2. **Cart & Order System**

   - **Draft Orders**: Unique approach where a "Cart" is essentially an order in `DRAFT` status associated with a customer.
   - **Add to Cart**: Adds items to the customer's current draft order.
   - **Get Cart**: Retrieves the active draft order including all items and totals.
   - **Checkout**: Converts the status from `DRAFT` to `PLACED`.

3. **Auditing**

   - Automatic recording of `created_at` and `updated_at` timestamps for all entities.

4. **Global Error Handling**
   - Centralized exception handling to ensure consistent JSON error responses across the API.

## Prerequisites

Ensure you have the following installed on your local machine:

- **JDK 17** or higher
- **Maven** (v3.8+)
- **PostgreSQL** (v14+)

## Environment Variables

The application requires a database connection. Configure these in your environment or update `src/main/resources/application.properties`.

| Variable      | Description               | Example                                               |
| ------------- | ------------------------- | ----------------------------------------------------- |
| `DB_URL`      | JDBC URL for the database | `jdbc:postgresql://localhost:5432/mobile_place_order` |
| `DB_USERNAME` | Database username         | `postgres`                                            |
| `DB_PASSWORD` | Database password         | `secret`                                              |

## Installation & Running

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/mobile_place_order.git
   cd mobile_place_order
   ```

2. **Build the project**

   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   _Alternatively, you can run the generated JAR from the `target/` directory._

## API Documentation

Interactive API documentation is available via Swagger UI once the application is running.

- **URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`

Use this interface to explore endpoints, test requests, and view response schemas.

## Project Structure

The project follows a standard layered architecture:

```
com.example.mobile_place_order
├── config       # Configuration classes (OpenAPI, etc.)
├── controller   # REST Controllers (API Layer)
├── dto          # Data Transfer Objects (Request/Response models)
├── entity       # JPA Entities (Database Layer)
├── exception    # Global Exception Handling
├── mapper       # MapStruct Interfaces (Entity <-> DTO)
├── repository   # Spring Data Repositories
└── service      # Business Logic Layer
```
