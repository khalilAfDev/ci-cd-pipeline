FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/ci-cd-pipeline-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "-Dserver.port=8081"]