package cz.moneta;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class ExcelWriter {
    private final String filename;
    private WritableWorkbook workbook;

    public ExcelWriter(String filename) {
        this.filename = filename;
        try {
            workbook = Workbook.createWorkbook(Paths.get(filename).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToXlsFile(Map<String, List<List<String>>> data) {
        try {
            for (Map.Entry<String, List<List<String>>> sheet : data.entrySet()) {
                WritableSheet writableSheet = workbook.createSheet(sheet.getKey(), 0);

                addCells(writableSheet, sheet.getValue());
                formatCells(writableSheet);
            }
            workbook.write();
            workbook.close();
            System.out.println("File " + filename + " created.");
        } catch (WriteException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addCells(WritableSheet sheet, List<List<String>> data) {
        for (int row = 0; row < data.size(); row++) {
            for (int col = 0; col < data.get(row).size(); col++) {
                Label label = new Label(col, row, data.get(row).get(col));
                try {
                    sheet.addCell(label);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void formatCells(WritableSheet sheet) {
        try {
            for (int col = 0; col < sheet.getColumns(); col++) {
                WritableCell writableCell = sheet.getWritableCell(col, 0);
                WritableFont font = new WritableFont(WritableWorkbook.ARIAL_10_PT);
                font.setBoldStyle(WritableFont.BOLD);
                WritableCellFormat newFormat = new WritableCellFormat(font);
                newFormat.setBackground(Colour.LIGHT_GREEN);
                writableCell.setCellFormat(newFormat);
                sheet.setColumnView(col, 25);
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

}