import Jama.Matrix;
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
 * Created by biruzka on 23.02.17.
 */
public class StatisticModel {
    private Map<Integer,Double> Factor0X;
    private Map<Integer,Double> Factor1X;
    private Map<Integer,Double> Factor2X;
    private Map<Integer,Double> Factor3X;
    private Map<Integer,Double> Y;
    private int n = 0;

    private double[][] FactorsX;
    private double[] FactualResultsY;


    public StatisticModel() {
        Factor0X = new HashMap<Integer,Double>();
        Factor1X = new HashMap<Integer,Double>();
        Factor2X = new HashMap<Integer,Double>();
        Factor3X = new HashMap<Integer,Double>();
        Y = new HashMap<Integer,Double>();
    }

    public void importStatistic2() {
        FactorsX = new double[][]{{2,4,5,6,8},{4,7,3,2,1},{2,40,8,90,1},{4,5,60,7,80}};
        FactualResultsY = new double[] { 20 , 40, 570,760,800};
        this.n = 5;
    }

    public void importStatistic() {
        try {
            File excel = new File("ukraina-statistic.xlsx");
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            Iterator<Row> itr = sheet.iterator();
            // Iterating over Excel file in Java

            int counter = 0;
            double value;
            while (itr.hasNext()) {
                Row row = itr.next();
                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();

                Cell cell = cellIterator.next();
                value = cell.getNumericCellValue();
                this.Factor0X.put(counter, value);

                cell = cellIterator.next();
                value = cell.getNumericCellValue();
                this.Factor1X.put(counter, value);

                cell = cellIterator.next();
                value = cell.getNumericCellValue();
                this.Factor2X.put(counter, value);

                cell = cellIterator.next();
                value = cell.getNumericCellValue();
                this.Factor3X.put(counter, value);

                cell = cellIterator.next();
                value = cell.getNumericCellValue();
                this.Y.put(counter, value);


                counter++;
            }
            this.n=this.Y.size();
            book.close();
            fis.close();

        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        FactorsX = new double[4][this.n];
        FactualResultsY = new double[this.n];

        for (Map.Entry<Integer,Double> pair : Y.entrySet()) {
            FactualResultsY[pair.getKey()]=pair.getValue();
        }
            for (int i=0; i<this.n; i++){
                FactorsX[0][i] = Factor0X.get(i);
                FactorsX[1][i] = Factor1X.get(i);
                FactorsX[2][i] = Factor2X.get(i);
                FactorsX[3][i] = Factor3X.get(i);
            }
    }



    public double[] getModel(double[][]factors, double[]factualResults){
        double[] z= new double[3];
        Jama.Matrix A1=new Jama.Matrix(factors);
        A1.print(10, 2);
        Jama.Matrix B1=A1.transpose();
        Jama.Matrix F1=A1.times(B1);
        Jama.Matrix F4=F1.inverse();
        Jama.Matrix F2=F4.times(A1);
        Jama.Matrix C=new Jama.Matrix(factualResults,this.n);
        Jama.Matrix F3=F2.times(C);
        z=F3.getColumnPackedCopy();
        for (int i = 0; i < 3; i++) {
            System.out.println("z[" + i + "]=" + z[i]);
        }
        return z;
    }

    public double R(double[][]ff, double[]yy, double[] z){
        double r=0, S1=0, S2=0, S3=0;
        double[] u1=new double[this.n];
        double[] u2=new double[this.n];
        for (int i = 0; i < this.n; i++)
        {S3+=yy[i];
            u1[i]=0;}
        S3=S3/5;
        for (int m = 0; m < this.n; m++){
            u1[m]=(z[0]+z[1]*ff[1][m]+z[2]*ff[2][m]-yy[m])*(z[0]+z[1]*ff[1][m]+z[2]*ff[2][m]-yy[m]);
            S1+=u1[m];
            u2[m]=(S3-yy[m])*(S3-yy[m]);
            S2+=u2[m];
        }
        r=1-S1/S2;
        System.out.println("r="+r);
        return r;
    }



    public void count(){
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                System.out.println("i="+i);
                System.out.println("j="+j);
                for (int k = 0; k < this.n; k++) {
                    f[1][k]=FactorsX[i][k];
                    f[2][k]=FactorsX[j][k];
                    System.out.println("f[1]["+k+"]="+f[1][k]);
                    System.out.println("f[2]["+k+"]="+f[2][k]);
                }
                double[] r = this.getModel(f, FactualResultsY);
                this.R(f, FactualResultsY, r);
            }
        }
    }

    public void toShow() {
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : Factor0X.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : Factor1X.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : Factor2X.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : Factor3X.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : Y.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }

    }
}
