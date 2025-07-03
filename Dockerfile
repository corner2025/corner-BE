# 1) 베이스 이미지: JDK17
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 2) 로컬에서 미리 빌드된 JAR 복사
COPY build/libs/*.jar app.jar

# 3) 애플리케이션 포트
EXPOSE 8080

# 4) 실행 커맨드
ENTRYPOINT ["java","-jar","app.jar"]
