# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy only the Maven descriptor first so dependency resolution can be cached.
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the source and build the executable Spring Boot JAR.
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user for secure runtime execution.
RUN groupadd --system spring && useradd --system --gid spring --home-dir /app --create-home spring

# Copy only the built executable JAR into the runtime image.
COPY --from=build /workspace/target/calculator-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application port in the container.
EXPOSE 8080

# Configure the app to listen on port 8080.
ENV SERVER_PORT=8080

# Run as a non-root user.
USER spring:spring

# Launch the Spring Boot JAR.
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=8080"]
