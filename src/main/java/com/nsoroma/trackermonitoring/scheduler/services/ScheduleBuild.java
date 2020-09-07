package com.nsoroma.trackermonitoring.scheduler.services;

import com.nsoroma.trackermonitoring.scheduler.model.Schedule;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class ScheduleBuild {

    public JobDetail buildScheduleDetail(Schedule schedule) {
        JobDataMap jobDataMap = new JobDataMap();

        if(schedule.getScheduleId() == null) {
            String scheduleId = UUID.randomUUID().toString();
            schedule.setScheduleId(scheduleId);
        }

        jobDataMap.put("email", schedule.getEmail());
        jobDataMap.put("subject", schedule.getSubject());
        jobDataMap.put("body", "Please find attached the data intervals for filter parameters: / " +
                "Start Date : " + schedule.getStartDate() +
                " ---> End Date : " + schedule.getEndDate() +
                " / Tracker Type : " + schedule.getTrackerType() +
                " / Customer Id : " + schedule.getCustomerId() +
                " / Status : " + schedule.getStatus() +
                "/ Time Frame : " + schedule.getTimeFrame() +
                "/ End Time Frame : " + schedule.getEndTimeFrame() +
                "/ Start Time Frame : " + schedule.getStartTimeFrame());
        jobDataMap.put("scheduleId", schedule.getScheduleId());
        jobDataMap.put("alertFrequency",schedule.getAlertFrequency());
        jobDataMap.put("alertTime", schedule.getAlertTime().toString());
        jobDataMap.put("zoneId", schedule.getZoneId().toString());
        jobDataMap.put("startDate", schedule.getStartDate());
        jobDataMap.put("endDate", schedule.getEndDate());
        jobDataMap.put("trackerType", schedule.getTrackerType());
        jobDataMap.put("customerId", schedule.getCustomerId());
        jobDataMap.put("status", schedule.getStatus());
        jobDataMap.put("timeFrame", schedule.getTimeFrame());
        jobDataMap.put("endTimeFrame", schedule.getEndTimeFrame());
        jobDataMap.put("startTimeFrame", schedule.getStartTimeFrame());
        jobDataMap.put("scheduleType", schedule.getScheduleType());

        return JobBuilder.newJob(ScheduleExecution.class)
                .withIdentity(schedule.getScheduleId(), "NsoromaTrackerMonitoringSystemJobs")
                .withDescription("execute schedules for the tracker monitoring system")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildScheduleTrigger(JobDetail jobDetail, ZonedDateTime startTime) {

        String alertFrequency = jobDetail.getJobDataMap().getString("alertFrequency");

        if (alertFrequency.equals("everyday")) {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "NsoromaTrackerMonitoringSystemTriggers")
                    .withDescription("triggers for the Nsoroma tracker monitoring system")
                    .startAt(Date.from(startTime.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24).withMisfireHandlingInstructionFireNow())
                    .build();
        } else {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "NsoromaTrackerMonitoringSystemTriggers")
                    .withDescription("triggers for the Nsoroma tracker monitoring system")
                    .startAt(Date.from(startTime.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
        }

    }
}
