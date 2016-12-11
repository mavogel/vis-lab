# Eureka Server
By default (if you don't change the _application.yml_ port settings), find this server's dashboard at port 8761. Here, it will display any registered eureka clients.

## How to register a client

### Updating the pom.xml
Update the `dependencyManagement` and the `dependencies`:
```
<dependencyManagement>
    <dependencies>
        ...
        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>Camden.RELEASE</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>

<dependencies>
    ...
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka</artifactId>
	</dependency>
</dependencies>
```
### bootstrap.properties
Update your _bootstrap.properties_ or create them. They will be found next to your _application.yml_ in the resources directory. Add the following content:
```
spring.application.name=YOUR_MICROSERVICE_NAME
eureka.instance.appname=YOUR_MICROSERVICE_NAME
```

### Add an annotation to your SpringBootApplication
```
@EnableDiscoveryClient
```



Now, your client will register itself with the eureka server.