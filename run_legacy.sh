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
# 1: Check for required deps in the $PATH
info "Check for needed binaries in PATH";
BINS_TO_CHECK=( mvn docker docker-compose )
for BIN in ${BINS_TO_CHECK[@]}; do command -v $BIN >/dev/null 2>&1 || { echo >&2 "I require '$BIN' but it's not installed.  Aborting."; exit 1; }; done
echo "-> Fine √"

# 1.1 check if docker deamon runs
info "Checking if docker is running";
docker ps
echo "-> Fine √"

# 2: Build legacy webshop??
info "Bulding legacy web shop"
cd LegacyWebShop
mvn clean package
cd ..

# 3: Build initialized MySQL Database image
info "Building initialized MySQL Database image"
docker build -t web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

# 4: Compose all together and init db
info "Composing MySQL Server container, tomcat8 and deploying webshop war"
docker-compose -f docker-compose-legacy.yml up -d

# 5: done
info "Legacy WebShop started -> 'http://localhost:8888/EShop-0.0.1-SNAPSHOT/'"
