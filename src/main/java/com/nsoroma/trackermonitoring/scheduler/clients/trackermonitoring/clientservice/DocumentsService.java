package com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.clientservice;

import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.model.TrackerState;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class DocumentsService {

    private static String[] columns = {"Label", "Customer Name", "Customer Id", "Last Gps Update",
            "Tracker Id", "Imei No.", "Model", "Phone Number", "Connection Status",
            "Tariff End Date", "Last Gps Signal Level", "Last Gps Latitude",
            "Last Gps Longitude", "Last Battery Level", "Last Gsm Signal Level", "Gsm NetworkName"};



    public Sheet generateExcellSheet(Set<TrackerState> trackerStates) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tracker States");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.DARK_GREEN.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNumber = 1;

            for (TrackerState trackerState : trackerStates) {
                Row row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(trackerState.getLabel());
                row.createCell(1).setCellValue(trackerState.getCustomerName());
                row.createCell(2).setCellValue(trackerState.getCustomerId());
                row.createCell(3).setCellValue(trackerState.getLastGpsUpdate());
                row.createCell(4).setCellValue(trackerState.getTrackerId());
                row.createCell(5).setCellValue(trackerState.getImei());
                row.createCell(6).setCellValue(trackerState.getModel());
                row.createCell(7).setCellValue(trackerState.getPhoneNumber());
                row.createCell(8).setCellValue(trackerState.getConnectionStatus());
                row.createCell(9).setCellValue(trackerState.getTariffEndDate());
                row.createCell(10).setCellValue(trackerState.getLastGpsSignalLevel());
                row.createCell(11).setCellValue(trackerState.getLastGpsLatitude());
                row.createCell(12).setCellValue(trackerState.getLastGpsLongitude());
                row.createCell(13).setCellValue(trackerState.getLastBatteryLevel());
                row.createCell(14).setCellValue(trackerState.getGsmSignalLevel());
                row.createCell(15).setCellValue(trackerState.getGsmNetworkName());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return sheet;
        }
    }






}
