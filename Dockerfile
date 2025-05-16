# To build the Docker image, use the following command:
# docker build -t exercise-backend:latest .

# To run the Docker container, use the following command:
# Replace ${JWT_SECRET} with your actual JWT secret
# Replace ${DB_HOST}, ${DB_USERNAME}, and ${DB_PASSWORD} with your actual database credentials
# docker run -p 8080:8080 -e JWT_SECRET="${JWT_SECRET}" -e DB_HOST="${DB_HOST}" -e DB_USERNAME="${DB_USERNAME}" -e DB_PASSWORD="${DB_PASSWORD}" exercise-backend:latest

# 1. Build the app

# Use openjdk as the base image for building the app
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy gradlew and gradle files
COPY gradlew build.gradle settings.gradle ./

# Make gradlew executable
RUN chmod +x gradlew

# Copy the gradle wrapper
COPY gradle ./gradle

# Copy the source code
COPY src ./src

# Build the app, this will create a jar file in the build/libs directory
RUN ./gradlew build --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file from the builder stage to the runtime stage
COPY --from=builder /app/build/libs/exercise-latest.jar exercise.jar

# Expose port for the application
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "exercise.jar"]