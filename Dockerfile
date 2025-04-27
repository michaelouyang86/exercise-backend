FROM openjdk:21
WORKDIR /app
COPY build/libs/exercise-1.0.jar exercise.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar"]
CMD ["exercise.jar"]