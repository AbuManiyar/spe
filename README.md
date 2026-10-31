# Calculator REST API CI/CD Project

This project demonstrates an end-to-end CI/CD pipeline for a Spring Boot calculator REST API using Maven, Jenkins, Docker, Docker Compose, and Nginx.

## Overview

The application is packaged as a Docker image and deployed as a small Compose stack with:

- 2 API replicas: `calculator-api-1` and `calculator-api-2`
- 1 Nginx reverse proxy
- Shared Docker network: `calculator-net`
- Compose project name: `calculator`

## Tech Stack

- Java 17 / Spring Boot
- Maven
- Jenkins
- Docker
- Docker Compose
- Nginx

## Project Structure

```text
.
├── Dockerfile
├── Jenkinsfile
├── docker-compose.yml
├── nginx.conf
├── pom.xml
├── mvnw
├── src/
├── target/
└── README.md
```

## Prerequisites

Before running the project, ensure you have:

- Java and Maven installed
- Docker installed and running
- Docker Compose installed
- Jenkins configured with a Docker Hub credential named `dockerhub-creds`

## Build and Test Locally

```bash
chmod +x mvnw
./mvnw clean package
./mvnw test
```

## Docker Build

```bash
docker build -t calculator-api:v1 .
```

## Run with Docker Compose

```bash
docker compose -p calculator down --remove-orphans || true
docker compose -p calculator pull
docker compose -p calculator up -d --remove-orphans
```

The app is exposed through Nginx on:

```text
http://localhost:8083
```

The actuator health endpoint is checked at:

```text
http://localhost:8083/actuator/health
```

## Jenkins Pipeline

The Jenkins pipeline includes:

1. Checkout
2. Build
3. Test
4. Docker Build
5. Docker Push
6. Deploy

The deploy stage:

- uses `docker compose -p calculator`
- pulls the latest image
- redeploys the stack
- waits for the API health endpoint
- prints Compose status and logs on failure

## Notes

- Fixed `container_name` entries were removed to avoid naming conflicts.
- Compose manages container naming automatically when using the consistent project name `calculator`.
- The Nginx upstream services remain configured to use the Compose service names:
  - `calculator-api-1:8080`
  - `calculator-api-2:8080`

## Useful Commands

```bash
# View running services
docker compose -p calculator ps

# View logs
docker compose -p calculator logs --tail=100

# Stop the stack
docker compose -p calculator down --remove-orphans
```

## Git Workflow

```bash
git add .
git commit -m "Add CI/CD setup and deployment config"
git push origin <branch-name>
```
