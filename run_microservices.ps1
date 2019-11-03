# 1: Set Environment Variables from $env:s_vars.sh
docker login
$env:DOCKER_PROJECT_IMAGE_PREFIX='vis'
$env:TAG='latest'

$env:MYSQL_WEBSHOP_DB_ADDR='web-shop-db-image';
$env:MYSQL_WEBSHOP_DB_PORT='3306';
$env:MYSQL_WEBSHOP_DB_DATABASE='webshop';
$env:MYSQL_WEBSHOP_DB_USER='webshopuser';
$env:MYSQL_WEBSHOP_DB_PASSWORD='240b2c6d58ff2ce2f508b49f';


#2: "Building shared/dtos"
Write-Host "Building shared/dtos"
Set-Location shared/dtos
mvn clean install
Set-Location ../..

#3: "Building microservices and docker images"
Write-Host "Building microservices and docker images"
mvn clean package docker:build #-DskipTests

#4 "Building new version of legacy webshop"
Write-Host "Building new version of legacy webshop"
Set-Location LegacyWebShop_Micro 
mvn clean package 
Set-Location ../
docker build -t mavogel/vis-legacywebshop-micro ./LegacyWebShop_Micro

#5 "Building OLD version of legacy webshop"
Write-Host "Building OLD version of legacy webshop"
Set-Location LegacyWebShop 
mvn clean package 
Set-Location ../
docker build -t mavogel/vis-legacywebshop ./LegacyWebShop

#6 "Building initialized MySQL Database image"
Write-Host "Building initialized MySQL Database image"
docker build -t mavogel/vis-web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

#7 "Stopping and removing old containers"
Write-Host "Stopping and removing old containers"
docker-compose -f docker-compose-microservices.yml stop
docker-compose -f docker-compose-microservices.yml rm -f

#8 "Composing microservice containers"
Write-Host "Composing microservice containers"
docker-compose -f docker-compose-microservices.yml up -d --remove-orphans
docker-compose -f docker-compose-microservices.yml logs -tf

# 5: done
Write-Host "Legacy WebShop started -> 'http://localhost:8899/EShop-Micro-2.0.0'"
Write-Host "get logs via: 'docker-compose -f docker-compose-microservices.yml logs -tf'"
