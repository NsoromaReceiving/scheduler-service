# Tracker Monitoring Scheuder #

## Quick summary ##

This application implements a task scheuler using : [Quartz] (http://www.quartz-scheduler.org/).

It also registers as spring cloud eureka client on this : [Eureka-Server] (https://banabasave@bitbucket.org/banabasave/tracker-monitoring-eureka-server.git) in order to be acessible by the [nsoroma-tracker-monitoring-service] (https://banabasave@bitbucket.org/hawkmanlabs/nsoroma-tracker-monitoring-service.git)

It uses [Spring Boot](http://projects.spring.io/spring-boot/) to start Spring context and run the application and [Spring Cloud Eureka](https://cloud.spring.io/spring-cloud-netflix/) to integrate Netflix implementation into Spring.

##Version

* Spring Boot 2.1.7
* Spring Cloud 1.1.7
* Quartz 2.1.7



### Execution instructions ###

The application starts as a normal Spring Boot application:

* Run `mvn clean spring-boot:run` inside the proeject