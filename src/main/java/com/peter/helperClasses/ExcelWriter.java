package com.peter.helperClasses;

import com.peter.model.ExtendedStatTableEntry;
import com.peter.model.StatTableEntry;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;


/**
 * Created by KungPeter on 2016-03-20.
 */
public class ExcelWriter {


    // Handle aa
    public static void writeToExcelFile(File selectedFile, ObservableList<StatTableEntry> passPerDayList, ObservableList<StatTableEntry> passPerRoundList,
                                        ObservableList<ExtendedStatTableEntry> noOfRoundsWithMoreThanXList, Label numberOfTravelsForRoundLabel,
                                        Label numberOfRoundsLabel1, Label selectedStatslabel, ObservableList<Object> labelList, LocalDate startDate,
                                        LocalDate endDate) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(startDate.toString() + " - " + endDate.toString());

        // Bold fonts for headline
        final HSSFFont BOLD_FONT = workbook.createFont();
        BOLD_FONT.setBold(true);

        int rowCount = 0;

        // Overall headline
        HSSFRow titleRow = sheet.createRow(rowCount++);
        HSSFCell titleCell = titleRow.createCell(0);
        //titleCell.getCellStyle().setFont(BOLD_FONT);
        titleCell.setCellValue(selectedStatslabel.getText());

        rowCount += 2;
        HSSFRow passPerDayTitleRow = sheet.createRow(rowCount++);
        HSSFCell passPerDayTitleCell = passPerDayTitleRow.createCell(0);
        passPerDayTitleCell.setCellValue("Antal påstigande per dag under perioden");

        // Print passPerDayList
        rowCount += 2;
        for (StatTableEntry entry : passPerDayList) {
            HSSFRow row = sheet.createRow(rowCount++);
            HSSFCell dateCell = row.createCell(0);
            dateCell.setCellValue(entry.getDay());

            HSSFCell numOfPassCell = row.createCell(1);
            numOfPassCell.setCellValue(entry.getNumOfPass());
        }


        rowCount += 4;
        HSSFRow passPerRoundTitleRow = sheet.createRow(rowCount);
        HSSFCell passPerRoundTitleCell = passPerRoundTitleRow.createCell(0);
        passPerRoundTitleCell.setCellValue("Antal påstigande för tur " + numberOfTravelsForRoundLabel.getText().trim() + " för perioden");

        // Print passPerRoundList
        rowCount += 2;

        for (StatTableEntry entry : passPerRoundList) {
            HSSFRow row = sheet.createRow(rowCount++);
            HSSFCell dateCell = row.createCell(0);
            dateCell.setCellValue(entry.getDay());

            HSSFCell dataCell = row.createCell(1);
            dataCell.setCellValue(entry.getNumOfPass());

        }

        rowCount += 4;
        HSSFRow roundsWithMoreThaxXTitleRow = sheet.createRow(rowCount);
        HSSFCell roundsWithMoreThaxXTitleCell = roundsWithMoreThaxXTitleRow.createCell(0);
        roundsWithMoreThaxXTitleCell.setCellValue("Turer med mer än " + numberOfRoundsLabel1.getText() + " antal påstigande");

        // Preint roundsWithMoreThanXList
        rowCount += 2;

        for (ExtendedStatTableEntry entry : noOfRoundsWithMoreThanXList) {
            HSSFRow row = sheet.createRow(rowCount++);
            HSSFCell dateCell = row.createCell(0);
            dateCell.setCellValue(entry.getDay());

            HSSFCell roundCell = row.createCell(1);
            roundCell.setCellValue(entry.getRound());

            HSSFCell numPassCell = row.createCell(2);
            numPassCell.setCellValue(entry.getNumOfPass());
        }

        rowCount += 4;

        for (int i = 0; i < labelList.size() / 2; i++) {
            if (labelList.get(i) instanceof Label) {
                if (labelList.get(i + (labelList.size() / 2)) instanceof Label) {
                    Label title = (Label) labelList.get(i);
                    Label data = (Label) labelList.get(i + (labelList.size() / 2));

                    HSSFRow row = sheet.createRow(rowCount++);
                    HSSFCell destinationCell = row.createCell(0);
                    destinationCell.setCellValue(title.getText());

                    HSSFCell dataCell = row.createCell(1);
                    dataCell.setCellValue(data.getText());

                }
            }
        }

        FileOutputStream fous = null;

        try {
            fous = new FileOutputStream(selectedFile, false);
            workbook.write(fous);
            fous.close();
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
