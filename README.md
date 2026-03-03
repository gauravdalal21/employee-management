# Employee Management - Spring Boot Application

A Spring Boot REST API application for managing employees, departments, projects, and employee-project assignments.

## Tech Stack

- **Java 17**
- **Spring Boot 3.x.x**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **JUnit 5 + Mockito** (Unit Tests)

## Project Structure

```
employee-management/
├── src/main/java/com/example/employeemanagement/
│   ├── config/          # DataLoader for seed data
│   ├── controller/     # REST controllers
│   ├── dto/            # Request & Response DTOs
│   ├── entity/         # JPA entities
│   ├── exception/      # Global exception handling
│   ├── repository/     # JPA repositories
│   └── service/        # Business logic
└── src/test/           # Unit tests
```

## Database Schema (4 Tables)

| Table | Description |
|-------|-------------|
| **departments** | Department info (name, location) |
| **employees** | Employee info (name, email, salary, hire_date, department_id) |
| **projects** | Project info (name, description, department_id) |
| **employee_projects** | Many-to-many link (employee_id, project_id, role, assigned_date) |

## Prerequisites

- Java 17
- Maven 3.6+
- MySQL 9.0+ (running on localhost:3306)

## Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

## Running the Application

```bash
cd /Users/gauravdalal/employee-management
mvn spring-boot:run
```

The application will create the database, tables, and seed 60 employees, 6 departments, 9 projects, and 40 assignments on first run.

## API Endpoints

- **Employees:** GET/POST/PUT/DELETE `/api/employees`
- **Departments:** GET/POST/PUT/DELETE `/api/departments`
- **Projects:** GET/POST/PUT/DELETE `/api/projects`
- **Employee-Projects:** GET/POST/PUT/DELETE `/api/employee-projects`

## Running Tests

```bash
mvn test
```
