#!/bin/sh

source run_base_script.sh

info "Check if ENV variables were set"
[ -z "$MYSQL_WEBSHOP_DB_ADDR" ] && echo "Need to set MYSQL_WEBSHOP_DB_ADDR ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$MYSQL_WEBSHOP_DB_DATABASE" ] && echo "Need to set MYSQL_WEBSHOP_DB_DATABASE ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$MYSQL_WEBSHOP_DB_USER" ] && echo "Need to set MYSQL_WEBSHOP_DB_USER ENV-var. Run '$ source export_vars.sh' first!" && exit 1;
[ -z "$MYSQL_WEBSHOP_DB_PASSWORD" ] && echo "Need to set MYSQL_WEBSHOP_DB_PASSWORD ENV-var. Run '$ source export_vars.sh' first!" && exit 1;

# 2: Build legacy webshop??
info "Bulding legacy web shop"
cd LegacyWebShop && mvn clean package && cd ..

# 3: Build initialized MySQL Database image
info "Building initialized MySQL Database image"
docker build -t web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

# 4: Compose all together and init db
info "Composing MySQL Server container, tomcat8 and deploying webshop war"
docker-compose -f docker-compose-legacy.yml up -d --remove-orphans

# 5: done
info "Legacy WebShop started -> 'http://localhost:8888/EShop-1.0.0/'"

docker-compose -f docker-compose-legacy.yml logs -tf
