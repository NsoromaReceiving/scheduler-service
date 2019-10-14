package com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.clientservice;

import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.model.TrackerState;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;

@FeignClient("NsoromaTrackerMonitoringService")
@Headers({"Accept: application/json", "Content-Type: application/json"})
public interface TrackerMonitoringClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/trackers", produces = "application/json", consumes = "application/json") //define with the entire job details
    LinkedHashSet<TrackerState> getTrackers(@RequestParam(value = "startDate", required = false) String startDate, //call on schedule execute
                                            @RequestParam(value = "endDate", required = false) String endDate,
                                            @RequestParam(value = "customerId", required = false) String customerId,
                                            @RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = "order", required = false) String order,
                                            @RequestParam(value = "status", required = false) String status);
}
