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
- Check out the project, export variables and simply run the script:
```bash
$ source export_vars.sh
$ chmod +x run_legacy.sh
$ ./run_legacy.sh
```
- Builds the web app `war`, packs it into a docker tomcat8 container,
and sets the user `tomcat` with password `admin` for the Management Console at [http://localhost:8888/](http://localhost:8888/)
- Inits the MySQL Database docker container with the db user defined in `hibernate.cfg.xml`
- Sets up both containers and make the web app available under [http://localhost:8888/EShop-1.0.0/](http://localhost:8888/EShop-1.0.0/)

### Microservice Architecture
- Start Docker daemon
- Check out the project and simply run the script:
```bash
# Read env variables for DB location and credentials
$ source export_vars.sh
# Build all microservices, create docker images and start them
$ ./run_microservices.sh
```
- Access the new webshop via `http://localhost:8899/EShop-Micro-2.0.0/`

```bash
# for shutting down
$ ./shutdown_microservices.sh
```
## Useful links
- Eureka service registry: `http://localhost:8761/`
- Zuul Gateway routes: `http://localhost:8765/routes` TODO CSRF

**Note**: run e.g. `curl -D- -X GET localhost:8765/category` first, so hystrix produces stream data
- Local Monitoring Dashboard for Composite: `http://localhost:8084/hystrix` -> enter url `http://localhost:8765/hystrix.stream` 
- Monitoring Dashboard Service: `http://localhost:7979/hystrix` 
  - cat comp: `http://categorycompositeservice:8080/hystrix.stream`
  - gateway: `http://api-gateway:8765/hystrix.stream`

## `curl` statements for testing:
1. Obtain the access token and refresh token for the `grand_type=password` via one of the three methods:
  - `curl -v --insecure -H "Authorization: Basic $(echo -n 'acme:acmesecret' | base64)" http://localhost:8765/uaa/oauth/token -d grant_type=password -d username=admin -d password=admin`
  - `curl -v --insecure -u acme:acmesecret http://localhost:8765/uaa/oauth/token -d grant_type=password -d username=admin -d password=admin`
  - `curl -v --insecure http://acme:acmesecret@localhost:8765/uaa/oauth/token -d grant_type=password -d username=admin -d password=admin`
  
2. Save the obtained access token in the `ACCESS_TOKEN` env var:
`export ACCESS_TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0ODQ0ODExM...`

3. Perform requests
- `curl -v 'http://localhost:8765/category' -i -X GET -H 'Content-type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"`
- `curl -v 'http://localhost:8765/category' -i -X POST  -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN" -d '{"name":"TestCategory"}'`
- `curl -v 'http://localhost:8765/product' -i -X GET -H 'Content-type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"`
- `curl -v 'http://localhost:8765/product' -i -X POST -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN" -d '{"name":"NewProduct", "price":"1.00", "category":"1", "details":"fancy details"}'`
- `curl -v 'http://localhost:8765/user/admin' -i -X GET -H "Authorization: Bearer $ACCESS_TOKEN"`
- `curl -v 'http://localhost:8765/user/register' -i -X POST  -H 'Content-Type: application/json' -d '{"username":"jdoe", "firstname":"John", "lastname":"Doe", "password":"s3cr3t"}'`

## Debugging:
### Check MySQL contents
- Connect to container: `docker exec -it <#containerId> bash`
- Connect to db: `mysql -h localhost -D webshop -u webshopuser -p` -> password: `240b2c6d58ff2ce2f508b49f`


### API Documentation
The documentation of the services 
- CategoryService
- ProductService
- UserService
is available at the api-gateway: `http://localhost:8765/docs/api-guide.html`


### Notes
If you change the user and password of the MySQL database, you should run
```bash
$ docker-compose -f docker-compose-legacy.yml rm -v
$ rm -rf .data
```
Details can be found [here](https://github.com/docker-library/mysql/issues/51)
