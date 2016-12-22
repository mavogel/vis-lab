[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg)](http://opensource.org/licenses/MIT)
[![Travis Build Status](https://travis-ci.org/mavogel/vis-lab.svg?branch=master)](https://travis-ci.org/mavogel/vis-lab)

# Distributed Information Systems Laboratory aka vis-lab
Splitting a monolithic web shop application, running on a Tomcat into small domain-driven microservices with Spring Boot and Docker. This project is part of the masters course 'Distributed Information Systems' at the University of Applied Sciences (Karlsruhe).

## Prerequisites
- [maven](https://maven.apache.org/)
- [docker](https://docker.com)
- docker-compose

## Starting
### Legacy System
- Start Docker daemon
- Check out the project and simply run the script:
```bash
$ chmod +x run_legacy.sh
$ ./run_legacy.sh
```
- Builds the web app `war`, packs it into a docker tomcat8 container,
and sets the user `tomcat` with password `admin` for the Management Console at [http://localhost:8888/](http://localhost:8888/)
- Inits the MySQL Database docker container with the db user defined in `hibernate.cfg.xml`
- Sets up both containers and make the web app available under [http://localhost:8888/EShop-1.0.0/](http://localhost:8888/EShop-1.0.0/)

### Microservice Architecture
**DRAFT:**
- Start Docker daemon
- Check out the project and simply run the script:
```bash
# Read env variables for DB location and credentials
$ source export_vars.sh
# Build all microservices, create docker images and start them
$ ./run_microservices.sh
# for shutting down
$ ./shutdown_microservices.sh
```
## Useful links
- Eureka service registry: `http://localhost:8761/`
- Zuul Gateway routes: `http://localhost:8088/routes`

**Note**: run e.g. `curl -D- -X GET localhost:8088/category` first, so hystrix produces stream data
- Local Monitoring Dashboard for Composite: `http://localhost:8084/hystrix` -> enter url `http://localhost:8080/hystrix.stream` 
- Monitoring Dashboard Service: `http://localhost:7979/hystrix` 
  - cat comp: `http://categorycompositeservice:8080/hystrix.stream`
  - gateway: `http://gateway:8081/hystrix.stream`

## `curl` statements for testing:
- `curl -D- -X GET localhost:8088/category`
- `curl 'http://localhost:8080/category' -i -X POST  -H 'Content-Type: application/json' -d '{"name":"TestCategory"}'`
- `curl -D- -X GET localhost:8088/product`
- `curl 'http://localhost:8080/product' -i -X POST  -H 'Content-Type: application/json' -d '{"name":"NewProduct", "price":"1.00", "category":"1", "details":"fancy details"}'`
- `curl -D- -X GET localhost:8088/user/admin`
- `curl 'http://localhost:8080/user' -i -X POST  -H 'Content-Type: application/json' -d '{"username":"jdoe", "firstname":"John", "lastname":"Doe", "password":"s3cr3t"}'`

### Documentation
**TODO**: aggreate in Gateway
- CategoryService: `http://localhost:8081/docs/api-guide.html`


### Notes
If you change the user and password of the MySQL database, you should run
```bash
$ docker-compose -f docker-compose-legacy.yml rm -v
$ rm -rf .data
```
Details can be found [here](https://github.com/docker-library/mysql/issues/51)
