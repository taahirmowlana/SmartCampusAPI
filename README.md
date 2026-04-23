# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W Client-Server Architectures  
**Student Name:** Taahir Mowlna  
**Student ID:** 20242154 / w2153133  

## Project Overview
The Smart Campus API is a robust, high-performance RESTful web service designed to serve as the backend infrastructure for a university "Smart Campus" initiative. It acts as the centralized system for tracking physical facilities (Rooms) and monitoring the diverse array of hardware components (Sensors) deployed within them, such as CO2 monitors and occupancy trackers. 

This project is built strictly using the JAX-RS (Jakarta RESTful Web Services) specification. To ensure high performance and strict adherence to coursework constraints (no external databases like SQL Server), the system utilizes a custom, thread-safe, in-memory singleton data store leveraging `ConcurrentHashMap` and `CopyOnWriteArrayList` to handle concurrent HTTP requests safely. The architecture features sub-resource locators for historical data routing, and a comprehensive global exception mapping system to ensure strict data integrity and secure HTTP error handling (409 Conflict, 422 Unprocessable Entity, 403 Forbidden).

## Technology Stack
* **Language:** Java
* **Build Tool:** Maven
* **Framework:** JAX-RS / Jersey
* **Deployment Target:** Embedded Grizzly HTTP Server
* **Data Storage:** In-memory collections only (`ConcurrentHashMap`)

## Base URL
`http://localhost:8081/api/v1`

## How To Build And Run
1. Discovery Endpoint (HATEOAS)
curl -X GET http://localhost:8081/api/v1/
1. **Clone the repository:**
   ```bash
   git clone https://github.com/taahirmowlana/SmartCampusAPI.git
