public class line
{
	int[] x = {0,1,2,3,4,5,6,7 ,8, 9,10,11,12, 13,14,15,16,17,18,19,20,21,22,23,24,25,26};
	int[] y = {0,3,2,4,6,6,5,7, 10,8,10,7 ,6 , 7, 9, 11,13,11,14,13,16,15,12,10,12,9, 7};
	double a,b;
	double up_b;
	double down_b;
	static int n = 0;
	static int k = 0;
	
	/** по мнк находим апроксимирующую функцию с n точки  kтое(>=5) количество **/
	public void koeff(int n, int k)
	{
		int sumx = 0;
		int sumy = 0;
		int sumx2 = 0;
		int sumxy = 0;
		for (int i = 0+n; i<k+n; i++) 
		{
		    sumx += x[i];
		    sumy += y[i];
		    sumx2 += x[i] * x[i];
		    sumxy += x[i] * y[i];
		    
		}
		  a = (double)(k*sumxy - (sumx*sumy)) / (double)(k*sumx2 - sumx*sumx);
		  b = (double)(sumy - a*sumx) / k;
		  String approks_func = "y = " + a + "x + " + b;
		  System.out.println(approks_func);
		  return;
	}
	
	public boolean check(int n)
	{
		if (a>0)
		{
			up_b = a*x[n]+b+3;
			down_b = a*x[n]+b-3;
			if ((y[n]>up_b)||(y[n]<down_b))
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
			up_b = ((y[n]-b)/a)+3;
			down_b = ((y[n]-b)/a)-3;
			if ((x[n]>up_b)||(x[n]<down_b))
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
	//Âûâîä
	public static void main(String[] args)
	{
		line l= new line();
		n =1;
		l.koeff(n, 5);
		k = 6;
		int s = k;
		for (int i=1;i<15;i++)
		{
			boolean first = l.check(s);
			boolean sec = l.check(s+1);
			if ( first == false && sec==false)
			{
				System.out.println("");
				System.out.println("new chanal");
				System.out.println("********");
				System.out.println(" ");
				n = s;
				l.koeff(n, 5);
				k = 6;
				s = s+5;
				
				
			}
			else
			{
				System.out.println("trade update");
				l.koeff(n, k);
				k = k+1;
				s = s+1;
			}
			
		}
}
}
