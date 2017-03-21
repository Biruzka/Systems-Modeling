package chanals;

/**
 * Created by biruzka on 10.03.17.
 */
public class Main {
    public static void main(String[] args) {
        //из 7 выбирать по три лучших
        Stocks stocks = new Stocks(4);
        stocks.read("actions.xlsx");
        //1 акция
        double[] s1 = stocks.getStockList(0);
        for (int i = 0; i < stocks.getN(); i++) {
            System.out.println(s1[i]);
        }
        Stock st1 = new Stock(s1, stocks.getN());
        int sum1chanals1 = st1.countTrades();


//        1 акция
        double[] s2 = stocks.getStockList(1);
        for (int i = 0; i < stocks.getN(); i++) {
            System.out.println(s2[i]);
        }
        Stock st2 = new Stock(s2, stocks.getN());
        int sum1chanals2 = st2.countTrades();

        System.out.println(sum1chanals1);
        System.out.println(sum1chanals2);

    }
}
