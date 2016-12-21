#!/bin/sh

source run_base_script.sh

# 1.1 check if docker deamon runs
info "Checking if docker is running";
docker ps
echo "-> Fine √"

# 1.2 unsetting ENVS
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

# 2: stopping microservices
info "Stopping microservices"
docker-compose -f docker-compose-microservices.yml stop
echo "-> Done √"
