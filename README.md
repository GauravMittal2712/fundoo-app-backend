# Fundoo Application Backend

Fundoo is a high-performance, collaborative note-taking web application backend modeled after Google Keep. It provides a RESTful API for managing user profiles, stateless JWT-based authentication, rich-text notes (with pins, archivals, trashing, and custom colors), labels, and real-time collaboration features supported by background schedulers and cache layers.

---

## 🚀 Pre-Running Checklist (Must Check Before Running!)

Before you compile and run the application, make sure the following checklist is completed:

### 1. Database Configuration
* **MySQL Server**: Ensure your MySQL server is running.
* **Database Setup**: You must create a database named `fundoo_db` before launching the app:
  ```sql
  CREATE DATABASE fundoo_db;
  ```
* **Credentials**: The application uses profile-specific settings. Check the configurations in [application-dev.properties](file:///d:/BridgeLabz/MagicSoftware/fundoo/src/main/resources/application-dev.properties):
  * **Default Username**: `root`
  * **Default Password**: `Passwd@123`
  * **URL**: `jdbc:mysql://localhost:3306/fundoo_db`
  *(If your local MySQL credentials differ, please update this file before running).*

### 2. Caching Configuration
* **Redis Server**: The application uses Redis for cache management. Ensure Redis is running on port `6379`.
  * Command to verify Redis: `redis-cli ping` (should respond with `PONG`).

### 3. Event Broker & Messaging
* **Apache Kafka**: The application uses Kafka for processing registration events, reminders, and audit logs.
  * Ensure ZooKeeper and Kafka Broker are running locally at `localhost:9092`.
  * The application will automatically construct the required topics (`user-events`, `reminder-alerts`, `audit-logs`) upon startup.

### 4. Java SDK Version
* **JDK Version**: Make sure your local JDK version is set to **Java 21**.
  * Verify in terminal: `java -version`
  * Verify `JAVA_HOME` environment variable points to your Java 21 path (e.g. `C:\Program Files\Java\jdk-21.0.11` on Windows).

---

## 🛠️ Technology Stack & Architecture

* **Framework**: Spring Boot 3.5.x
* **Language**: Java 21
* **Security**: Stateless Spring Security via JWT
* **Persistence**: Spring Data JPA & Hibernate
* **Database**: MySQL 8.x
* **Cache**: Spring Data Redis
* **Messaging**: Spring Kafka
* **Scheduler**: Spring Boot Scheduler (handles active reminder checking)

---

## 🏃 Running the Application

Follow these steps to build, test, and run the backend locally:

### 1. Compile Code
```bash
mvn clean compile
```

### 2. Run Test Suite
```bash
mvn test
```

### 3. Start Development Server
```bash
mvn spring-boot:run
```
The server will start on **`http://localhost:8080`**.

---

## 🌐 Frontend Integration & API Documentation

For the frontend team, a detailed API Integration and CORS setup guide is available inside the project at:
* **[docs/frontend-integration.md](file:///d:/BridgeLabz/MagicSoftware/fundoo/docs/frontend-integration.md)**

This document includes:
* Detailed endpoint tables for User, Notes, Labels, Collaborators, and Reminders.
* Required JSON DTO structures and field validation limits.
* Setup instructions for token authorization headers (`Authorization: Bearer <JWT>`).
* CORS whitelist information.
