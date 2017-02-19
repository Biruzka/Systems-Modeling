import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by biruzka on 19.02.17.
 */
public class ReaderWriter {
    public Statistic read() {
        Statistic statistic = new Statistic();
        try {
            File excel = new File("uk-prom.xlsx");
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            Iterator<Row> itr = sheet.iterator();
            statistic = new Statistic();
            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();
                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                int key;
                double value;
                Cell cell = cellIterator.next();
//                System.out.print(cell.getNumericCellValue() + "\t");
                key = (int) cell.getNumericCellValue();
                cell = cellIterator.next();
//                System.out.print(cell.getNumericCellValue() + "\t");
                value = cell.getNumericCellValue();
//                System.out.println("");
                statistic.put(key, value);
            }

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return statistic;
    }
}


//            Counting

            // writing data into XLSX file
//            Map<String, Object[]> newData = new HashMap<String, Object[]>();
//            newData.put("7", new Object[] { 7d, "Sonya", "75K", "SALES", "Rupert" });
//            newData.put("8", new Object[] { 8d, "Kris", "85K", "SALES", "Rupert" });
//            newData.put("9", new Object[] { 9d, "Dave", "90K", "SALES", "Rupert" });
//
//            Set<String> newRows = newData.keySet();
//            int rownum = sheet.getLastRowNum();
//
//            for (String key : newRows) { Row row = sheet.createRow(rownum++);
//                Object[] objArr = newData.get(key);
//                int cellnum = 0;
//                for (Object obj : objArr)
//                {
//                    Cell cell = row.createCell(cellnum++);
//                    if (obj instanceof String)
//                    {
//                        cell.setCellValue((String) obj);
//                    }
//                    else if (obj instanceof Boolean)
//                    {
//                        cell.setCellValue((Boolean) obj);
//                    } else if (obj instanceof Date)
//                    {
//                        cell.setCellValue((Date) obj);
//                    } else if (obj instanceof Double)
//                    {
//                        cell.setCellValue((Double) obj);
//                    }
//                }
//            }

            // open an OutputStream to save written data into Excel file

//            FileOutputStream os = new FileOutputStream(excel);
//            book.write(os);
//            System.out.println("Writing on Excel file Finished ...");
//            // Close workbook, OutputStream and Excel file to prevent leak
//            os.close();
//            book.close();
//            fis.close();




