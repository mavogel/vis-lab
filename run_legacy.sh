#!/bin/sh

# 1: Check for required deps in the $PATH
BINS_TO_CHECK=( mvn docker docker-compose )
for BIN in ${BINS_TO_CHECK[@]}; do command -v $BIN >/dev/null 2>&1 || { echo >&2 "I require '$BIN' but it's not installed.  Aborting."; exit 1; }; done
echo "[INFO] Need binaries found in path"

# 2: Build legacy webshop
echo "[INFO] Bulding legacy web shop"
cd LegacyWebShop && mvn clean package

# 3: Compose tomcat server with webshop war and start MySql Server as well
echo "[INFO] Building local docker tomcat container with legacy webshop war"

echo "[INFO] Composing MySQL Server container and legacy webshop container"

echo "[INFO] Legacy WebShop started -> 'http://localhost:xxxx'"
