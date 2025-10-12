# Base image with Java 17
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the built JAR file into container
COPY target/auth-service.jar auth-service.jar

# Expose the port your Spring Boot app runs on
EXPOSE 4000

# Start the application
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
