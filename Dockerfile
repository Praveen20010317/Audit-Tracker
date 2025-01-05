# Use an official OpenJDK runtime as a parent image
FROM maven:3.9.9-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine
COPY --from=build/target/*.jar AuditTracker.jar

EXPOSE 8080

# Run the 'AuditTracker.jar' file
ENTRYPOINT ["java", "-jar", "AuditTracker.jar"]

