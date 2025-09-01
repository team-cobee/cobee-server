# 빌드 단계: Gradle을 사용하여 애플리케이션 빌드
FROM gradle:8.1.1-jdk17-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 모든 프로젝트 파일 복사
COPY . .

# Gradle 빌드 및 테스트 실행 (테스트 생략)
RUN gradle clean build -x test

FROM openjdk:17-jdk-slim-buster

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/cobee.server-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]