FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /build
COPY build.gradle settings.gradle ./
COPY gradle/ ./gradle/
COPY gradlew ./
RUN chmod +x ./gradlew
COPY ./src ./src


RUN ./gradlew build --no-daemon -x test
FROM amazoncorretto:21-alpine3.21
WORKDIR /app
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]