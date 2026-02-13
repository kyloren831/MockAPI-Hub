# API Sandbox -- Mock API Platform (Spring Boot 4 + PostgreSQL + Docker + OpenAPI)

Backend platform to create and consume simulated REST APIs (mocks)
without depending on a real backend.

It allows:

-   Creating isolated sandboxes
-   Defining endpoints by HTTP method and path
-   Configuring multiple responses per endpoint
-   Enforcing ONLY one enabled response per endpoint
-   Logging all incoming requests with response timing metrics

------------------------------------------------------------------------

## 🧰 Tech Stack

Based on the actual project dependencies:

-   Java 17
-   Spring Boot 4.0.2
-   Spring Web MVC
-   Spring Data JPA (Hibernate 7)
-   PostgreSQL
-   Hypersistence Utils (JSONB support)
-   Lombok
-   Spring Validation
-   Spring Boot Actuator
-   SpringDoc OpenAPI (Swagger UI)
-   Docker / Docker Compose

------------------------------------------------------------------------

## 🚀 Run the Project

``` bash
git clone https://github.com/kyloren831/ApiSandbox.git
cd apisandbox

docker compose up --build
```

------------------------------------------------------------------------

## 🌐 Important URLs

-   API: http://localhost:8080
-   Swagger UI: http://localhost:8080/swagger-ui/index.html
-   Health Check: http://localhost:8080/actuator/health

------------------------------------------------------------------------

# 📦 Functional Model

## 1️⃣ Create Sandbox

POST /api/sandboxes

``` json
{
  "name": "Frontend QA",
  "slug": "frontend-qa"
}
```

------------------------------------------------------------------------

## 2️⃣ Create Mock Endpoint

POST /api/mock-endpoints

``` json
{
  "sandboxId": "4c6cfda5-962e-49eb-b96a-a701d07dcf59",
  "method": "POST",
  "path": "/users",
  "enabled": true,
  "description": "First mock endpoint"
}
```

An endpoint can be enabled or disabled. If disabled, the gateway will
not resolve it.

------------------------------------------------------------------------

## 3️⃣ Create Mock Response

POST /api/mock-responses

``` json
{
  "endpointId": "1e84b42b-d1b6-4433-bf0e-f5c9081613d9",
  "statusCode": 201,
  "body": {
    "message": "Simulated response",
    "success": true,
    "data": {
      "id": 123,
      "name": "Demo product"
    }
  },
  "contentType": "application/json",
  "delayMs": 500,
  "enabled": true
}
```

🔒 Important Rule:

-   An endpoint can have multiple responses.
-   ONLY one response can have enabled = true.
-   If a new response is enabled, the previous one is automatically
    disabled.
-   The gateway always returns the enabled response.

------------------------------------------------------------------------

## 4️⃣ Consume the Mock (Gateway)

Example:

    POST /mock/frontend-qa/users

Gateway flow:

1.  Resolve sandbox by slug.
2.  Resolve endpoint by HTTP method + path.
3.  Fetch the enabled response.
4.  Apply configured artificial delay.
5.  Return status + body + content-type.
6.  Log the request in request_logs.

------------------------------------------------------------------------

# 🧾 Request Logs

Each request stores:

-   endpoint reference
-   timestamp
-   method
-   path
-   query string
-   serialized headers
-   request body
-   response status
-   response_time_ms

Enables debugging and traffic analysis.

------------------------------------------------------------------------

# 🐳 Docker Stack

Services:

-   postgres:18
-   app (Spring Boot)

Start:

``` bash
docker compose up --build
```

Reset database:

``` bash
docker compose down -v
```

------------------------------------------------------------------------

# 🏗 Architecture

-   Administrative Controllers (CRUD)
-   MockGatewayController (dynamic resolution for any HTTP method)
-   Service layer with business rules
-   JPA Repositories
-   JSONB support for dynamic response bodies

The gateway supports dynamic path resolution including:

    /orders/{id}

------------------------------------------------------------------------

# 🎯 Project Status

MVP Features:

✔ Sandbox management\
✔ Endpoint management\
✔ Multiple responses per endpoint\
✔ Single enabled response rule\
✔ Dynamic gateway\
✔ Request logging\
✔ Swagger documentation\
✔ Fully dockerized

------------------------------------------------------------------------

# 👨‍💻 Author

Built as a community-focused mock API platform for frontend development
and QA testing workflows.
