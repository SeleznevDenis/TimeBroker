FROM openjdk:11
VOLUME /tmp
COPY target/TimeBroker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]