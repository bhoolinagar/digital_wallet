FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY ./target/TransactionService-0.0.1-SNAPSHOT.jar app.jar

LABEL authors="bhooli.nagar"

ENTRYPOINT ["java", "-jar", "app.jar"]