#!/bin/sh

./mvnw clean package -T 4 -DskipTests && \
docker-compose -f all-in-one.yml build && \
docker-compose -f all-in-one.yml up