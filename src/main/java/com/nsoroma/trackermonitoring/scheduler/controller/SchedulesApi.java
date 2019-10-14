package com.nsoroma.trackermonitoring.scheduler.controller;

import com.nsoroma.trackermonitoring.scheduler.model.Schedule;
import com.nsoroma.trackermonitoring.scheduler.services.ScheduleService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchedulesApi {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/api/schedules")
    public @ResponseBody
    List<Schedule> getSchedulesList() throws SchedulerException {
        return scheduleService.getSchedules();
    }

    @PostMapping("/api/schedules")
    public Boolean createSchedule(@RequestBody Schedule schedule) throws SchedulerException {
        return scheduleService.createSchedule(schedule);
    }

    @GetMapping("/api/schedule/{id}")
    public Schedule getSchedule(@PathVariable("id") String id) throws SchedulerException {
        return scheduleService.getSchedule(id);
    }

    @DeleteMapping("/api/schedule/{id}")
    public Boolean deleteSchedule(@PathVariable("id") String id) throws SchedulerException {
        return scheduleService.deleteSchedule(id);
    }

    @PutMapping("/api/schedule/{id}")
    public Boolean updateSchedule(@PathVariable("id") String id, @RequestBody Schedule schedule) throws SchedulerException {
        return scheduleService.updateSchedule(id, schedule);
    }

}
