package chanals;

/**
 * Created by biruzka on 10.03.17.
 */
public class Main {
    public static void main(String[] args) {
        //из 7 выбирать по три лучших
        Stocks stocks = new Stocks(7);
        stocks.read("stocks.xlsx");
        double[] s = stocks.getStockList(0);
        for (int i = 0; i < stocks.getN(); i++) {
            System.out.println(s[i]);
        }
        Stock st1 = new Stock(s, stocks.getN());
        st1.countTrades();
    }
}
