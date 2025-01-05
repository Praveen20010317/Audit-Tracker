# Stage 1: Build the application using Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Copy the entire project into the container
COPY . .

# Run Maven build to package the application (skipping tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Set up the runtime environment with a smaller base image
FROM eclipse-temurin:17-alpine

# Copy the JAR file from the build stage to the runtime stage
# Ensure the target path and filename are correct
COPY --from=build /target/*.jar /app/AuditTracker.jar

# Expose port 8080 (ensure your application uses this port)
EXPOSE 8080

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app/AuditTracker.jar"]


