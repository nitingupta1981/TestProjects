# Multi-stage build for Spring Boot application
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/src ./backend/src

# Build the application
RUN mvn clean package -DskipTests -f pom.xml

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Cloud Run sets PORT environment variable, default to 8080
ENV PORT=8080

# Expose port
EXPOSE ${PORT}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

