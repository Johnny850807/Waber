FROM openjdk:11-jre-slim

ENV APP_NAME waber-payment
ENV APP_HOME /

RUN mkdir -p ${APP_HOME}

COPY ./target/*.jar ${APP_HOME}/${APP_NAME}.jar
COPY ./bin/opentelemetry-javaagent-all.jar ${APP_HOME}/
COPY ./bin/start-app.sh ${APP_HOME}/

WORKDIR ${APP_HOME}

ENTRYPOINT ["sh", "start-app.sh"]