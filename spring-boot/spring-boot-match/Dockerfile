FROM adoptopenjdk/openjdk11:alpine-slim

COPY ./target/*.jar /match-service.jar
COPY ./bin/opentelemetry-javaagent-all.jar /

CMD ["java", "-javaagent:/opentelemetry-javaagent-all.jar", "-jar", "/match-service.jar"]