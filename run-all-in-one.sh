#!/bin/sh

./mvnw clean package -DskipTests && \
docker-compose -f all-in-one.yml build && \
docker-compose -f all-in-one.yml up