# 🚀 Smart Campus Sensor & Room Management API

**Module:** 5COSC022W Client-Server Architectures  
**Student:** Taahir Mowlna  
**Student ID:** 20242154 / w2153133  

---

## 📌 Overview

The **Smart Campus API** is a high-performance RESTful backend designed to support a university smart campus system.

It provides a centralized platform to:

- Manage **rooms and facilities**
- Monitor **sensors** (CO₂, occupancy, etc.)
- Store and retrieve **sensor readings**

The system is built strictly using **JAX-RS (Jakarta RESTful Web Services)** and uses a **custom in-memory datastore** to meet coursework constraints (no external databases).

---

## ⚙️ Key Features

- RESTful API design with proper HTTP semantics  
- Thread-safe in-memory data handling  
- Sub-resource architecture for nested endpoints  
- Advanced error handling with proper HTTP status codes  
- HATEOAS-enabled discovery endpoint  
- Centralized logging via JAX-RS filters  

---

## 🧱 Tech Stack

- **Language:** Java  
- **Build Tool:** Maven  
- **Framework:** JAX-RS (Jersey)  
- **Server:** Embedded Grizzly HTTP Server  
- **Data Storage:**  
  - `ConcurrentHashMap`  
  - `CopyOnWriteArrayList`  

---

## 🌐 Base URL

http://localhost:8081/api/v1

---

## ▶️ Getting Started

### 1. Clone the Repository
git clone https://github.com/taahirmowlana/SmartCampusAPI.git

### 2. Open in IDE
Use Apache NetBeans or any Maven-supported Java IDE.

### 3. Build the Project
- Right-click project → Clean and Build
- Maven will download dependencies automatically

### 4. Run the Server
Navigate to:
com.smartcampus.config.ApiLauncher.java  
Right-click → Run File

### 5. Confirm Startup
Look for this message in the console:
Jarvis Systems Online

---

## 🧪 API Usage (cURL Examples)

### Discovery Endpoint
curl -X GET http://localhost:8081/api/v1/

### Create a Room
curl -X POST http://localhost:8081/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"name\": \"Tony Stark's Workshop\", \"capacity\": 10}"

### Register a Sensor
curl -X POST http://localhost:8081/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"type\": \"CO2\", \"status\": \"ACTIVE\", \"roomId\": \"ROOM-ID\"}"

### Add Sensor Reading
curl -X POST http://localhost:8081/api/v1/sensors/SENS-ID/readings \
-H "Content-Type: application/json" \
-d "{\"value\": 415.5}"

### Filter Sensors
curl -X GET http://localhost:8081/api/v1/sensors?type=CO2

---

## 🧠 Architecture Highlights

- Per-request lifecycle in JAX-RS  
- Thread-safe static data structures  
- HATEOAS for dynamic API navigation  
- Sub-resource locators for clean routing  
- Centralized logging using filters  

---

## ⚠️ Error Handling

- 400 – Bad Request  
- 403 – Forbidden  
- 404 – Not Found  
- 409 – Conflict  
- 415 – Unsupported Media Type  
- 422 – Unprocessable Entity  

---

## 🔐 Security Notes

Avoid exposing stack traces as they reveal internal system details that could be exploited.

---

## 📌 Summary

This project demonstrates strong RESTful API design, scalable architecture, and clean backend engineering practices using Java and JAX-RS.
