# Build stage
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
RUN apk add --no-cache netcat-openbsd bash
COPY src/main/resources/wait-for-oracle.sh /wait-for-oracle.sh
RUN chmod +x /wait-for-oracle.sh
EXPOSE 8080
ENTRYPOINT ["/wait-for-oracle.sh"]