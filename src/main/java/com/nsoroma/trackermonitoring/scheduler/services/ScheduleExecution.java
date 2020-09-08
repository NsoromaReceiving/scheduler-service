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
import java.util.stream.Collectors;


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
        String scheduleId = jobDataMap.getString("scheduleId");
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
        String scheduleType = jobDataMap.getString("scheduleType");

        //time frame
        String timeFrame = jobDataMap.getString("timeFrame");
        if(timeFrame != null && !timeFrame.equals("")) {
            int period = Integer.parseInt(timeFrame);
            ZonedDateTime zonedStartDate = ZonedDateTime.now(zoneId);
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

        LinkedHashSet<TrackerState> trackerStatesBatteryLevel = trackerStateList.parallelStream().filter(trackerState -> {
            try {
                return Integer.parseInt(trackerState.getLastBatteryLevel()) > 15;
            } catch (NumberFormatException e) {
                return false;
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));

        if (scheduleType.equals("INHOUSE")) {
            writeToExcelSheets(scheduleId, subject, body, receiverMail, alertFrequency, trackerStateList, trackerStatesBatteryLevel);
        } else {
            designClientMail(scheduleId, subject, receiverMail, alertFrequency, trackerStateList);
        }


    }

    private void designClientMail(String scheduleId, String subject, String receiverMail, Optional<String> alertFrequency, LinkedHashSet<TrackerState> trackerStateList) {
        StringBuilder mailBody = new StringBuilder();

        mailBody.append("Dear Client,");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("The column below  contains a list of your trackers that have not updated on the tracking server in the last 24 hours. \n" +
                "Kindly let us know if each vehicle is");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("a. actively moving");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("b. parked for a long period or has its battery disconnected");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("c. at the workshop.");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("<br>");

        mailBody.append("<head><style>table {width:100%;} th, td {padding: 5px;} th { text-align: left;}</style></head>");
        mailBody.append("<table><tr><th>Label</th><th>*Last Gsm Update</th><th>Last Gps Update</th><th>Last Battery Level</th></tr>");
        for (TrackerState trackerState: trackerStateList) {
            mailBody.append("<tr><td>").append(trackerState.getLabel()).append("</td>");
            mailBody.append("<td>").append(trackerState.getLastGsmUpdate()).append("</td>");
            mailBody.append("<td>").append(trackerState.getLastGpsUpdate()).append("</td>");
            mailBody.append("<td>").append(trackerState.getLastBatteryLevel()).append("</td>");
            mailBody.append("</tr>");
        }
        mailBody.append("</table>");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("Kindly respond to this message by sending an email to technicalservices@nsoromagps.com. ");
        mailBody.append("<br>");
        mailBody.append("<br>");
        mailBody.append("In the case of an active vehicle, please be sure it has moved for at least 5-10 minutes today and is still persistent in not updating so that we would know where to begin troubleshooting without asking you to repeat that step. Thank you. ");

        sendMailWithoutAttachment(mailProperties.getUsername(), receiverMail, subject, mailBody.toString());

        try {
            checkAndDeleteSchedule(scheduleId, alertFrequency);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void writeToExcelSheets(String scheduleId, String subject, String body, String receiverMail, Optional<String> alertFrequency, LinkedHashSet<TrackerState> trackerStateList, LinkedHashSet<TrackerState> trackerStatesBatteryLevel) {
        try {
            Sheet trackerStateSheet = documentsService.generateInHouseExcelSheet(trackerStateList, scheduleId);
            FileOutputStream fos = new FileOutputStream(scheduleId + ".xls");
            trackerStateSheet.getWorkbook().write(fos);
            fos.close();

            Sheet trackerStateSheet2 = documentsService.generateInHouseExcelSheet(trackerStatesBatteryLevel, scheduleId + "cutoff");
            FileOutputStream fos2 = new FileOutputStream(scheduleId + " Cut-Off.xls");
            trackerStateSheet2.getWorkbook().write(fos2);
            fos2.close();

            FileDataSource source = new FileDataSource(scheduleId + ".xls");
            FileDataSource source2 = new FileDataSource(scheduleId + " Cut-Off.xls");
            subject  = subject.concat(" : Total {" + trackerStateList.size() + "}"); //providing total number
            sendMailWithExcelAttachment(mailProperties.getUsername(), receiverMail, subject, body, source, source2);
            checkAndDeleteSchedule(scheduleId, alertFrequency);

        } catch (IOException | SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void checkAndDeleteSchedule(String scheduleId, Optional<String> alertFrequency) throws SchedulerException {
        if (alertFrequency.isPresent() && alertFrequency.get().equals("once")) {
            scheduleService.deleteSchedule(scheduleId);
        }
    }

    private void sendMailWithExcelAttachment(String senderMail, String receiverMail, String subject, String body, FileDataSource trackerStateSheet, FileDataSource trackerStateSheetCutoff) {

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(senderMail);
            messageHelper.setTo(receiverMail);
            messageHelper.addAttachment("Tracker States.xlsx", trackerStateSheet);
            messageHelper.addAttachment("Tracker States Cutoff.xlsx", trackerStateSheetCutoff);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendMailWithoutAttachment(String senderMail, String receiverMail, String subject, String body) {

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(senderMail);
            messageHelper.setTo(receiverMail);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
