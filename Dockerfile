FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
EXPOSE 8080


COPY --from=build /build/libs/AnhCop-production.jar app.jar
COPY --from=build /firebase-service-account.json firebase-service-account.json

ENV PG_HOST ep-still-wood-995647.ap-southeast-1.aws.neon.tech
ENV PG_DATABASE neondb
ENV PG_USERNAME dogsteven
ENV PG_PASSWORD lTfoL9iUc4qb

ENV JWT_AUDIENCE anhcop
ENV JWT_ISSUER anhcop-auth
ENV JWT_SECRET_KEY 7sDs74Z7rddP3Lp/CQmBkzDlHBwIJ6q6khVD9lYgEYFb91aAdR36mUNQr0DNggH2

ENV FIREBASE_GOOGLE_CREDENTIALS firebase-service-account.json

ENTRYPOINT ["java","-jar","/app.jar"]