#!/bin/bash

[ -z "$JAVA_XMS" ] && JAVA_XMS=3g
[ -z "$JAVA_XMX" ] && JAVA_XMX=600m

set -e

JAVA_OPTS="${JAVA_OPTS} \
  -Dapplication.name=${APP_NAME} \
  -Dapplication.home=${APP_HOME} \
  -Dotel.resource.attributes=service.name=${APP_NAME} \
  -javaagent:${APP_HOME}/opentelemetry-javaagent-all.jar"

exec java ${JAVA_OPTS} -jar "${APP_HOME}/${APP_NAME}.jar"