

# Daily To-Do List Application

A full-stack Spring Boot application for managing daily tasks and to-do lists.

## Features

- User authentication and authorization
- Create, read, update, and delete to-do lists
- Task management with due dates
- Task categorization and priority settings
- REST API endpoints
- Responsive web interface

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.x
- **Frontend**: Thymeleaf, Bootstrap, JavaScript
- **Database**: MySQL
- **Security**: Spring Security with BCrypt password hashing
- **Build Tool**: Maven
- **Testing**: JUnit, Mockito

## Prerequisites

- Java Development Kit (JDK) 17
- MySQL 8.0+
- Maven 3.6+
- Git

## Setup Instructions

1. Clone the repository:

   ```
   git clone https://github.com/yourusername/todolist.git
   ```

2. Configure MySQL:
    - Create a database named `todolist_db`.
    - Update `application.properties` with your MySQL database credentials.

3. Build the project:

   ```
   mvn clean install
   ```

4. Run the application:

   ```
   mvn spring-boot:run
   ```

5. Access the application:
    - Web Interface: [http://localhost:8080](http://localhost:8080)
    - API Documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/yourusername/todolist/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── security/
│   └── resources/
│       ├── static/
│       ├── templates/
│       └── application.properties
└── test/
    └── java/
```

## Key Requirements

- **Spring Boot backend with REST APIs**: The backend will expose RESTful endpoints to manage tasks and users.
- **MySQL database with JPA/Hibernate**: The application uses MySQL for data persistence with JPA/Hibernate for object-relational mapping.
- **User authentication and authorization**: Implemented using Spring Security with JWT or session-based authentication, including password hashing using BCrypt.
- **CRUD operations for tasks**: The app allows users to create, read, update, and delete tasks.
- **Responsive frontend design**: The frontend uses Thymeleaf and Bootstrap to create a responsive interface for all devices.
- **Unit testing**: JUnit and Mockito are used for unit testing the application's logic and ensuring it works as expected.
- **Transaction logging**: Implement logging using SLF4J and Logback for auditing and debugging purposes.
- **Exception handling**: A global exception handler (using @ControllerAdvice) is used to handle errors gracefully.

## Contributing

This is a capstone project for the Java Developer Bootcamp. Contributions are currently not accepted.

## License

This project is for educational purposes only.
