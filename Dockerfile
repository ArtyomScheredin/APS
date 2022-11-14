FROM openjdk:17-jdk-alpine
MAINTAINER Artyom Scheredin
COPY build/libs/APS-1.0-SNAPSHOT-all.jar APS-1.0-SNAPSHOT-all.jar
ENTRYPOINT ["java","-jar","/APS-1.0-SNAPSHOT-all.jar"]