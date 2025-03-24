Below is a `README.md` file based on the content from the `Setup Instructions.pdf`. This file is formatted for GitHub and provides a clear guide for setting up and running the project.

```markdown
# Project Setup and Execution Guide

This guide provides step-by-step instructions to set up, configure, and run the project. Ensure you meet the prerequisites before proceeding.

---

## Prerequisites

Before starting, ensure the following are installed on your system:

- **Java Development Kit (JDK) 23**
- **Apache Maven 3.x**
- **MongoDB** (Community Edition or higher)

---

## Project Setup

### Step 1: Clone the Repository

Clone the project repository and navigate to the project directory:

```bash
git clone <repository-url>
cd <project-directory>
```

### Step 2: Install Project Dependencies

Clean and build the project using Maven:

```bash
mvn clean install
```

---

## Configure MongoDB

### Step 1: Install MongoDB

Download and install MongoDB from the [MongoDB Download Center](https://www.mongodb.com/try/download/community).

Start the MongoDB service:

```bash
mongod
```

### Step 2: Update MongoDB Connection Details

Edit the `src/main/resources/application.properties` file with your MongoDB connection string:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/<your-database-name>
```

---

## Configure Currency Exchange API

1. Register and get a **Free API Key** from [Exchange Rate API](https://app.exchangerate-api.com).
2. Add the API Key to `application.properties`:

```properties
spring.exchangerate.key=YOUR_API_KEY
```

---

## Run the Application

Start the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will be available at:

[http://localhost:8080](http://localhost:8080)

---

## Unit Testing

### Step 1: Configure MongoDB for Testing

Create a new properties file for testing:

```bash
src/test/resources/application-test.properties
```

Add the following MongoDB connection details:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/testdb
```

### Step 2: Run Unit Tests

Execute unit tests with Maven:

```bash
mvn test
```

---

## Integration Testing

### Step 1: Add Integration Test Dependencies

Ensure the following dependencies are included in your `pom.xml` under `<dependencies>`:

```xml
<!-- Spring Boot Starter Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Embedded MongoDB for testing -->
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <version>3.0.0</version>
    <scope>test</scope>
</dependency>

<!-- RestAssured for API testing -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>
```

### Step 2: Run Integration Tests

Execute integration tests with Maven:

```bash
mvn verify
```

---

## Dependencies Summary

| Dependency                  | Purpose                              |
|-----------------------------|--------------------------------------|
| Spring Boot Starter Web     | Build RESTful APIs                   |
| Spring Data MongoDB         | MongoDB integration                  |
| Mockito                     | Mocking framework for testing        |
| JUnit                       | Unit testing framework               |
| Spring Boot Starter Test    | Testing support for Spring           |
| Flapdoodle Embedded MongoDB | Embedded MongoDB for tests           |
| RestAssured                 | REST API integration testing         |

---

## Security Testing

Install **ngrok** to expose the Spring Boot app for security testing:

```bash
ngrok http 8080
```

---

## Performance Testing

Tools for performance testing:

- **Apache JMeter**
- **Postman Collection Runner**

To run JMeter on Windows:

```bash
jmeter.bat
```

Add headers like:

```bash
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

---

## Useful Links

- [Java 23 Download](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven Download](https://maven.apache.org/download.cgi)
- [MongoDB Installation Guide](https://docs.mongodb.com/manual/installation/)
- [Exchange Rate API](https://app.exchangerate-api.com)

---

**Author**: Mannapperuma MVN / IT22125866
```

### Notes:
- Replace `<repository-url>` and `<project-directory>` with the actual Git repository URL and project directory name.
- Replace `<your-database-name>` with the name of your MongoDB database.
- Replace `YOUR_API_KEY` with your actual API key from Exchange Rate API.

This `README.md` file is ready to be added to your GitHub repository and provides a comprehensive guide for setting up and running the project.