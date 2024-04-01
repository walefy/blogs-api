FROM maven:3-openjdk-17-slim as build-image
LABEL authors="walefy"

WORKDIR /build
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
COPY . .
RUN mvn package spring-boot:repackage -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build-image /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]