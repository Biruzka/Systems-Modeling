import Jama.Matrix;
import org.apache.poi.ss.formula.functions.Match;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
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
    private int countFactors = 0;


    private double[][] FactorsX;
    private double[] FactualResultsY;
    private double[] ModelResultsY;
    private double[] RegressionResidueE;
    private double dw = 0;
    private double R;
    private boolean heteroscedasticity;

    private double[] bestModelResultsY;
    private double bestModelR;
    private double bestModelDW;
    private boolean bestModelHeteroscedasticity;
    private int bestModelFactor1;
    private int getBestModelFactor2;





    public StatisticModel() {
        this.Factor0X = new HashMap<Integer,Double>();
        this.Factor1X = new HashMap<Integer,Double>();
        this.Factor2X = new HashMap<Integer,Double>();
        this.Factor3X = new HashMap<Integer,Double>();
        this.Y = new HashMap<Integer,Double>();
    }

//    public void importStatistic2() {
//        FactorsX = new double[][]{{2,4,5,6,8},{4,7,3,2,1},{2,40,8,90,1},{4,5,60,7,80}};
//        FactualResultsY = new double[] { 20 , 40, 570,760,800};
//        this.n = 5;
//    }

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

        this.FactorsX = new double[4][this.n];
        this.FactualResultsY = new double[this.n];
        this.ModelResultsY = new double[this.n];
        this.RegressionResidueE = new double[this.n];
        this.bestModelResultsY = new double[this.n];

        for (Map.Entry<Integer,Double> pair : Y.entrySet()) {
            this.FactualResultsY[pair.getKey()]=pair.getValue();
        }
            for (int i=0; i<this.n; i++){
                this.FactorsX[0][i] = Factor0X.get(i);
                this.FactorsX[1][i] = Factor1X.get(i);
                this.FactorsX[2][i] = Factor2X.get(i);
                this.FactorsX[3][i] = Factor3X.get(i);
            }
    }


    public void importStatisticYToX(int n, int countFactors, double[] factualResultsY, double[][] factors) {

        this.n = n;
        this.countFactors = countFactors;
        this.FactorsX = new double[countFactors][this.n];
        this.FactorsX = factors;
        this.FactualResultsY = new double[this.n];
        this.FactualResultsY = factualResultsY;
        this.ModelResultsY = new double[this.n];
        this.RegressionResidueE = new double[this.n];
        this.bestModelResultsY = new double[this.n];

    }



    public double[] getModel(double[][]factors, double[]factualResults, int n){
        double[] z= new double[3];
        Jama.Matrix A1=new Jama.Matrix(factors);
        A1.print(10, 2);
        Jama.Matrix B1=A1.transpose();
        Jama.Matrix F1=A1.times(B1);
        Jama.Matrix F4=F1.inverse();
        Jama.Matrix F2=F4.times(A1);
        Jama.Matrix C=new Jama.Matrix(factualResults,n);
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
        S3=S3/this.n;
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


    public double countDW(double[] E, int n){
        double dw = 0;
        double numerator = 0;
        double denominator = 1;

        for (int i = 1; i < n; i++) {
            numerator+=(E[i]-E[i-1])*(E[i]-E[i-1]);
        }

        for (int i = 0; i < n; i++) {
            denominator+=E[i]*E[i];
        }

        dw = numerator/denominator;
        System.out.println("DW!!!!!!!!!!!= " + dw);
        return dw;
    }


    public double[] countModelY1(double[] z, double[][]x, int n){
        double[] ModelY = new double[n];;

        System.out.println("modelResults Y");
        for (int yy = 0; yy < n; yy++) {
            ModelY[yy]=z[0]+z[1]*x[1][yy]+z[2]*x[2][yy];
            System.out.println(  ModelY[yy]);
        }
        return ModelY;
    }

    public double[]  countRegressionResidueE(double[] FactualY, double[] ModelY, int n) {
        double[] RRE = new double[n];
        System.out.println("RegressionResidueE");
        for (int e = 0; e < n; e++) {
            RRE[e]=FactualY[e]-ModelY[e];
            System.out.println( RRE[e]);
        }
        return RRE;
    }



//    public double EForGolCv () {
//
//    }

    public boolean countGolfildCvant(double[] FactualY, double[] ModelY, double[][]X, int n) {
        boolean heteroscedasticity;
        double Fkr = 1.87; //по таблице - при n=96 a=0.05 m=2 k=n/3=32 v=k-m-1(32-2-1)=29;
        double S1=0;
        double S3=0;
        double F=0;
        int k = (int)n/3; //96/3 = 32

        double[] FactualYGC = new double[n];
        FactualYGC = FactualY;
        double[] ModelYGC = new double[n];
        ModelYGC = ModelY;
        double[][]XGC = new double[3][n];
        XGC = X;


        System.out.println("Sorting before");

        Sort s = new Sort();
        s.testQuickSort(ModelYGC,FactualYGC,XGC, n);
        System.out.println("Sorting after");
        System.out.println(Arrays.toString(ModelYGC));
        System.out.println(Arrays.toString(FactualYGC));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(XGC[i][j]);
                System.out.print("  ");
            }
            System.out.println();
        }
        double[] ZGC;

        int nS1 = k;
        int nS2 = n-k;

        double[][] XGCS1 = new double[3][nS1];
        double[] FactualYGCS1 = new double[nS1];
        double[] ModelYGCS1 = new double[nS1];
        double[] RegressionResidueES1  = new double[nS1];
        double[] zS1 = new double[3];

        double[][] XGCS2 = new double[3][nS2];
        double[] FactualYGCS2 = new double[nS2];
        double[] ModelYGCS2 = new double[nS2];
        double[] RegressionResidueES2  = new double[nS2];
        double[] zS2 = new double[3];


        //создаем модель для S1 и считаем S1

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < nS1; j++) {
                XGCS1[i][j]=XGC[i][j];
            }
        }
        for (int i = 0; i < nS1; i++) {
            FactualYGCS1[i] = FactualYGC[i];
        }

        zS1 = this.getModel(XGCS1, FactualYGCS1, nS1);
        ModelYGCS1 = this.countModelY1(zS1,XGCS1,nS1);
        RegressionResidueES1 = this.countRegressionResidueE(FactualYGCS1,ModelYGCS1,nS1);
        for (int i = 0; i < nS1; i++) {
            S1+=RegressionResidueES1[i]*RegressionResidueES1[i];
        }
        System.out.println("S1 " +S1);

        //создаем модель для S2 и считаем S2
        for (int i = 0; i < 3; i++) {
            for (int j = nS2-1; j < n; j++) {
                for (int l = 0; l < nS2-1; l++) {
                    XGCS2[i][l]=XGC[i][j];
                }
            }
        }
        for (int i = 0; i < nS2; i++) {
            for (int j = nS2-1; j < n; j++) {
                FactualYGCS2[i] = FactualYGC[j];
            }
        }

        zS2 = this.getModel(XGCS2, FactualYGCS2, nS2);
        ModelYGCS2 = this.countModelY1(zS2,XGCS2,nS2);
        RegressionResidueES2 = this.countRegressionResidueE(FactualYGCS2,ModelYGCS2,nS2);
        for (int i = 0; i < nS2; i++) {
            S3+=RegressionResidueES2[i]*RegressionResidueES2[i];
        }
        System.out.println("S3 " +S3);

        F = S3/S1;

        heteroscedasticity = (F-Fkr)>0;
        System.out.println("F="+F+"Fkr="+Fkr+" heteroscedasticity: " + heteroscedasticity);
        return heteroscedasticity;
    }


    public double corell(double[][] x, int n) {
        double corell = 0;
        double numerator=0;
        double denominator;
        double denominatorL=0;
        double denominatorR=0;
        double x1av = 0;
        double x2av = 0;
        double sum = 0;

//        count average

        for (int i = 0; i < n; i++) {
            x1av+=x[1][i];
        }
        x1av = x1av/n;

        for (int i = 0; i < n; i++) {
            x2av+=x[2][i];
        }
        x2av = x2av/n;

        for (int i = 0; i < n; i++) {
            numerator+=(x[1][i]-x1av)*(x[2][i]-x2av);
        }
        for (int i = 0; i < n; i++) {
            denominatorL+=(x[1][i]-x1av)*(x[1][i]-x1av);
        }
        for (int i = 0; i < n; i++) {
            denominatorR+=(x[2][i]-x2av)*(x[2][i]-x2av);
        }
        denominator = Math.sqrt(denominatorL*denominatorR);
        corell=numerator/denominator;
        System.out.println();
        System.out.println();
        System.out.println("Correlation "+corell);
        return corell;
    }



    public double[] countModelYY(){
        System.out.println("=====================");
        System.out.println("Y instead of X");
        System.out.println("=====================");
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }
        boolean firstStep = true;
        for (int i = 0; i < this.countFactors; i++) {
            for (int j = i+1; j < this.countFactors; j++) {
                System.out.println("i="+i);
                System.out.println("j="+j);
                for (int k = 0; k < this.n; k++) {
                    f[1][k]=FactorsX[i][k];
                    f[2][k]=FactorsX[j][k];
                    System.out.println("f[1]["+k+"]="+f[1][k]);
                    System.out.println("f[2]["+k+"]="+f[2][k]);
                }
                double[] r = this.getModel(f, this.FactualResultsY, this.n); //вернули коэффициенты
                this.ModelResultsY = this.countModelY1(r,f,this.n); //вернули теоретические результаты
                this.RegressionResidueE = this.countRegressionResidueE(this.FactualResultsY,this.ModelResultsY,this.n);
                this.dw = this.countDW(this.RegressionResidueE, this.n);
                this.R = this.R(f, this.FactualResultsY, r);
                this.corell(f,this.n);
                this.heteroscedasticity = this.countGolfildCvant(this.FactualResultsY, this.ModelResultsY , f, this.n);

                if (firstStep) {
                    this.bestModelResultsY =  this.ModelResultsY;
                    this.bestModelR =  this.R;
                    this.bestModelDW = this.dw;
                    this.bestModelHeteroscedasticity = this.heteroscedasticity;
                    this.bestModelFactor1 = i;
                    this.getBestModelFactor2 = j;
                    firstStep = false;
                }
                else {

                    if (this.R>this.bestModelR) {
                        if (this.dw<this.bestModelDW) {
                            if (this.heteroscedasticity != true || this.heteroscedasticity == this.bestModelHeteroscedasticity) {
                                this.bestModelResultsY =  this.ModelResultsY;
                                this.bestModelR =  this.R;
                                this.bestModelDW = this.dw;
                                this.bestModelHeteroscedasticity = this.heteroscedasticity;
                                this.bestModelFactor1 = i;
                                this.getBestModelFactor2 = j;

                                System.out.println("Best model for family:  ");
                                System.out.println("==========================");
                                System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
                                System.out.println("factor 1: " + this.bestModelFactor1 + " factor 2: " + this.getBestModelFactor2);
                            }
                        }
                    }

                }
//                Голфилд Квант
            }
        }

        System.out.println("Best model for family:  ");
        System.out.println("==========================");
        System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
        System.out.println("factor 1: " + this.bestModelFactor1 + "factor 2: " + this.getBestModelFactor2);
        return this.bestModelResultsY;
    }



    public double[] countModelX1(){
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }


        boolean firstStep = true;
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
                double[] r = this.getModel(f, this.FactualResultsY, this.n); //вернули коэффициенты
                this.ModelResultsY = this.countModelY1(r,f,this.n); //вернули теоретические результаты
                this.RegressionResidueE = this.countRegressionResidueE(this.FactualResultsY,this.ModelResultsY,this.n);
                this.dw = this.countDW(this.RegressionResidueE, this.n);
                this.R = this.R(f, this.FactualResultsY, r);
                this.corell(f,this.n);
                this.heteroscedasticity = this.countGolfildCvant(this.FactualResultsY, this.ModelResultsY , f, this.n);

                if (firstStep) {
                    this.bestModelResultsY =  this.ModelResultsY;
                    this.bestModelR =  this.R;
                    this.bestModelDW = this.dw;
                    this.bestModelHeteroscedasticity = this.heteroscedasticity;
                    this.bestModelFactor1 = i;
                    this.getBestModelFactor2 = j;
                    firstStep = false;
                }
                else {

                    if (this.R>this.bestModelR) {
                        if (this.dw<this.bestModelDW) {
                            if (this.heteroscedasticity != true || this.heteroscedasticity == this.bestModelHeteroscedasticity) {
                                this.bestModelResultsY =  this.ModelResultsY;
                                this.bestModelR =  this.R;
                                this.bestModelDW = this.dw;
                                this.bestModelHeteroscedasticity = this.heteroscedasticity;
                                this.bestModelFactor1 = i;
                                this.getBestModelFactor2 = j;

                                System.out.println("Best model for family:  ");
                                System.out.println("==========================");
                                System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
                                System.out.println("factor 1: " + this.bestModelFactor1 + " factor 2: " + this.getBestModelFactor2);
                            }
                        }
                    }

                }
