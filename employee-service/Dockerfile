# syntax=docker/dockerfile:1

# ==========================================
# Stage 1: Build the Spring Boot application
# ==========================================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

# Copy Maven Wrapper and pom.xml first.
# This improves Docker build-cache usage.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Download dependencies before copying source code.
RUN ./mvnw dependency:go-offline -DskipTests

# Copy application source code.
COPY src/ src/

# Build the executable Spring Boot JAR.
RUN ./mvnw clean package -DskipTests


# ==========================================
# Stage 2: Run the Spring Boot application
# ==========================================
FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]