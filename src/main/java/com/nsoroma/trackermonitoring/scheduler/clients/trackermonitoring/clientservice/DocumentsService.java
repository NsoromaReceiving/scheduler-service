package com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.clientservice;

import com.nsoroma.trackermonitoring.scheduler.clients.trackermonitoring.model.TrackerState;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class DocumentsService {

    private static String[] inHouseSheetColumns = {"Label", "Customer Name", "Customer Id", "*Last Gsm Update", "Last Gps Update",
            "Tracker Id", "Imei No.", "Model", "Phone Number", "Connection Status",
            "Tariff End Date", "Last Gps Signal Level", "Last Gps Latitude",
            "Last Gps Longitude", "Last Battery Level", "Last Gsm Signal Level", "Gsm NetworkName", "Server"};



    public Sheet generateInHouseExcelSheet(Set<TrackerState> trackerStates, String name) throws IOException {
            Workbook workbook = new XSSFWorkbook();

            XSSFCellStyle batteryStyle = (XSSFCellStyle) workbook.createCellStyle();
            batteryStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            batteryStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle gsmNetworkStyle = (XSSFCellStyle) workbook.createCellStyle();
        gsmNetworkStyle.setFillForegroundColor(IndexedColors.INDIGO.getIndex());
        gsmNetworkStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Sheet sheet = workbook.createSheet(name);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.DARK_GREEN.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < inHouseSheetColumns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(inHouseSheetColumns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNumber = 1;

            for (TrackerState trackerState : trackerStates) {
                Row row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(trackerState.getLabel());
                row.createCell(1).setCellValue(trackerState.getCustomerName());
                row.createCell(2).setCellValue(trackerState.getCustomerId());
                row.createCell(3).setCellValue(trackerState.getLastGsmUpdate());
                row.createCell(4).setCellValue(trackerState.getLastGpsUpdate());
                row.createCell(5).setCellValue(trackerState.getTrackerId());
                row.createCell(6).setCellValue(trackerState.getImei());
                row.createCell(7).setCellValue(trackerState.getModel());
                row.createCell(8).setCellValue(trackerState.getPhoneNumber());
                row.createCell(9).setCellValue(trackerState.getConnectionStatus());
                row.createCell(10).setCellValue(trackerState.getTariffEndDate());
                row.createCell(11).setCellValue(trackerState.getLastGpsSignalLevel());
                row.createCell(12).setCellValue(trackerState.getLastGpsLatitude());
                row.createCell(13).setCellValue(trackerState.getLastGpsLongitude());
                row.createCell(14).setCellValue(trackerState.getLastBatteryLevel());
                try {
                    if(Integer.parseInt(trackerState.getLastBatteryLevel()) < 30 ) {
                        row.getCell(14).setCellStyle(batteryStyle);
                    }
                }catch (NumberFormatException | NullPointerException e) {
                    continue;
                }
                row.createCell(15).setCellValue(trackerState.getGsmSignalLevel());
                try {
                    if(Double.parseDouble(trackerState.getGsmSignalLevel()) < 20 ) {
                        row.getCell(15).setCellStyle(gsmNetworkStyle);
                    }
                }catch (NumberFormatException | NullPointerException e) {
                    continue;
                }
                row.createCell(16).setCellValue(trackerState.getGsmNetworkName());
                row.createCell(17).setCellValue(trackerState.getServer());
            }

            for (int i = 0; i < inHouseSheetColumns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return sheet;
    }






}
