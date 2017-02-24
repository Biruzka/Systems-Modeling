import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by biruzka on 19.02.17.
 */
public class Statistic {

    private Map<Integer,Double> statisticList;
    private int n = 0;
    private double CAP;
    private Map<Integer,Double> forecast;
    private double average;
    private double standardDeviation;
    private Map<Integer,Double> irvinList;
    private double[] straight;
    private Map<Integer,Double> deviationsList;

    public Statistic() {
        this.statisticList = new HashMap<Integer,Double>();
        this.forecast = new HashMap<Integer,Double>();
        this.irvinList = new HashMap<Integer,Double>();
        this.deviationsList = new HashMap<Integer,Double>();
        this.straight = new double[2];

    }

    public void put (int key, double value) {
        statisticList.put(key,value);
        n = statisticList.size();
    }

    public int getN () {
        return n;
    }

    public double getValue (int key) {
        return this.statisticList.get(key);
    }

    public void Count(){
        this.CAP = CountMethods.CAP(this.getValue(this.n), this.getValue(1), this.n);
        this.forecast = CountMethods.forecast(statisticList,this.CAP);
        this.average = CountMethods.average(statisticList);
        this.standardDeviation = CountMethods.standardDeviation(statisticList);
        this.irvinList = CountMethods.irvin(statisticList);
        //МНК для ЛПР
        this.straight = CountMethods.LeastSquareMethod(statisticList);
        //отклонения
        this.deviationsList = CountMethods.deviations(statisticList,straight);

    }

    public void toShow() {
        System.out.println("STATISTIC");
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("CAP = " + this.CAP);
        System.out.println("FORECAST");
        for (Map.Entry<Integer,Double> pair : forecast.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("AVERAGE = " + this.average);
        System.out.println("SD = " + this.standardDeviation);
        System.out.println("IRVIN LIST");
        for (Map.Entry<Integer,Double> pair : irvinList.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
        System.out.println("STRAIGHT: а =" + this.straight[0]+" и ="+this.straight[1]);
        System.out.println("Deviations");
        for (Map.Entry<Integer,Double> pair : deviationsList.entrySet()) {
            System.out.print(pair.getKey() + "\t");
            System.out.println(pair.getValue());
        }
    }
}
