FROM amazoncorretto:21-alpine3.21 AS builder
WORKDIR /build
RUN apk add --no-cache bash
COPY build.gradle settings.gradle ./
COPY gradle/ ./gradle/
COPY gradlew ./
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon --quiet
COPY ./src ./src
RUN ./gradlew build --no-daemon -x test --quiet

FROM amazoncorretto:21-alpine3.21
WORKDIR /app
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]