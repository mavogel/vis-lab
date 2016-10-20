# Distributed Information Systems Laboratory aka vis-lab
Splitting a monilithic web shop application from Tomcat into microservices with Spring Boot and Docker. This project is part of the masters course 'Distributed Information Systems' at the University of Applied Sciences (Karlsruhe)

## Prerequisites
- maven
- docker
- docker-compose

## Starting
### Legacy System
Simply run the script:
```bash
$ chmod +x run_legacy.sh
$ ./run_legacy.sh
```
- Builds the web app and packs it into a docker tomcat8 container
- Init the MySQL Database
- Sets up both containers and make it available under `http://localhost:8888/EShop-0.0.1-SNAPSHOT/``

### Microservice Architecture
TODO
