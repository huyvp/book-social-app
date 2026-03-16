# 🚀 Book Social App - Microservices Architecture

This project is a **microservices-based social networking system** built using Spring Boot technologies and React.
It includes an API Gateway for centralized routing, and several specialized services handling identity, profiles, posts, chats, files, and notifications.

---

## 📦 **All Services Included**

---

### **1. API Gateway**
**Port: 9000**
**Role:**
* Acts as the **single entry point** for all external requests
* Handles **routing**, **request filtering**, and **load balancing**
* Protects internal services from direct external access

**Tech Stack:**
* **Spring Cloud Gateway**

---

### **2. Identity Service**
**Port: 9001**
**Role:**
* Manages **users, passwords, permissions**, and **authentication**
* Issues **JWT tokens** to clients
* Produces Kafka events for other services

**Tech Stack:**
* **Spring Boot**
* **Spring Security**
* **PostgreSQL Database**
* **Apache Kafka (Producer)**

---

### **3. Profile Service**
**Port: 9002**
**Role:**
* Manages **user profile information** after account creation
* Stores relationships between users (e.g., following/followers)

**Tech Stack:**
* **Spring Boot**
* **Neo4j Graph Database**

---

### **4. Notification Service**
**Port: 8082**
**Role:**
* Consumes events via Kafka to send notifications/emails

**Tech Stack:**
* **Spring Boot**
* **MongoDB**
* **Apache Kafka (Consumer)**
* **Brevo Email API**

---

### **5. Post Service**
**Port: 8083**
**Role:**
* Handles creation, retrieval, and management of user posts

**Tech Stack:**
* **Spring Boot**
* **MongoDB**

---

### **6. File Service**
**Port: 8084**
**Role:**
* Manages file uploads (images, attachments) and provides download links

**Tech Stack:**
* **Spring Boot**
* **MongoDB**

---

### **7. Chat Service**
**Port: 8085**
**Role:**
* Manages real-time messages and conversation history between users

**Tech Stack:**
* **Spring Boot**
* **MongoDB**

---

### **8. Web App (Frontend)**
**Role:**
* The client-side application that users interact with.

**Tech Stack:**
* **React**
* **Material-UI (MUI)**
* **React Router**

---

## 🔄 **System Architecture Overview**

```mermaid
graph TD
    %% Define Nodes
    WA["Web App (React)"]
    AG["API Gateway<br/>(Port: 9000)"]
    
    IS["Identity Service<br/>(Port: 9001)"]
    PS["Profile Service<br/>(Port: 9002)"]
    PoS["Post Service<br/>(Port: 8083)"]
    CS["Chat Service<br/>(Port: 8085)"]
    FS["File Service<br/>(Port: 8084)"]
    NS["Notification Service<br/>(Port: 8082)"]

    PG[("PostgreSQL")]
    N4J[("Neo4j")]
    K{"Kafka Message Broker"}
    
    %% Shared/Isolated Mongo DBs represented visually
    M_P[("MongoDB")]
    M_C[("MongoDB")]
    M_F[("MongoDB / Local Storage")]
    M_N[("MongoDB / Email API")]

    %% Connections
    WA --> AG
    
    AG --> IS
    AG --> PS
    AG --> PoS
    AG --> CS
    AG --> FS

    IS --> PG
    PS --> N4J
    PoS --> M_P
    CS --> M_C
    FS --> M_F

    %% Kafka Events
    IS -- Events --> K
    K -- Consumes --> NS
    NS --> M_N

    %% Styling
    classDef client fill:#d4edda,stroke:#28a745,stroke-width:2px;
    classDef gateway fill:#cce5ff,stroke:#004085,stroke-width:2px;
    classDef service fill:#fff3cd,stroke:#856404,stroke-width:2px;
    classDef db fill:#f8d7da,stroke:#721c24,stroke-width:2px;
    classDef broker fill:#e2e3e5,stroke:#383d41,stroke-width:2px;

    class WA client;
    class AG gateway;
    class IS,PS,PoS,CS,FS,NS service;
    class PG,N4J,M_P,M_C,M_F,M_N db;
    class K broker;
```

---

## 🧰 **Common Technical Features**

* Microservices architecture
* JWT-based authentication
* Distributed communication (Kafka, REST/HTTP)
* Centralized API routing structure

---

## 📁 **Folder Structure**

```text
/api-gateway          # Spring Cloud routing and filters
/identity-service     # Auth, users, PostgreSQL
/profile-service      # Graph relationships, Neo4j
/notification-service # Emails, Kafka consumer, MongoDB
/post-service         # Posts, MongoDB
/chat-service         # Chat, MongoDB
/file-service         # Static assets, MongoDB
/web-app              # React frontend workspace
docker-compose.yml    # Infrastructure definitions
README.md             # This technical document
```

---

## 📌 **Future Improvements**

* Add service discovery (Eureka/Consul)
* Add centralized config system
* Add monitoring (Prometheus + Grafana)
* Add tracing (Zipkin)
