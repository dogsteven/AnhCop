FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
EXPOSE 9120


COPY --from=build /build/libs/AnhCop-production.jar app.jar

ENV FIREBASE_GOOGLE_CREDENTIALS /etc/secrets/firebase-service-account.json

ENTRYPOINT ["java","-jar","/app.jar"]