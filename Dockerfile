FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080

RUN ls -la /app/

ENTRYPOINT ["java", "-jar", "app.jar"]