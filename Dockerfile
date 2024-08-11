FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/franchise-0.0.1-SNAPSHOT.jar api-franchise.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-franchise.jar"]