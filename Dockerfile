FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

COPY . .

RUN ./gradlew build --no-daemon

FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]