import java.io.IOException;

/**
 * Created by biruzka on 19.02.17.
 */
public class Main {

//    САП, Ирвин, МНК, отклонения, прогноз

//    public static void main(String[] args) throws IOException {
//        ReaderWriter rw = new ReaderWriter();
//        Statistic statistic;
//        statistic = rw.read();
//        statistic.Count();
//        statistic.toShow();
//    }


//    Украина за 2000 - 2007 года. Факторы: ВВП, газ, промышленность, грузооборот. Проектируем курс валюты
    public static void main(String[] args) {
        StatisticModel statisticUk = new StatisticModel();
        statisticUk.importStatistic();
        statisticUk.count();
    }
}
