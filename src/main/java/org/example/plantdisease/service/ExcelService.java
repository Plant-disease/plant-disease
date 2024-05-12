package org.example.plantdisease.service;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.GetLeafDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelService {
    @Value("${app.files.excel}")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public static final String[] COLUMN_1 = {"№", "Meva nomi", "Kasallik turi", "Barg holati",
            "leafArea","elongation","compactness","circularity","textureEnergy","leafPerimeter","leafAspectRatio","fractalDimension","totalVeinCount","averageVeinWidth","averageVeinLength"};

    public ByteArrayOutputStream generateExcel(String[] columns, List<Map<Integer, String>> object, String listName) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Workbook workbook = new XSSFWorkbook();
            CreationHelper creationHelper = workbook.getCreationHelper(); //workbookni xamma formatlarini qo'llab quvvatlaydi
            Sheet sheet = workbook.createSheet(listName);

            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontHeightInPoints((short) 12);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);

            Row row = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columns[i]);
            }

            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            CellStyle bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);

            int rowNum = 1;
            for (Map<Integer, String> map : object) {
                Row row1 = sheet.createRow(rowNum++);
                map.forEach((integer, value) -> {
                    Cell cell = row1.createCell(integer);
                    cell.setCellValue(value);
                    cell.setCellStyle(bodyCellStyle);
                });
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
            throw RestException.restThrow("CANNOT_BUILD_EXCEL_FILE");
        }
    }

    public List<Map<Integer, String>> makeExcelDataForLeafMeasurement(List<GetLeafDataDTO> getLeafDataDTOList) {

        List<Map<Integer, String>> object = new ArrayList<>();

        for (GetLeafDataDTO getLeafDataDTO : getLeafDataDTOList) {
            Map<Integer, String> forObject = new HashMap<>();
            forObject.put(0, String.valueOf(getLeafDataDTO.getId()));
            forObject.put(1, getLeafDataDTO.getFruit());
            forObject.put(2, getLeafDataDTO.getDisease() );
            forObject.put(3, getLeafDataDTO.getLeafCondition());
            forObject.put(4, String.valueOf(getLeafDataDTO.getLeafArea()));
            forObject.put(5, String.valueOf(getLeafDataDTO.getElongation()));
            forObject.put(6, String.valueOf(getLeafDataDTO.getCompactness()));
            forObject.put(7, String.valueOf(getLeafDataDTO.getCircularity()));
            forObject.put(8, String.valueOf(getLeafDataDTO.getTextureEnergy()));
            forObject.put(9, String.valueOf(getLeafDataDTO.getLeafPerimeter()));
            forObject.put(10, String.valueOf(getLeafDataDTO.getLeafAspectRatio()));
            forObject.put(11, String.valueOf(getLeafDataDTO.getFractalDimension()));
            forObject.put(12, String.valueOf(getLeafDataDTO.getTotalVeinCount() ));
            forObject.put(13, String.valueOf(getLeafDataDTO.getAverageVeinWidth()));
            forObject.put(14, String.valueOf(getLeafDataDTO.getAverageVeinLength()));
            object.add(forObject);
        }
        return object;
    }


    public CellStyle createBold(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }


    //PAPKA TOPILMASA OCHISH UCHUN
    public void createFolder() {
        File file = new File(filePath);
        file.mkdirs();
    }


    public CellStyle getCellStyle(Map<Integer,CellStyle> cellStyleMap,Double passingScore,String score){

        if (passingScore==null)
            return cellStyleMap.get(1);
        if (score.equals(""))
            return cellStyleMap.get(1);
        try{
            double _score = Double.parseDouble(score);
            if (_score>=passingScore)
                return cellStyleMap.get(2);
        } catch (Exception e) {
            return cellStyleMap.get(1);
        }

        return cellStyleMap.get(1);
    }

    public String getDate(Long number) {

        if (number==null)
            return "";

        Date date=new Date(number);
        DateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public String[] getColumnName(int size){

        String[] arrayColumn=new String[(size+15)];
        arrayColumn[0]="№";
        arrayColumn[1]="FIO";
        arrayColumn[2]="Telefon raqam";
        arrayColumn[3]="Davomat";
        int key=4;
        for (int i = 1; i <= size; i++) {
            arrayColumn[key++]="Q"+i;
        }
        arrayColumn[key++]="Score";
        arrayColumn[key++]="Ticket";
        arrayColumn[key++]="Examiner";
        arrayColumn[key++]="Invoice Raqami";
        arrayColumn[key++]="Invoice Status";
        arrayColumn[key++]="Paid Inv Amount";
        arrayColumn[key++]="Status";
        arrayColumn[key++]="Failed TimeTable";
        arrayColumn[key++]="Group Type";
        arrayColumn[key++]="Specialization";
        arrayColumn[key++]="Module";
        return arrayColumn;
    }


}
