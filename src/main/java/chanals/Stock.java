package chanals;

import java.util.Map;

/**
 * Created by biruzka on 10.03.17.
 */
public class Stock {
    double[] time;
    double[] stockStatistic;
    int countPointsChanalAverage;
    int chanalWidth = 100;
    int n;

    public Stock(double[] stockStatistic, int n) {
        this.stockStatistic = stockStatistic;
        this.n = n;
        this.time = new double[n];
        for (int i = 0; i < this.n; i++) {
            time[i] = i;
        }
    }

    public void countTrades() {
        double a,b;
        int firstP = 0;
        int k = 6;
        firstP = 1;
        double[] koeff = this.koeff(firstP, 5);
        int s = k;
        for (int i=1;i<this.n-5;i++)
        {
            boolean first = this.check(s, koeff);
            boolean sec = this.check(s+1, koeff);
            if ( first == false && sec==false)
            {
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
    }
    /** по мнк находим апроксимирующую функцию с n точки  kтое(>=5) количество **/
//    koeff[0] = a, koeff[1] = b;
    public double[] koeff(int n, int k)
    {
        System.out.println("kkkkkkkkkkkk");
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
