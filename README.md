#Feature Flag Implementation with Unleash

## Overview:
 - The project builds upon the e-commerce microservices system that was developed in Assignment one by deploying feature flags with Unleash as the feature flag management server. The system has three feature flags and two microservices Product service and Order Service, which manage different features.

# setup requirements:
- Java JDK21
- Docker and Docker Compose
- Maven
- Git

### Services
- PostgreSQL 
    Port - 5432
    Description - Database For Unleash
- Unleash Server
    Port - 4242
    Description - Feature flag management ui
- Product Service 
    Port - 8081
    Description - Product microservice with premium pricing flag
- Order Service
    Port - 8082
    Description - Order microservice with bulk discount and notification flags

### Feature Flags
- premium-pricing
    Service - Product Service
    Description - When flag enabled, it applies 10% discount to all product
- bulk-order-discount
    Service - Order Service
    Description - When flag is enabled, it applies 15% discount for more than 5 items.
- order-notifications
    Service - Order Service
    Description - When flag is enabled, logs order confirmation notifications
- 
# Running Application
1. Clone the repository
2. Start all service using
    docker-compose up --build
3. Initialize feature flags
    navigate the command in git bash
    chmod +x scripts/init-flags.sh
    ./scripts/init-flags.sh
4. Verify all services are running
    docker-compose ps
5. Unleash Admin UI
    URL: http://localhost:4242
    Username: admin
    Password: unleash4all
6. you can use this ui to toggle feature flags, and you can see real-time behavior changes.
7. Run unit tests locally
    For product Service
    cd product-service
    ./mvnw clean test
    
    For Order service
    cd ../order-service OR cd order-service
    ./mvnw clean test

- The project includes a GitHUb Actions pipeline

GitHub repository Link:- https://github.com/YatriPatel17/FeatureFlag_Group9
   