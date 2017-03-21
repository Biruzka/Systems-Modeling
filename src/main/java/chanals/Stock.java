package chanals;

import java.util.Map;

/**
 * Created by biruzka on 10.03.17.
 */
public class Stock {
    double[] time;
    double[] stockStatistic;
    int countPointsChanalAverage;
    double chanalWidth = 0.3;
    int averPointsInChanal=0; //сколько в среднем точек в канале
    Chanal[] chanals;

    int n;

    public Stock(double[] stockStatistic, int n) {
        this.stockStatistic = stockStatistic;
        this.n = n;
        this.time = new double[n];
        for (int i = 0; i < this.n; i++) {
            time[i] = i;
        }
        this.chanals=new Chanal[n];
    }

//    public void countTrades() {
//
//        double a,b;
//        int firstP = 0;
//        int k = 6;
//        firstP = 1;
//        double[] koeff = this.koeff(firstP, 5);
//        int s = k;
////        for (int i=1;i<this.n-10;i++)
//        for (int i=1;i<15;i++)
//        {
//            boolean first = this.check(s, koeff);
//            boolean sec = this.check(s+1, koeff);
//            if ( first == false && sec==false)
//            {
//                //фиксация канал
//
//                if (koeff[0]>0.2) {
//                    this.averPointsInChanal++;
//                    chanals[averPointsInChanal] = new Chanal(koeff[0],k);
//                }
//
//                System.out.println("");
//                System.out.println("new chanal");
//                System.out.println("********");
//                System.out.println(" ");
//                firstP = s;
//                koeff = this.koeff(firstP, 5);
//                k = 6;
//                s = s+5;
//                System.out.println("s="+s);
//            }
//            else
//            {
//                System.out.println("trade update");
//                koeff = this.koeff(firstP, k);
//                k = k+1;
//                s = s+1;
//            }
//
//        }
//        int sumP=0;
//        for (int i = 1; i <= averPointsInChanal; i++) {
//            System.out.println();
//            System.out.println("A: " + chanals[i].a + "   points " + chanals[i].countPoints);
//            sumP+=chanals[i].countPoints;
//        }
//        sumP=sumP/averPointsInChanal;
//        System.out.println("aver points");
//        System.out.println(sumP);
//    }

    public int countTrades() {

        double a,b;
        int firstP = 0;
        int k = 6;
        firstP = 1;
        double[] koeff = this.koeff(firstP, 5);
        int s = k;
//        for (int i=1;i<this.n-10;i++)
        for (int i=1;s<(n-2);i++)
        {
            boolean first = this.check(s, koeff);
            boolean sec = this.check(s+1, koeff);
            if ( first == false && sec==false)
            {
                //фиксация канал

                if (koeff[0]>0.2) {
                    this.averPointsInChanal++;
                    chanals[averPointsInChanal] = new Chanal(koeff[0],k);
                }

                System.out.println("");
                System.out.println("new chanal");
                System.out.println("********");
                System.out.println(" ");
                firstP = s;
                koeff = this.koeff(firstP, 5);
                k = 6;
                s = s+5;
                System.out.println("s="+s);
            }
            else
            {
                System.out.println("trade update");
                koeff = this.koeff(firstP, k);
                k = k+1;
                s = s+1;
            }

        }
        if (koeff[0]>0.2) {
            this.averPointsInChanal++;
            chanals[averPointsInChanal] = new Chanal(koeff[0],k);
        }
        System.out.println("OKI");
        int sumP=0;
        for (int i = 1; i <= averPointsInChanal; i++) {
            System.out.println();
            System.out.println("A: " + chanals[i].a + "   points " + chanals[i].countPoints);
            sumP+=chanals[i].countPoints;
        }
        sumP=sumP/averPointsInChanal;
        System.out.println("aver points");
        System.out.println(sumP);
        return sumP;
    }
    /** по мнк находим апроксимирующую функцию с n точки  kтое(>=5) количество **/
//    koeff[0] = a, koeff[1] = b;
    public double[] koeff(int n, int k)
    {
        System.out.println("Points:");
        System.out.println(n);
        System.out.println(k);
        double[] koeff = new double[2];
        int sumx = 0;
        int sumy = 0;
        int sumx2 = 0;
        int sumxy = 0;
        int before = k+n;
        if (before > this.n) {
            before = this.n;
        }
        for (int i = n; i<before; i++)
        {
            sumx += this.time[i];
            sumy += this.stockStatistic[i];
            sumx2 += this.time[i] * this.time[i];
            sumxy += this.time[i] * this.stockStatistic[i];

        }
        koeff[0] = (double)(k*sumxy - (sumx*sumy)) / (double)(k*sumx2 - sumx*sumx);
        koeff[1] = (double)(sumy - koeff[0]*sumx) / k;
        System.out.println("y = " + koeff[0] + "x + " + koeff[1]);
        return koeff;
    }


    public boolean check(int n, double[] koeff)
    {
        double up_b;
        double down_b;

        if (koeff[0]>0)
        {
            up_b = koeff[0]*time[n]+koeff[1]+this.chanalWidth ;
            down_b = koeff[0]*time[n]+koeff[1]-this.chanalWidth ;
            if ((stockStatistic[n]>up_b)||(stockStatistic[n]<down_b))
            {
                System.out.println(" ");
                System.out.println("doesn't belong");
                System.out.println("********");
                System.out.println(" ");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            up_b = ((stockStatistic[n]-koeff[1])/koeff[0])+this.chanalWidth ;
            down_b = ((stockStatistic[n]-koeff[1])/koeff[0])-this.chanalWidth ;
            if ((time[n]>up_b)||(time[n]<down_b))
            {
                System.out.println(" ");
                System.out.println("doesn't belong");
                System.out.println("********");
                System.out.println(" ");
                return false;
            }
            else
            {
                return true;
            }
        }


    }
}
