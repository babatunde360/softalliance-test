FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean compile package -DskipTests

FROM amazoncorretto:17.0.7-alpine
LABEL authors="babatundeoladotun"

RUN addgroup -S app && adduser -S app -G app

USER app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]