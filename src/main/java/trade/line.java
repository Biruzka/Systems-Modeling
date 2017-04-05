package trade;
/*
* Нахождение a,b для тренда (y=ax b)
* и проверка принадлежности следующей точки каналу
*/

public class line
{

    double a,b;
    double up_b;
    double down_b;
    static int n = 0;
    static int k = 0;
    double ab[]=new double[2];
    //По МНК находим аппроксимирующую прямую по k(>=5) точкам
    public double[] koeff(double[] y, int n, int k)
    {
        double[] x = new double[255];
        for (int i=0;i<254;i++)
        {
            x[i]=i+1;
        }
        double sumx = 0;
        double sumy = 0;
        double sumx2 = 0;
        double sumxy = 0;
        for (int i = 0+n; i<k+n; i++)
        {
            sumx += x[i];
            sumy += y[i];
            sumx2 += x[i] * x[i];
            sumxy += x[i] * y[i];
        }
        a = (double)(k*sumxy - (sumx*sumy)) / (double)(k*sumx2 - sumx*sumx);
        b = (double)(sumy - a*sumx) / k;
        //System.out.println("appr function: y = " + a + "x + " + b);
        ab[0]=a;
        ab[1]=b;
        return ab;
    }

    public boolean check(double[] y, int n)
    {
        double[] x = new double[255];
        for (int i=0;i<255;i++)
        {
            x[i]=i;
        }
        if (a>0)
        {
            up_b = a*x[n]+b+1.5;
            down_b = a*x[n]+b-1.5;

            if ((y[n]>up_b)||(y[n]<down_b))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            up_b = ((y[n]-b)/a)+1.5;
            down_b = ((y[n]-b)/a)-1.5;
            if ((x[n]>up_b)||(x[n]<down_b))
            {
                return false;
            }
            else
            {
                return true;
            }
        }


    }
}