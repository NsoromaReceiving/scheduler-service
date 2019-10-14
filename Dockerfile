FROM maven:3.6.2-jdk-8 As Package

COPY src /usr/app/src

COPY pom.xml /usr/app

RUN mvn -f /usr/app/pom.xml clean package



FROM java:8-jdk

COPY --from=Package ./usr/app/target/tracker-monitoring-scheduler-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch tracker-monitoring-scheduler-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java", "-jar", "tracker-monitoring-scheduler-0.0.1-SNAPSHOT.jar"]