FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootJar --no-deamon

FROM openjdk:17-jdk-slim
COPY --from=build /build/libs/AnhCop-production.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]