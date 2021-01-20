#!/bin/sh

./mvnw package -DskipTests && \
docker-compose -f all-in-one.yml build && \
docker-compose -f all-in-one.yml up