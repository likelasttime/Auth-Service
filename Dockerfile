FROM openjdk:11

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar","/app.jar"]