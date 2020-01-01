package com.nsoroma.trackermonitoring.scheduler.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Schedule {

    private String scheduleId;
    private String alertFrequency;
    private LocalDateTime alertTime;
    private ZoneId zoneId;
    private String startDate;
    private String endDate;
    private String trackerType;
    private String customerId;
    private String email;
    private String status;
    private String subject;
    private String timeFrame;
    private String endTimeFrame;
    private String startTimeFrame;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlertFrequency() {
        return alertFrequency;
    }
    public void setAlertFrequency(String alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }
    public void setAlertTime(LocalDateTime alertTime) { this.alertTime = alertTime; }

    public ZoneId getZoneId() { return zoneId; }
    public void setZoneId(ZoneId zoneId) { this.zoneId = zoneId; }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTrackerType() {
        return trackerType;
    }
    public void setTrackerType(String trackerType) {
        this.trackerType = trackerType;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTimeFrame() { return timeFrame; }
    public void setTimeFrame( String timeFrame) { this.timeFrame = timeFrame; }

    public String getEndTimeFrame() { return endTimeFrame; }
    public void setEndTimeFrame(String endTimeFrame) { this.endTimeFrame = endTimeFrame; }

    public String getStartTimeFrame() { return startTimeFrame; }
    public void setStartTimeFrame(String startTimeFrame) { this.startTimeFrame = startTimeFrame; }
}
