# 🛒 Inventory & Order Management System (Microservices)
A professional, scalable microservices application built with **Spring Boot**, **Java 17**, and **MySQL**. This system provides a complete workflow for user authentication, product management, real-time inventory tracking, and order placement.
---
## 🏗️ Architecture & Technology Stack
### 🚀 Technical Overview
- **Language:** Java 17
- **Framework:** Spring Boot 3.2.3
- **IDE Recommendation:** Spring Tool Suite (STS)
- **Database:** MySQL 8.0 (Distributed Databases)
- **Security:** Spring Security & JWT (Stateless Authentication)
- **Communication:** Synchronous REST API
- **Fault Tolerance:** Resilience4j (Circuit Breaker & Retry)
- **Build Tool:** Maven
### 📂 Service Decomposition
1.  **Auth Service (8080):** Centralized identity management (JWT issuer).
2.  **Product Service (8081):** Product catalog and information management.
3.  **Inventory Service (8082):** Stock tracking and availability verification.
4.  **Order Service (8083):** Order lifecycle management with inter-service stock deduction.
---
## 🚀 Local Setup Guide
### 1️⃣ Prerequisites
- **Java 17** (or higher)
- **Maven**
- **MySQL Server**
- **Spring Tool Suite (STS)** (Recommended IDE)
### 2️⃣ Database Setup
Create these 4 databases in your MySQL server:
```sql
CREATE DATABASE auth_db;
CREATE DATABASE product_db;
CREATE DATABASE inventory_db;
CREATE DATABASE order_db;
```
> [!IMPORTANT]
> **Database Credentials:** 
> Before running the services, open each service's `src/main/resources/application.properties` and update the following lines with your own MySQL username and password:
> ```properties
> spring.datasource.username=YOUR_MYSQL_USERNAME
> spring.datasource.password=YOUR_MYSQL_PASSWORD
> ```
### 3️⃣ Running the Services
Open 4 separate terminals (or use the Boot Dashboard in STS) and execute the following:
```bash
# Terminal 1: Auth Service
cd auth-service && mvn spring-boot:run
# Terminal 2: Product Service
cd product-service && mvn spring-boot:run
# Terminal 3: Inventory Service
cd inventory-service && mvn spring-boot:run
# Terminal 4: Order Service
cd order-service && mvn spring-boot:run
```
---
## 🛡️ Security & Authentication
> [!IMPORTANT]
> **Stateless Security (JWT):** All services except for the **Auth Service** require a JWT Bearer Token in the headers of every request.
> 
> **How to Authenticate:**
> 1. Sign in to get the `token`.
> 2. Add it as `Authorization: Bearer <your_token>` in Postman.
> 3. Some APIs only work with an **ADMIN token**.
---
## 📡 API Documentation (Postman Guide)
### 🔑 1. Auth Service (Port 8080)
*These endpoints are publicly accessible.*
- **Register a New User**
  - **URL:** `POST http://localhost:8080/api/auth/signup`
  - **Body (JSON):** `{"username": "johndoe", "password": "password123"}`
- **Register an Admin**
  - **URL:** `POST http://localhost:8080/api/auth/signup/admin`
  - **Body (JSON):** `{"username": "admin1", "password": "securepassword"}`
- **Sign In**
  - **URL:** `POST http://localhost:8080/api/auth/signin`
  - **Body (JSON):** `{"username": "admin1", "password": "securepassword"}`
  - **Response:** Copies the `token` (this is your **Admin Bearer Token**).
---
### 📦 2. Product Service (Port 8081)
- **Get All Products**
  - **URL:** `GET http://localhost:8081/api/products`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
  - **Access:** Admin & User
- **Create Product (🔒 Admin Only)**
  - **URL:** `POST http://localhost:8081/api/products`
  - **Header:** `Authorization: Bearer <Admin Token>`
  - **Body (JSON):**
    ```json
    {
      "name": "Smart Watch",
      "description": "Latest generation OLED watch",
      "price": 299.99,
      "category": "Electronics"
    }
    ```
- **Update Product (🔒 Admin Only)**
  - **URL:** `PUT http://localhost:8081/api/products/1`
  - **Header:** `Authorization: Bearer <Admin Token>`
- **Delete Product (🔒 Admin Only)**
  - **URL:** `DELETE http://localhost:8081/api/products/1`
  - **Header:** `Authorization: Bearer <Admin Token>`
---
### 🏬 3. Inventory Service (Port 8082)
- **View Current Stock**
  - **URL:** `GET http://localhost:8082/api/inventory/1`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
  - **Access:** Admin & User
- **Add Stock (🔒 Admin Only)**
  - **URL:** `POST http://localhost:8082/api/inventory/add`
  - **Header:** `Authorization: Bearer <Admin Token>`
  - **Body (JSON):** `{"productId": 1, "quantity": 100}`
- **Deduct Stock (Manual)**
  - **URL:** `POST http://localhost:8082/api/inventory/deduct`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
  - **Body (JSON):** `{"productId": 1, "quantity": 10}`
---
### 📑 4. Order Service (Port 8083)
- **Create New Order**
  - **URL:** `POST http://localhost:8083/api/orders`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
  - **Body (JSON):**
    ```json
    {
      "productId": 1,
      "quantity": 2
    }
    ```
- **Get Order Details**
  - **URL:** `GET http://localhost:8083/api/orders/1`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
- **Cancel Order**
  - **URL:** `POST http://localhost:8083/api/orders/1/cancel`
  - **Header:** `Authorization: Bearer <Admin or User Token>`
---
## 🛡️ Resilience & Fault Tolerance
- **Circuit Breaker:** The system uses `Resilience4j` in the **Order Service**. If the **Inventory Service** is offline, the order placement will trigger a fallback method and log a graceful error instead of crashing.
- **Retry Logic:** Automatic retries for transient network failures.
 
