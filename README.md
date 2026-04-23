# Smart Campus Sensor & Room Management API

## Overview
This is a robust, highly available RESTful API developed for the university's "Smart Campus" initiative. Built entirely with Java and JAX-RS (Jersey), the API provides a seamless interface for campus facilities managers to track Rooms and monitor deep-nested IoT Sensor hardware (e.g., CO2 monitors, occupancy trackers). 

The architecture features an in-memory thread-safe data vault (`ConcurrentHashMap`), sub-resource locators for historical data routing, and a comprehensive global exception mapping system to ensure strict data integrity and secure HTTP error handling (409 Conflict, 422 Unprocessable Entity, 403 Forbidden).

## Build & Launch Instructions
1. **Clone the repository:**
   `git clone https://github.com/taahirmowlana/SmartCampusAPI.git`
2. **Open the project** in Apache NetBeans IDE (or any Java IDE supporting Maven).
3. **Resolve Dependencies:** Right-click the project and select **Clean and Build** to allow Maven to download the Jersey/Grizzly dependencies.
4. **Start the Server:** Navigate to `src/main/java/com/smartcampus/ApiLauncher.java`, right-click, and select **Run File**.
5. The API will launch on `http://localhost:8081/api/v1/`.

## API Interactions (cURL Examples)

**1. Discovery Endpoint (HATEOAS)**
`curl -X GET http://localhost:8081/api/v1/`

**2. Create a New Room**
`curl -X POST http://localhost:8081/api/v1/rooms -H "Content-Type: application/json" -d "{\"name\": \"Advanced Robotics Lab\", \"capacity\": 15}"`

**3. Register a New Sensor**
*(Replace ROOM-ID with the ID generated from the previous step)*
`curl -X POST http://localhost:8081/api/v1/sensors -H "Content-Type: application/json" -d "{\"type\": \"CO2\", \"status\": \"ACTIVE\", \"roomId\": \"ROOM-ID\"}"`

**4. Log a New Sensor Reading (Sub-Resource)**
*(Replace SENS-ID with the ID from the previous step)*
`curl -X POST http://localhost:8081/api/v1/sensors/SENS-ID/readings -H "Content-Type: application/json" -d "{\"value\": 415.5}"`

**5. Filter Sensors by Type**
`curl -X GET http://localhost:8081/api/v1/sensors?type=CO2`

## Conceptual Report
**(Add your answers to the PDF report questions here)**# SmartCampusAPI
