# 1단계: 빌드 단계
FROM gradle:7.4-jdk17-alpine AS builder

WORKDIR /app

# Gradle 캐싱을 활용하여 빌드 속도 향상
COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

# 프로젝트 소스 코드 복사 및 빌드
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 단계
FROM amazoncorretto:17-alpine

WORKDIR /app

# 빌드된 JAR 파일을 실행할 디렉토리로 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# container 내 파일 시스템과 노드 파일 시스템 심볼릭 링크로 연결 (컨테이너 내 로그파일을 노드 파일 시스템에서도 볼 수 잇도록)
#RUN mkdir -p /logs/error && ln -s /dev/stderr /logs/error &&  \
#    mkdir -p /logs/access && ln -s /dev/stdout /logs/access

# 컨테이너 실행 시 JAR 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
