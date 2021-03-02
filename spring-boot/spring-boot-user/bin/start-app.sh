#!/bin/bash


set -e

# OpenTelemetry:
# https://github.com/open-telemetry/opentelemetry-java-instrumentation
JAVA_OPTS="${JAVA_OPTS} \
  -Dapplication.name=${APP_NAME} \
  -Dapplication.home=${APP_HOME} \
  -Dotel.exporter=jaeger \
  -Dotel.jaeger.endpoint=jaeger:14250 \
  -Dotel.jaeger.service.name=${APP_NAME} \
  -javaagent:${APP_HOME}/opentelemetry-javaagent-all.jar"

exec java ${JAVA_OPTS} \
  -jar "${APP_HOME}/${APP_NAME}.jar"