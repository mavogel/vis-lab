#!/bin/sh

source run_base_script.sh

info "Checking if docker is running";
docker ps
echo "-> Fine √"

info "Stopping microservices"
docker-compose -f docker-compose-microservices.yml down --remove-orphans
echo "-> Done √"