//                Голфилд Квант
            }
        }

        System.out.println("Best model for family:  ");
        System.out.println("==========================");
        System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
        System.out.println("factor 1: " + this.bestModelFactor1 + "factor 2: " + this.getBestModelFactor2);
        return this.bestModelResultsY;
    }

    public double[] countModelLog(){
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }
        boolean firstStep = true;
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                System.out.println("i="+i);
                System.out.println("j="+j);
                for (int k = 0; k < this.n; k++) {
                    f[1][k]=Math.log(FactorsX[i][k]);
                    f[2][k]=Math.log(FactorsX[j][k]);
                    System.out.println("f[1]["+k+"]="+f[1][k]);
                    System.out.println("f[2]["+k+"]="+f[2][k]);
                }
                double[] r = this.getModel(f, this.FactualResultsY, this.n); //вернули коэффициенты
                this.ModelResultsY = this.countModelY1(r,f,this.n); //вернули теоретические результаты
                this.RegressionResidueE = this.countRegressionResidueE(this.FactualResultsY,this.ModelResultsY,this.n);
                this.dw = this.countDW(this.RegressionResidueE, this.n);
                this.R = this.R(f, this.FactualResultsY, r);
                this.corell(f,this.n);
                this.heteroscedasticity = this.countGolfildCvant(this.FactualResultsY, this.ModelResultsY , f, this.n);

                if (firstStep) {
                    this.bestModelResultsY =  this.ModelResultsY;
                    this.bestModelR =  this.R;
                    this.bestModelDW = this.dw;
                    this.bestModelHeteroscedasticity = this.heteroscedasticity;
                    this.bestModelFactor1 = i;
                    this.getBestModelFactor2 = j;
                    firstStep = false;
                }
                else {

                    if (this.R>this.bestModelR) {
                        if (this.dw<this.bestModelDW) {
                            if (this.heteroscedasticity != true || this.heteroscedasticity == this.bestModelHeteroscedasticity) {
                                this.bestModelResultsY =  this.ModelResultsY;
                                this.bestModelR =  this.R;
                                this.bestModelDW = this.dw;
                                this.bestModelHeteroscedasticity = this.heteroscedasticity;
                                this.bestModelFactor1 = i;
                                this.getBestModelFactor2 = j;

                                System.out.println("Best model for family:  ");
                                System.out.println("==========================");
                                System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
                                System.out.println("factor 1: " + this.bestModelFactor1 + " factor 2: " + this.getBestModelFactor2);
                            }
                        }
                    }

                }
