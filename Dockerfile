# Build Environment
FROM maven:3.9.9-sapmachine-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -P 'prod' -DskipTests
# Run Environment
FROM openjdk:17
WORKDIR /
COPY --from=builder /app/target/forex-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]