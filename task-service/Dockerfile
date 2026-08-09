# syntax=docker/dockerfile:1

# Build stage

FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -DskipTests

COPY src/ src/

RUN ./mvnw clean package -DskipTests && \
JAR_FILE=$(find target -maxdepth 1 -type f -name "*.jar" ! -name "*.original" | head -n 1) && \
cp "$JAR_FILE" app.jar

# Runtime stage

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /workspace/app.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
