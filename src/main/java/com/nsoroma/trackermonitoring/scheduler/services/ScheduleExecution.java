package com.nsoroma.trackermonitoring.scheduler.services;

import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.clientservice.DocumentsService;
import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.clientservice.TrackerMonitoringClient;
import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.model.TrackerState;
import org.apache.poi.ss.usermodel.Sheet;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Optional;


public class ScheduleExecution extends QuartzJobBean {

    @Autowired
    private
    TrackerMonitoringClient trackerMonitoringClient;

    @Autowired
    private
    DocumentsService documentsService;

    @Autowired
    private
    JavaMailSender mailSender;

    @Autowired
    private
    MailProperties mailProperties;

    @Autowired
    private
    ScheduleService scheduleService;
 //refactor to fetch from eureka server instance
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String receiverMail = jobDataMap.getString("email");
        String trackerType = jobDataMap.getString("trackerType");
        String customerId = jobDataMap.getString("customerId");
        String status = jobDataMap.getString("status");
        String order = null;
        Optional<String> alertFrequency = Optional.ofNullable(jobDataMap.getString("alertFrequency"));
        String startDate = jobDataMap.getString("startDate");
        String endDate = jobDataMap.getString("endDate");

        String scheduleZoneId = jobDataMap.getString("zoneId");
        ZoneId zoneId = ZoneId.of(scheduleZoneId);
        DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //time frame
        String timeFrame = jobDataMap.getString("timeFrame");
        if(timeFrame != null && !timeFrame.equals("")) {
            int period = Integer.parseInt(timeFrame);
            ZonedDateTime zonedStartDate = ZonedDateTime.now(zoneId).minusDays(1);
            startDate = zonedStartDate.minusDays(period).format(df);
            endDate = zonedStartDate.format(df);
        }

        //endTimeFrame
        String endTimeFrame = jobDataMap.getString("endTimeFrame");
        if(endTimeFrame != null && !endTimeFrame.equals("")) {
            int periodToEndDate = Integer.parseInt(endTimeFrame);
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId).minusDays(periodToEndDate);
            endDate = zonedDateTime.format(df);
        }

        //startTimeFrame
        String startTimeFrame = jobDataMap.getString("startTimeFrame");
        if(startTimeFrame != null && !startTimeFrame.equals("")) {
            int periodToStartDate = Integer.parseInt(startTimeFrame);
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId).minusDays(periodToStartDate);
            startDate = zonedDateTime.format(df);
        }
        LinkedHashSet<TrackerState> trackerStateList = trackerMonitoringClient.getTrackers(startDate,endDate,customerId,trackerType,order,status);

        try {
            Sheet trackerStateSheet = documentsService.generateExcellSheet(trackerStateList);
            FileOutputStream fos = new FileOutputStream("Tracker States.xls");
            trackerStateSheet.getWorkbook().write(fos);
            fos.close();
            FileDataSource source = new FileDataSource("Tracker States.xls");
            sendMail(mailProperties.getUsername(), receiverMail, subject, body, source);
            if (alertFrequency.isPresent() && alertFrequency.get().equals("once") && Optional.ofNullable(jobDataMap.getString("scheduleId")).isPresent()) {
                String scheduleId = Optional.ofNullable(jobDataMap.getString("scheduleId")).get();
                scheduleService.deleteSchedule(scheduleId);

            }

        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }

    }

    private void sendMail(String senderMail, String receiverMail, String subject, String body, FileDataSource trackerStateSheet) {

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(senderMail);
            messageHelper.setTo(receiverMail);
            messageHelper.addAttachment("Tracker States.xlsx", trackerStateSheet);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
