FROM openjdk:17-jdk
COPY target/app.jar .
EXPOSE 8080
CMD mvn clean install
ENTRYPOINT ["java", "-jar", "/app.jar"]