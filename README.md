# Distributed Information Systems Laboratory aka vis-lab
Splitting a monolithic web shop application, running on a Tomcat into small domain-driven microservices with Spring Boot and Docker. This project is part of the masters course 'Distributed Information Systems' at the University of Applied Sciences (Karlsruhe).

## Prerequisites
- [maven](https://maven.apache.org/)
- [docker](https://docker.com)
- docker-compose

## Starting
### Legacy System
- Start Docker deamon
- Check out the project and simply run the script:
```bash
$ chmod +x run_legacy.sh
$ ./run_legacy.sh
```
- Builds the web app `war` and packs it into a docker tomcat8 container
- Inits the MySQL Database docker container
- Sets up both containers and make the web app available under [http://localhost:8888/EShop-0.0.1-SNAPSHOT/](http://localhost:8888/EShop-0.0.1-SNAPSHOT/)

### Microservice Architecture
will be realized soon...
