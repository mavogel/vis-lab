#!/bin/sh

###################
# Config
###################
# Aborts the script if a command fails
set -e

###################
# Vars and helpers
###################
SP='********';
function info () {
  echo "";
  echo "${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}";
  echo "$SP [SCRIPT INFO] $1";
  echo "${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}${SP}";
  echo "";
}

###################
## Go woop woop
###################
info "Check for needed binaries in PATH";
BINS_TO_CHECK=( mvn docker docker-compose )
for BIN in ${BINS_TO_CHECK[@]}; do
  command -v $BIN >/dev/null 2>&1 || { echo >&2 "I require '$BIN' but it's not installed.  Aborting."; exit 1; };
done
echo "-> Fine √"

#######
info "Checking if docker is running";
docker ps
echo "-> Fine √"

#######
info "Check if ENV variables were set"
[ -z "$DOCKER_USER" ] && echo "Need to set DOCKER_USER ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$DOCKER_PROJECT_IMAGE_PREFIX" ] && echo "Need to set DOCKER_PROJECT_IMAGE_PREFIX ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$TAG" ] && echo "Need to set TAG ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$MYSQL_WEBSHOP_DB_ADDR" ] && echo "Need to set MYSQL_WEBSHOP_DB_ADDR ENV-var. Run '$ source export_vars.sh' first!" && exit 1;

#######
info "Building microservices and docker images"
mvn clean package docker:build

#######
info "Building initialized MySQL Database image"
docker build -t ${MYSQL_WEBSHOP_DB_ADDR} -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

#######
# x: Compose all together
docker-compose -f docker-compose-microservices.yml stop
docker-compose -f docker-compose-microservices.yml rm

info "Composing microservice containers"
docker-compose -f docker-compose-microservices.yml up -d --remove-orphans
