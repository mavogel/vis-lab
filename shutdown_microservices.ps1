Write-Host "Stopping microservices"
docker-compose -f docker-compose-microservices.yml down --remove-orphans