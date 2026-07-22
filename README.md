# Employee Task Microservices

A production-style microservices learning project built incrementally 
using
Spring Boot and MySQL.

## Services

- Employee Service
- Task Service
- Notification Service
- Report Service

## Initial Communication

- Task Service validates employees through synchronous REST.
- Task Service sends notifications through synchronous REST.
- Reporting initially uses REST-based data retrieval.

## Planned Evolution

- API Gateway
- Eureka Service Discovery
- Resilience patterns
- Redis caching
- Kafka or RabbitMQ
- Distributed tracing
- Centralized configuration

## Service Ports

| Service | Port |
|---|---:|
| Employee Service | 8081 |
| Task Service | 8082 |
| Notification Service | 8083 |
| Report Service | 8084 |

## Architecture Rules

- Each service owns its database.
- Services never directly access another service's database.
- JPA entities are not shared between services.
- Service URLs remain configurable.
- Infrastructure components are introduced only after basic communication 
works.
