# 빌드 스테이지
FROM gradle:8.1.1-jdk17-alpine AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon

# 런타임 스테이지
FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
