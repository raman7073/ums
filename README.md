User Management System
This is a User Management System implemented using Spring Boot. It provides user login, and management features with built-in security using JWT (JSON Web Token) authentication and unit test cases.

Features->

User Login: Authenticates users based on their credentials using JWT authentication and generates a secure token.
User Management: Provides functionality to view, edit, and delete user profiles.
Security: Implements JWT authentication for secure user access and protects endpoints from unauthorized access.
Unit Test Cases: Includes unit test cases using JUnit and Mockito to ensure code functionality and integrity.

Technologies Used->
Spring Boot
Spring Security
JSON Web Token (JWT)
JUnit
Mockito
Java
Maven

Getting Started ->
Clone the repository: git clone (https://github.com/raman7073/ums.git)
Navigate to the project directory: cd ums
Build the project: mvn clean install
Run the application: mvn spring-boot:run
Access the application in your browser: http://localhost:8080

Configuration ->
Application configuration can be modified in the application.properties file.
Security configuration and JWT-related settings can be customized in the configs/SecurityConfig.java file and security package.
Unit Tests
Unit tests are located in the src/test directory.
Run unit tests: mvn test
