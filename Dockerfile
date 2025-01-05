# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the 'AuditTracker.jar' from the local 'target' directory to the '/app' directory in the container
COPY target/AuditTracker.jar /app/AuditTracker.jar

# Expose the port that the app will use (Render uses 10000 by default)
EXPOSE 10000

# Set the environment variable for the port
ENV PORT=10000

# Run the 'AuditTracker.jar' file
ENTRYPOINT ["java", "-jar", "/app/AuditTracker.jar"]
