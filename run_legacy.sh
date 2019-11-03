#!/bin/bash

source run_base_script.sh

# 2: Build legacy webshop
info "Bulding legacy web shop"
cd LegacyWebShop && mvn -X clean package && cd ..
docker build -t mavogel/vis-legacywebshop ./LegacyWebShop

# 3: Build initialized MySQL Database image
info "Building initialized MySQL Database image"
docker build -t mavogel/vis-web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

# 4: Compose all together and init db
info "Composing MySQL Server container, tomcat8 and deploying webshop war"
docker-compose -f docker-compose-legacy.yml up -d --remove-orphans

# 5: done
info "Legacy WebShop started -> 'http://localhost:8888/EShop-1.0.0/'"
info "get logs via: 'docker-compose -f docker-compose-legacy.yml logs -tf'"
