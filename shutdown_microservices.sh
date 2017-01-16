#!/bin/sh

source run_base_script.sh

info "Checking if docker is running";
docker ps
echo "-> Fine √"

info "Stopping microservices"
docker-compose -f docker-compose-microservices.yml down
echo "-> Done √"

info "Unsetting ENV variables"
unset DOCKER_USER
unset TAG
unset DOCKER_PROJECT_IMAGE_PREFIX
unset MYSQL_CATEGORY_DB_ADDR
unset MYSQL_CATEGORY_DB_PORT
unset MYSQL_CATEGORY_DB_DATABASE
unset MYSQL_CATEGORY_DB_USER
unset MYSQL_CATEGORY_DB_PASSWORD
echo "-> Done √"