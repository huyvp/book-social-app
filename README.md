# 🚀 Project Description

This project is a **microservices-based system** built using Spring technologies.
It includes an API Gateway for centralized routing, an Identity Service for authentication & authorization, and a Profile Service for user profile management.

---

## 📦 **All Services Included**

---

### ### **1. API Gateway**

**Port: 9000**

**Role:**

* Acts as the **single entry point** for all external requests
* Handles **routing**, **request filtering**, and **load balancing**
* Protects internal services from direct external access

**Tech Stack:**

* **Spring Cloud Gateway**
* **Spring Security** (optional: JWT filter)

**Core Responsibilities:**

* Centralized authentication filter
* Rate limiting / throttling
* Logging & tracing (Zipkin/ELK)
* Service discovery integration (Eureka/Consul)

---

### ### **2. Identity Service**
**Port: 9001**

**Role:**

* Manages **users, passwords, permissions**, and **authentication**
* Issues **JWT tokens** to clients
* Stores credential-related data

**Tech Stack:**

* **Spring Boot**
* **Spring Security**
* **MSSQL Database**

**Core Responsibilities:**

* User registration & login
* Password hashing (BCrypt)
* Token generation & validation
* Role-based access control (RBAC)

---

### ### **3. Profile Service**

**Role:**

* Manages **user profile information** after account creation
* Stores relationships between users
* Provides profile update and retrieval APIs

**Tech Stack:**

* **Spring Boot**
* **Neo4j Graph Database**

**Core Responsibilities:**

* Create profile after new user registration
* Manage relationships (friends, organizations, roles…)
* Query optimized for graph structures

---

## 🔄 **System Architecture Overview**

```
                ┌─────────────────────┐
                │     Client App      │
                └─────────┬───────────┘
                          │
                          ▼
                ┌─────────────────────┐
                │    API Gateway      │
                └───┬────────┬────────┘
                    │        │
         ┌──────────┘        └──────────┐
         ▼                               ▼
┌─────────────────┐            ┌─────────────────┐
│ Identity Service │            │ Profile Service │
└─────────────────┘            └─────────────────┘
          │                               │
          ▼                               ▼
   ┌───────────────┐               ┌───────────────┐
   │    MSSQL DB    │               │  Neo4j Graph  │
   └───────────────┘               └───────────────┘
```

---

## 🧰 **Common Technical Features**

* Microservices architecture
* Distributed configuration (Spring Cloud Config)
* Centralized logging
* JWT-based authentication
* Docker-compose deployment (optional)

---

## 📁 Folder Structure (suggested)

```
/api-gateway
/identity-service
/profile-service
/shared-libs
docker-compose.yml
README.md
```

---

## 📌 Future Improvements

* Add service discovery (Eureka/Consul)
* Add centralized config system
* Add monitoring (Prometheus + Grafana)
* Add tracing (Zipkin)

---

Nếu bạn muốn, mình có thể **tạo luôn file README.md hoàn chỉnh** dưới dạng tải về hoặc generate thêm phần **API endpoints**, **environment variables**, hoặc **docker-compose**.
