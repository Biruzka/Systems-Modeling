import java.util.ArrayList;
import java.util.Arrays;


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

        double[] ModelResultsYModel1;
        StatisticModel modelStatisticUK1 = new StatisticModel();
        modelStatisticUK1.importStatistic();
        ModelResultsYModel1 = modelStatisticUK1.countModelX1();

        double[] ModelResultsYModel2;
        StatisticModel modelStatisticUK2 = new StatisticModel();
        modelStatisticUK2.importStatistic();
        ModelResultsYModel2 = modelStatisticUK2.countModelX2();

        double[] ModelResultsYModel3;
        StatisticModel modelStatisticUK3 = new StatisticModel();
        modelStatisticUK3.importStatistic();
        ModelResultsYModel3 = modelStatisticUK3.countModelX3();

        double[] ModelResultsYModelLog;
        StatisticModel modelStatisticUKLog = new StatisticModel();
        modelStatisticUKLog.importStatistic();
        ModelResultsYModelLog = modelStatisticUKLog.countModelLog();

        int n = modelStatisticUK1.getN();
        double[] FactualResultsY = modelStatisticUK1.getFactualResultsY();

        int countFamily = 4;
        StatisticModel[] models = {modelStatisticUK1, modelStatisticUK2, modelStatisticUK3, modelStatisticUKLog};
        StatisticModel[] twoBestModels;
        twoBestModels = CountMethods.findTwoBestModels(models, countFamily);
        double[][] FactorsFromY = CountMethods.createFactorsFromModels(twoBestModels, n);

        double[] ModelResultsYModelYY;
        StatisticModel finalModel = new StatisticModel();
        finalModel.importStatisticYToX(n, 2, FactualResultsY, FactorsFromY);
        ModelResultsYModelYY = finalModel.countModelYY();
        System.out.println("njkn");

    }


}
