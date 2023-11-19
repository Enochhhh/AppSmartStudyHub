FROM openjdk:17-jdk

WORKDIR /app

COPY target/smartstudyhub-0.0.1-SNAPSHOT.jar /app/smartstudyhub.jar

EXPOSE 8080

CMD [ "java", "-jar",  "smartstudyhub.jar"]