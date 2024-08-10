# User Management System

## Overview
This project is a simple user management system implemented using Spring Boot.
It provides RESTful APIs to manage users, including CRUD operations and scheduled processing of user data.
The application uses an H2 in-memory database for development and testing purposes.

## Features
- **CRUD Operations:** Create, Read, Update, and Delete users.
- **Validation:** Input validation for user data.
- **Scheduled Tasks:** Automatically process user data every minute.
- **Error Handling:** Custom error responses for various exceptions.

## Technologies Used
- **Spring Boot:** Framework for building the RESTful application.
- **Spring Data JPA:** For ORM and database operations.
- **H2 Database:** In-memory database for development and testing.
- **JUnit & Mockito:** For unit and integration testing.
- **Lombok:** To reduce boilerplate code.

## Prerequisites
- **JDK 17**
- **Maven**

## Installation
1. **git clone https://github.com/riddhi-Jani123/user-management-system.git
2. **cd user-management-system**
3. **cd user.management.system**
3. **Run the project** in any IDE using the following command:
   ```bash
   mvn spring-boot:run

## Configuration
To access the H2 database console, navigate to:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:user_db
- **Username**: root
- **Password**: root

## API Endpoints
### Create User:

POST /api/users
{
"name": "John Doe",
"email": "john.doe@example.com"
}

### Get User by ID:
GET /api/users/{id}

### Update User:
PUT /api/users/{id}
{
"name": "Jane Doe",
"email": "jane.doe@example.com"
}

### Delete User:
DELETE /api/users/{id}

Get All User:
GET /api/users

## NOTE:
Postman collection is also provided in the project

### NOTE For Spring boot devtools
For IntelliJ users, when using Spring Boot DevTools, the dependency alone is not sufficient.
You also need to configure the settings in IntelliJ as follows:

- Enable the "Make project automatically" option. You can find this in Settings -> Build, Execution, Deployment -> Compiler.
- Additionally, go to File -> Settings -> Advanced Settings and check the option for "Allow auto-make to start even if developed application is currently running."