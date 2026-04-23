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

## Conceptual Report: Smart Campus API

### Part 1: Service Architecture & Setup
**Q: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures.**
By default, JAX-RS treats resource classes as per-request. A brand new instance of the resource class is instantiated for every single incoming HTTP request, and it is destroyed after the response is sent. Because of this, we cannot store state in standard instance variables (like a standard `HashMap` or `ArrayList`), as the data would be wiped on the next request. To prevent data loss and race conditions across multiple simultaneous requests, our in-memory data structures must be declared as `static` and we must use thread-safe implementations, such as `ConcurrentHashMap`, to ensure atomic operations and safe concurrent access.

**Q: Why is the provision of "Hypermedia" (HATEOAS) considered a hallmark of advanced RESTful design? How does this approach benefit client developers?**
HATEOAS (Hypermedia as the Engine of Application State) transforms an API from a static directory into a dynamic, self-navigating system. By including navigation links within the JSON responses, client developers do not need to hardcode URLs or constantly refer to static, out-of-date documentation. The client can dynamically discover available endpoints and actions based on the current state of the resource, making the overall system much more resilient to future URL routing changes.

### Part 2: Room Management
**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.**
Returning only IDs minimizes network bandwidth payload, resulting in faster initial transmission. However, it forces the client to make subsequent "N+1" API calls to fetch the details for each ID, heavily increasing latency and server load. Returning the full room objects consumes more bandwidth upfront but allows the client to render the UI immediately with a single request, which is generally preferred for dashboard applications unless the dataset is massive (where pagination should be used).

**Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification.**
Yes, the DELETE operation is idempotent. If a client mistakenly sends the exact same DELETE request for a room multiple times, the first request will successfully delete the room and return a `204 No Content` (or `200 OK`). Any subsequent DELETE requests for that exact same ID will simply return a `404 Not Found` because the resource no longer exists. Regardless of how many times the request is sent, the final state of the server remains exactly the same: the room is gone.

### Part 3: Sensor Operations & Linking
**Q: Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml, when @Consumes(MediaType.APPLICATION_JSON) is used. How does JAX-RS handle this mismatch?**
If a client ignores the `@Consumes` annotation and sends an unsupported media type (like `text/plain`), JAX-RS intercepts the request before it even reaches the resource method. The framework automatically handles the mismatch by rejecting the request and returning a standard HTTP `415 Unsupported Media Type` error to the client, protecting the backend logic from parsing failures.

**Q: Contrast using a @QueryParam for filtering (e.g., ?type=CO2) with making the type part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach considered superior?**
Path parameters are designed to identify a specific, unique resource or hierarchy (e.g., "Sensor 5 inside Room A"). Query parameters are designed to sort, filter, or modify a broader collection. Using a query parameter (`?type=CO2`) is superior because filtering is an optional modifier, not a structural identity. It allows for combining multiple filters easily (`?type=CO2&status=ACTIVE`) without creating deeply nested, rigid, and unmaintainable URL path structures.

### Part 4: Deep Nesting with Sub-Resources
**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern.**
The Sub-Resource Locator pattern allows us to delegate the handling of nested routes to entirely separate classes. Instead of writing a massive, monolithic controller class that handles every single sensor operation and all reading histories, we hand off the `/{sensorId}/readings` path to a dedicated `SensorReadingResource`. This enforces the Single Responsibility Principle, keeps files small and maintainable, and makes the codebase vastly easier to read, test, and scale.

### Part 5: Advanced Error Handling & Logging
**Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**
A `404 Not Found` implies that the target URL endpoint itself does not exist. A `422 Unprocessable Entity` is much more precise: it tells the client that the URL is correct, and the JSON syntax is perfectly valid, but the *business logic* inside the payload is flawed (e.g., trying to link a sensor to a `roomId` that doesn't exist). It distinguishes a structural error from a data-relationship error.

**Q: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers.**
Exposing raw Java stack traces leaks highly sensitive internal intelligence to potential attackers. A stack trace reveals the specific frameworks being used (e.g., Jersey, Grizzly), exact version numbers, internal file paths, database library names, and the architectural flow of the code. Attackers can cross-reference this information with known CVE databases to exploit specific vulnerabilities in those exact framework versions.

**Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?**
Using JAX-RS filters centralizes the logic. If we manually insert `Logger.info()` into every method, the code becomes cluttered, repetitive, and highly prone to human error (e.g., a developer forgets to log a new endpoint). A `ContainerRequestFilter` and `ContainerResponseFilter` act as an automatic tollbooth, guaranteeing that 100% of incoming and outgoing traffic is logged uniformly without touching the core business logic, adhering to the DRY (Don't Repeat Yourself) principle.**# SmartCampusAPI
