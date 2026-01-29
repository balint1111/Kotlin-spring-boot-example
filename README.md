# Kotlin Spring Boot Example

A RESTful API example built with Kotlin and Spring Boot.

## Features

- **Spring Boot 4.0.0** with Kotlin
- **RESTful API** with CRUD operations
- **JPA/Hibernate** for data persistence
- **H2 In-Memory Database** for development
- **Comprehensive Testing** with JUnit 5 and Mockito
- **Gradle** build system with Kotlin DSL

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/example/demo/
│   │       ├── DemoApplication.kt      # Main application entry point
│   │       ├── controller/
│   │       │   └── UserController.kt   # REST API endpoints
│   │       ├── service/
│   │       │   └── UserService.kt      # Business logic
│   │       ├── repository/
│   │       │   └── UserRepository.kt   # Data access layer
│   │       └── model/
│   │           └── User.kt             # Entity model
│   └── resources/
│       └── application.yml             # Configuration
└── test/
    └── kotlin/
        └── com/example/demo/
            ├── DemoApplicationTests.kt
            ├── controller/
            │   └── UserControllerTest.kt
            └── service/
                └── UserServiceTest.kt
```

## Prerequisites

- Java 17 or higher
- Gradle (included via wrapper)

## Getting Started

### Build the project

```bash
./gradlew build
```

### Run the application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Run tests

```bash
./gradlew test
```

## API Endpoints

### User API

| Method | Endpoint          | Description           |
|--------|------------------|-----------------------|
| GET    | `/api/users`     | Get all users         |
| GET    | `/api/users/{id}`| Get user by ID        |
| POST   | `/api/users`     | Create new user       |
| PUT    | `/api/users/{id}`| Update user           |
| DELETE | `/api/users/{id}`| Delete user           |

### Example Requests

#### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}'
```

#### Get All Users

```bash
curl http://localhost:8080/api/users
```

#### Get User by ID

```bash
curl http://localhost:8080/api/users/1
```

#### Update User

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"John Updated","email":"john@example.com"}'
```

#### Delete User

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Database

The application uses an H2 in-memory database for development. You can access the H2 console at:

```
http://localhost:8080/h2-console
```

Connection details:
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave blank)

## Technology Stack

- **Kotlin** 1.9.20
- **Spring Boot** 4.0.0
- **Spring Data JPA**
- **H2 Database**
- **JUnit 5**
- **Mockito**
- **Gradle** 8.5
