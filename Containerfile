FROM amazoncorretto:17

COPY /target/app.jar /app.jar

CMD ["-Xmx256m", "-jar", "/app.jar"]