FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y openjdk-17-jdk

# Install Maven
RUN apt-get install -y maven

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /build/target/jewelry.jar jewelry.jar

ENTRYPOINT ["java", "-jar", "jewelry.jar"]