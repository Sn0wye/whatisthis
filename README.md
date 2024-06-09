# What is This?

This repository serves as a learning project to create and deploy multiple microservices using different technologies and languages, with a focus on enabling communication between them.

## Services

| Service Name   | Description                                       | Language | Framework   | Ports                     |
| -------------- | ------------------------------------------------- | -------- | ----------- | ------------------------- |
| Authentication | Manages user authentication and authorization.    | Go       | Gin         | 8080 (HTTP), 50050 (gRPC) |
| Scorer         | Calculates user scores based on certain criteria. | Java     | Spring Boot | 8081                      |

## Infrastructure

- **Docker**: Containerization platform
- **Docker Compose**: Orchestration tool for managing multi-container Docker applications
- **PostgreSQL**: Database for storing application data
- **RabbitMQ**: Message broker for asynchronous communication between microservices
