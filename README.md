# E-commerce Microservice based on CI.CD Pipeline

## Overview:
 - The project will use a simple e-commerce based on spring boot microservice, which will containerized with Docker Compose and automatized with a CI/CD pipeline with GitHub Actions.

# setup requirements:
- Java JDK21
- Docker and Docker Compose
- Git

# Running Application
- Build and start using docker compose: docker-compose up --build
- Access:
    - Product API: http://localhost:8081/api/products
    - Order API: http://localhost:8082/api/orders

# CI-CD:
- We automatically create, test and deploy our microservices through the GitHub Actions CI/CD pipeline.It generates Docker images, performs integration testing using Docker Compose and run uint tests on each main push. The pipeline validates service communication before it is deployed and ensure the quality of code.
