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
        System.out.println("executing schedule");

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String receiverMail = jobDataMap.getString("email");
        String startDate = jobDataMap.getString("startDate");
        String endDate = jobDataMap.getString("endDate");
        String trackerType = jobDataMap.getString("trackerType");
        String customerId = jobDataMap.getString("customerId");
        String status = jobDataMap.getString("status");
        String order = null;
        Optional<String> alertFrequency = Optional.ofNullable(jobDataMap.getString("alertFrequency"));

        LinkedHashSet<TrackerState> trackerStateList = trackerMonitoringClient.getTrackers(startDate,endDate,customerId,trackerType,order,status);

        try {
            Sheet trackerStateSheet = documentsService.generateExcellSheet(trackerStateList);
            FileOutputStream fos = new FileOutputStream("Tracker States.xls");
            trackerStateSheet.getWorkbook().write(fos);
            fos.close();
            FileDataSource source = new FileDataSource("Tracker States.xls");
            sendMail(mailProperties.getUsername(), receiverMail, subject, body, source);
            if(alertFrequency.isPresent()){
                if (alertFrequency.get().equals("once")) {
                    String scheduleId = Optional.ofNullable(jobDataMap.getString("scheduleId")).get();
                    scheduleService.deleteSchedule(scheduleId);
                }
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