//                Голфилд Квант
            }
        }

        System.out.println("Best model for family:  ");
        System.out.println("==========================");
        System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
        System.out.println("factor 1: " + this.bestModelFactor1 + "factor 2: " + this.getBestModelFactor2);
        return this.bestModelResultsY;
    }


    public double[] countModelX2(){
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }
        boolean firstStep = true;
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                System.out.println("i="+i);
                System.out.println("j="+j);
                for (int k = 0; k < this.n; k++) {
                    f[1][k]=FactorsX[i][k]*FactorsX[i][k];
                    f[2][k]=FactorsX[j][k]*FactorsX[i][k];
                    System.out.println("f[1]["+k+"]="+f[1][k]);
                    System.out.println("f[2]["+k+"]="+f[2][k]);
                }
                double[] r = this.getModel(f, this.FactualResultsY, this.n); //вернули коэффициенты
                this.ModelResultsY = this.countModelY1(r,f,this.n); //вернули теоретические результаты
                this.RegressionResidueE = this.countRegressionResidueE(this.FactualResultsY,this.ModelResultsY,this.n);
                this.dw = this.countDW(this.RegressionResidueE, this.n);
                this.R = this.R(f, this.FactualResultsY, r);
                this.corell(f,this.n);
                this.heteroscedasticity = this.countGolfildCvant(this.FactualResultsY, this.ModelResultsY , f, this.n);

                if (firstStep) {
                    this.bestModelResultsY =  this.ModelResultsY;
                    this.bestModelR =  this.R;
                    this.bestModelDW = this.dw;
                    this.bestModelHeteroscedasticity = this.heteroscedasticity;
                    this.bestModelFactor1 = i;
                    this.getBestModelFactor2 = j;
                    firstStep = false;
                }
                else {

                    if (this.R>this.bestModelR) {
                        if (this.dw<this.bestModelDW) {
                            if (this.heteroscedasticity != true || this.heteroscedasticity == this.bestModelHeteroscedasticity) {
                                this.bestModelResultsY =  this.ModelResultsY;
                                this.bestModelR =  this.R;
                                this.bestModelDW = this.dw;
                                this.bestModelHeteroscedasticity = this.heteroscedasticity;
                                this.bestModelFactor1 = i;
                                this.getBestModelFactor2 = j;

                                System.out.println("Best model for family:  ");
                                System.out.println("==========================");
                                System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
                                System.out.println("factor 1: " + this.bestModelFactor1 + " factor 2: " + this.getBestModelFactor2);
                            }
                        }
                    }

                }
