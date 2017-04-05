package trade;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class ActionService {
    //Для конкретного дня - возрастала ли акция и не достиг ли день среднего значения
    //Если акция возрастала u == 1, среднее значение точек в возрастающем канале, сколько прошло дней

    public boolean isDaySuitable(double average, double increasing, double countDays){
        boolean check = false;
        if (countDays<average && increasing==1){
            check = true;
        }
        return check;
    }

    public double[][] importData(){
        double[][] data = new double[254][9];
        int counter = 0;
        int j = 0;
        int n = 0;
        try {
            File excel = new File("actionsTrend.xlsx");
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            Iterator<Row> i = sheet.iterator();
            double value;
            while (i.hasNext()&&counter!=254) {
                Row row = i.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell key = cellIterator.next();
                    value = key.getNumericCellValue();
                    data[counter][j] = value;
                    j++;
                }
                n = j;
                counter++;
                j = 0;

            }

            book.close();
            fis.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return data;
    }
}
