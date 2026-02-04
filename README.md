# RentMyThing App Server

RentMyThing is a backend service that allows users to list, search, and rent items from others.  
This repository contains the Spring Boot server implementation, packaged as a runnable JAR and containerized with Docker for deployment.

---

## 🚀 Features
- Role-based authentication and authorization (JWT-based, stateless).
- User profile management with location auto-capture.
- Item listing, search, and owner info preloading.
- Account deletion and secure token handling.
- Unified API response format for predictable client integration.
- Dockerized environment with MySQL, Redis, and Gmail SMTP support.

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Cache:** Redis
- **Auth:** JWT
- **Build Tool:** Maven
- **Containerization:** Docker, GHCR (GitHub Container Registry)

---

## 📦 Build Instructions

### Build JAR ```bash
mvn clean package -DskipTests
The compiled JAR will be located in:

Code
target/rentmything-<version>.jar
Run JAR
bash
java -jar target/rentmything-0.0.1-SNAPSHOT.jar
🐳 Docker Setup
Build Image
bash
docker build -t rentmything-app .
Tag & Push to GHCR
bash
docker tag rentmything-app ghcr.io/goluksharma/rentmything-app-server-new-version:v1.0.0
docker push ghcr.io/goluksharma/rentmything-app-server-new-version:v1.0.0


# RentMyThing API Guide

This backend service provides endpoints for user registration, authentication, item management, and search.  
All endpoints are prefixed with:
Registration Flow
Send OTP → POST /sendotp  
Body: { "email": "user@example.com" }

Verify OTP → POST /verifyotp  
Body: { "email": "user@example.com", "otp": "123456" }

Register User → POST /register  
Body: { "name": "John", "email": "user@example.com", "password": "pass", "role": "OWNER" }

Login → POST /login  
Body: { "email": "user@example.com", "password": "pass" }  
Returns JWT token + user details.

Location
Save Location → POST /save-location  
Body: { "email": "user@example.com", "latitude": 25.123, "longitude": 85.456 }
Categories
Get Categories → GET /categories

Items
Add Item → POST /add-service  
Body: { "title": "Laptop", "description": "Dell Inspiron", "price": 500, "ownerEmail": "owner@example.com" }

Delete Item → DELETE /deleteItem  
Body: { "title": "Laptop", "email": "owner@example.com" }
Owner Profile
Fetch Owner Profile → POST /fetching  
Body: { "email": "owner@example.com", "role": "OWNER" }

Search
Search Users by Location → POST /serch  
Body: { "latitude": 25.123, "longitude": 85.456, "radius": 10 }

Account Management
Delete Account → DELETE /delete-account  
Body: { "email": "user@example.com" }

Response Format
Every endpoint returns a unified ApiResponse:

json
{
  "success": true,
  "code": "LOGIN_SUCCESS",
  "message": "Login Successful",
  "data": { ...optional payload... }
}

