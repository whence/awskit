FROM openjdk:8-jre-alpine

COPY ["build/libs/*.jar", "/app/app.jar"]

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
