/**
 * Created by biruzka on 19.02.17.
 */
public class Main {
    public static void main(String[] args) {
        ReaderWriter rw = new ReaderWriter();
        Statistic statistic;
        statistic = rw.read();
        statistic.Count();
        statistic.toShow();

    }
}
