#!/bin/sh

source run_base_script.sh

#######
info "Building microservices and docker images"
mvn clean install #-DskipTests

#######
info "Building new version of legacy webshop"
cd LegacyWebShop_Micro && mvn clean package && cd ..
docker build -t mavogel/vis-legacywebshop-micro ./LegacyWebShop

#######
info "Building OLD version of legacy webshop"
cd LegacyWebShop && mvn clean package && cd ..
docker build -t mavogel/vis-legacywebshop ./LegacyWebShop

#######
info "Building initialized MySQL Database image"
docker build -t mavogel/vis-web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

#######
info "Stopping and removing old containers"
docker-compose -f docker-compose-microservices.yml stop && docker-compose -f docker-compose-microservices.yml rm -f

#######
info "Composing microservice containers"
docker-compose -f docker-compose-microservices.yml up -d --remove-orphans && docker-compose -f docker-compose-microservices.yml logs -tf