//                Голфилд Квант
            }
        }

        System.out.println("Best model for family:  ");
        System.out.println("==========================");
        System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
        System.out.println("factor 1: " + this.bestModelFactor1 + "factor 2: " + this.getBestModelFactor2);
        return this.bestModelResultsY;
    }

    public double[] countModelX3(){
        double[][]f=new double[3][this.n];
        int ii = 0;
        for (int i = 0; i < this.n; i++) {
            f[0][i]=1;
        }
        boolean firstStep = true;
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 4; j++) {
                System.out.println("i="+i);
                System.out.println("j="+j);
                for (int k = 0; k < this.n; k++) {
                    f[1][k]=FactorsX[i][k]*FactorsX[i][k]*FactorsX[i][k];
                    f[2][k]=FactorsX[j][k]*FactorsX[i][k]*FactorsX[i][k];
                    System.out.println("f[1]["+k+"]="+f[1][k]);
                    System.out.println("f[2]["+k+"]="+f[2][k]);
                }
                double[] r = this.getModel(f, this.FactualResultsY, this.n); //вернули коэффициенты
                this.ModelResultsY = this.countModelY1(r,f,this.n); //вернули теоретические результаты
                this.RegressionResidueE = this.countRegressionResidueE(this.FactualResultsY,this.ModelResultsY,this.n);
                this.dw = this.countDW(this.RegressionResidueE, this.n);
                this.R = this.R(f, this.FactualResultsY, r);
                this.corell(f,this.n);
                this.heteroscedasticity = this.countGolfildCvant(this.FactualResultsY, this.ModelResultsY , f, this.n);

                if (firstStep) {
                    this.bestModelResultsY =  this.ModelResultsY;
                    this.bestModelR =  this.R;
                    this.bestModelDW = this.dw;
                    this.bestModelHeteroscedasticity = this.heteroscedasticity;
                    this.bestModelFactor1 = i;
                    this.getBestModelFactor2 = j;
                    firstStep = false;
                }
                else {

                    if (this.R>this.bestModelR) {
                        if (this.dw<this.bestModelDW) {
                            if (this.heteroscedasticity != true || this.heteroscedasticity == this.bestModelHeteroscedasticity) {
                                this.bestModelResultsY =  this.ModelResultsY;
                                this.bestModelR =  this.R;
                                this.bestModelDW = this.dw;
                                this.bestModelHeteroscedasticity = this.heteroscedasticity;
                                this.bestModelFactor1 = i;
                                this.getBestModelFactor2 = j;

                                System.out.println("Best model for family:  ");
                                System.out.println("==========================");
                                System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
                                System.out.println("factor 1: " + this.bestModelFactor1 + " factor 2: " + this.getBestModelFactor2);
                            }
                        }
                    }

                }
//                Голфилд Квант
            }
        }

        System.out.println("Best model for family:  ");
        System.out.println("==========================");
        System.out.println("R: " + this.bestModelR +" DW: " + this.bestModelDW + " heteroscedasticity: "+  this.bestModelHeteroscedasticity);
        System.out.println("factor 1: " + this.bestModelFactor1 + "factor 2: " + this.getBestModelFactor2);
        return this.bestModelResultsY;
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

    public double[] getBestModelResultsY() {
        return bestModelResultsY;
    }

    public double getBestModelR() {
        return bestModelR;
    }

    public double getBestModelDW() {
        return bestModelDW;
    }

    public boolean isBestModelHeteroscedasticity() {
        return bestModelHeteroscedasticity;
    }

    public int getN() {
        return n;
    }

    public double[] getFactualResultsY() {
        return FactualResultsY;
    }
}
