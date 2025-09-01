FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace
COPY gradlew ./
COPY gradle gradle
RUN chmod +x ./gradlew
COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
