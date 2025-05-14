# To build the Docker image, run the following command in the terminal:
# docker build -t exercise-backend:latest .

# 1. Build the app

# Use openjdk as the base image for building the app
FROM openjdk:21-slim AS builder

# Set the working directory
WORKDIR /app

# Copy gradlew and gradle files
COPY gradlew build.gradle settings.gradle ./

# Copy the gradle wrapper
COPY gradle ./gradle

# Copy the source code
COPY src ./src

# Build the app, this will create a jar file in the build/libs directory
RUN ./gradlew test build --no-daemon

# Stage 2: Runtime
FROM openjdk:21-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file from the builder stage to the runtime stage
COPY --from=builder /app/build/libs/exercise-latest.jar exercise.jar

# Expose port for the application
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "exercise.jar"]