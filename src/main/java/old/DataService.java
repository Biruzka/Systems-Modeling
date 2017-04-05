package old;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataService {

    double[][] data = new double[254][9];

    public double[][] importData(){
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
		/*for (int i=0;i<counter;i++){
		for (int jj=0;jj<n;jj++){

				System.out.print(data[i][jj]+" ");
			}
			System.out.println("");
		}*/
        return data;
    }
}
