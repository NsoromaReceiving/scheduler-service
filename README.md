# Tracker Monitoring Scheuder #

## Quick summary ##

This application implements a task scheuler using : [Quartz] (http://www.quartz-scheduler.org/). To schedule various types of tracker interval reports to be sent via email. 


## Version

* Spring Boot 2.1.7
* Spring Cloud 1.1.7


## Execution instructions ##

The application starts as a normal Spring Boot application:

* Run `mvn clean spring-boot:run` inside the proeject

## API Documentation
Working URLEndPoints.
1. **Post Schedule** `/api/schedules`
    * ***alertFrequency*** : indictes how often the report should be sent (daily, once)
    * ***alertTime*** : Time at which report is built and sent
    * ***zoneId*** : The time zone id according to the `java.time.ZonedDateTime` library
    * ***startDate*** : The upper date from which to filter
    * ***endDate*** : The lower date from which to filter
    * ***trackerType*** : The tracker type to filtered by
    * ***customerId*** : The Id of the targeted customer whose trackers is to be filtered
    * ***email*** : Reciepient email to send report to
    * ***status*** : The tracker status to filter for.
    * ***subject*** : The subject of report email 
    * ***timeFrame*** : Time interval to filter for trackers
    * ***endTimeFrame*** : The end time interval to filter for trackers
    * ***startTimeFrame*** : The start time interval to filter for trackers
    * ***server*** : The server target (one / two)
    * ***scheduleType*** : The type of schedule to generate (INHOUSE / CLIENT)

2. **Get List of all schedules** `/api/schedules`.
2. **Get schedule by id** `/api/schedule/{id}`.
3. **Delete schedule by id** `/api/schedule/{id}`.
4. **Update schedule by id** `/api/schedule/{id}`

## Service Architecture
1. **Schedule Creation**: The schedules are stored in an sql database initialized the necessary quartz database tables. All data pertaining to the individual schedules and stored in the quartz database and the trigger date/time for the schedule is set as the *alertTime* for that schedule. Implementation can be found in `src/java/com/nsoroma/trackermonitoring/scheduler/services/ScheduleBuild.java`
2. **Schedule Execution**: Schedule executions are triggered automatically by the quartz schedule and the neccessray data are process to make an in service call to tracker service for the data which is then bundle up as an attachement or as part of the email body before being sent to the receipient. Necessary implementation can be found in `src/java/com/nsoroma/trackermonitoring/scheduler/services/ScheduleExecution.java`

3. **Service - service call** This is implemented using netflix feign client `src/java/com/nsoroma/trackermonitoring/scheduler/clientservices/TrackerMonitoringClient.java`

## Depolyment Strategy
The application is dockerised with a docker file using java 8 found in a root folder.