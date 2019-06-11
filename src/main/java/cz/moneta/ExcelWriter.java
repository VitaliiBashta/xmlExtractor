package cz.moneta;

import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelWriter {
    public static void saveToXlsFile(String filename, Map<String, List<List<String>>> data) {
        try {
            WritableWorkbook workbook = jxl.Workbook.createWorkbook(new File(filename));

            for (Map.Entry<String, List<List<String>>> sheet : data.entrySet()) {
                List<List<String>> cells = sheet.getValue();
                List<WritableCell> writableCells = new ArrayList<>();

                for (int row = 0; row < cells.size(); row++) {
                    for (int col = 0; col < cells.get(row).size(); col++) {
                        writableCells.add(new Label(col, row, cells.get(row).get(col)));
                    }
                }


                WritableSheet workbookSheet = workbook.createSheet(sheet.getKey(), 0);

                for (WritableCell cell : writableCells) {
                    workbookSheet.addCell(cell);
                }

                // format cells width
                for (int i = 0; i < cells.size(); i++) {
                    workbookSheet.setColumnView(i, 25);
                }

            }
            workbook.write();
            workbook.close();
            System.out.println("File " + filename + " created.");
        } catch (WriteException | IOException e) {
            e.printStackTrace();
        }

    }
}