package chanals;

import com.sun.org.glassfish.external.statistics.Statistic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by biruzka on 10.03.17.
 */
public class Stocks {
    private Map<Integer,double[]> statisticList;
    int count;
    int n;

    public Stocks(int count) {
        this.count = count;
        this.n=0;
        this.statisticList = new HashMap<Integer,double[]>();
    }

    public double[] getStockList(int numbStock) {
        double[] d = new double[this.count];
        double[] stock = new double[this.n];;
        for (int i = 0; i < this.n; i++) {
            d = statisticList.get(i);
            stock[i] = d[numbStock];
        }
        return stock;
    }

    public void read(String file) {
        try {
            File excel = new File(file);
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            Iterator<Row> itr = sheet.iterator();
            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();
                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                double[] st = new double[this.count];
                Cell cell;
                for (int i = 0; i < this.count; i++) {
                    cell = cellIterator.next();
                    st[i] =  cell.getNumericCellValue();
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                System.out.println(""+this.n);
                statisticList.put(this.n,st);
                this.n++;
            }
            System.out.println(this.n);

            book.close();
            fis.close();

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public int getN() {
        return n;
    }
}
