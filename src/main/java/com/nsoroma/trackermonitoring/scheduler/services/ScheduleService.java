package com.nsoroma.trackermonitoring.scheduler.services;

import com.nsoroma.trackermonitoring.scheduler.model.Schedule;
import org.quartz.SchedulerException;

import java.util.List;

public interface ScheduleService {

    Boolean createSchedule(Schedule schedule) throws SchedulerException;

    List<Schedule> getSchedules() throws SchedulerException;

    Schedule getSchedule(String id) throws SchedulerException;

    Boolean deleteSchedule(String id) throws SchedulerException;

    Boolean updateSchedule(String id, Schedule schedule) throws SchedulerException;
}
