FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew
COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM openjdk:17-jdk-slim-buster
RUN apt-get update && apt-get install -y redis-server
WORKDIR /app
COPY firebase-adminsdk.json /app/firebase-adminsdk.json
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "-c", "redis-server --daemonize yes && java -jar /app/app.jar"]