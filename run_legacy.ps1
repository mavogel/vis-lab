# 1: Set Environment Variables from $env:s_vars.sh
$env:DOCKER_USER='mavogel'
$env:DOCKER_PROJECT_IMAGE_PREFIX='vis'
$env:TAG='latest'

$env:MYSQL_WEBSHOP_DB_ADDR='web-shop-db-image';
$env:MYSQL_WEBSHOP_DB_PORT='3306';
$env:MYSQL_WEBSHOP_DB_DATABASE='webshop';
$env:MYSQL_WEBSHOP_DB_USER='webshopuser';
$env:MYSQL_WEBSHOP_DB_PASSWORD='240b2c6d58ff2ce2f508b49f';

# 2: Build legacy webshop
Write-Host "Bulding legacy web shop"
Set-Location LegacyWebShop
mvn clean package
Set-Location ../
docker build -t mavogel/vis-legacywebshop ./LegacyWebShop 

#3: Build initialized MySQL Database image
Write-Host "Building initialized MySQL Database image"
docker build -t mavogel/vis-web-shop-db-image -f ./LegacyWebShop/DockerfileMySQL ./LegacyWebShop

# 4: Compose all together and init db
Write-Host "Composing MySQL Server container, tomcat8 and deploying webshop war"
docker-compose -f docker-compose-legacy.yml up -d --remove-orphans

# 5: done
Write-Host "Legacy WebShop started -> 'http://localhost:8888/EShop-1.0.0/'"
Write-Host "get logs via: 'docker-compose -f docker-compose-legacy.yml logs -tf'"