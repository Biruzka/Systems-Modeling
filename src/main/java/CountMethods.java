import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by biruzka on 19.02.17.
 */
public class CountMethods {


//*********CAP start*********//

    public static double CAP (double yn, double y1, int n) {
        return ((yn-y1)/(n-1));
    }

    public static double forecastCounter (double yn, int k, double CAP) {
        return (yn + k * CAP);
    }

    public static Map<Integer,Double> forecast (Map<Integer,Double> statisticList, double CAP) {
        Map<Integer,Double> forecast = new HashMap<Integer,Double>();
        double forecastYNK;
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            forecastYNK = CountMethods.forecastCounter(pair.getValue(),1,CAP);
            forecast.put((int)pair.getKey()+1,forecastYNK);
        }
        return forecast;
    }

//*********CAP END*********//

// *********IRVIN CRITERY*********//

    public static double average (Map<Integer,Double> statisticList) {
        double sum = 0;
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            sum += pair.getValue();
        }

        return sum/statisticList.size();
    }



    public static double standardDeviation (Map<Integer,Double> statisticList) {
        double sd = 0;

        double sum = 0;
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            sum += pow(pair.getValue()- average(statisticList),2);
        }
        sd = sqrt(sum/(statisticList.size()-1));
        return sd;
    }


    public static Map<Integer,Double> irvin (Map<Integer,Double> statisticList) {
        Map<Integer,Double> irvinList = new HashMap<Integer,Double>();
        double irvinStep;
        double sd = standardDeviation(statisticList);
        double prev = 0;
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            irvinStep = abs(pair.getValue()- prev)/sd;
            irvinList.put((int)pair.getKey(),irvinStep);
            prev = pair.getValue();
        }
        return irvinList ;
    }
// *********IRVIN CRITERY END*********//


// *********AVARAGES*********//
//    среднее произведения xiyi
public static double averageXiYi (Map<Integer,Double> statisticList) {
    double sum = 0;
    for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
        sum += pair.getValue()*pair.getKey();
    }

    return sum/statisticList.size();
}
//    среднее квадратов xi
public static double averageXiSquared (Map<Integer,Double> statisticList) {
    double sum = 0;
    for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
        sum += pair.getKey()*pair.getKey();
    }

    return sum/statisticList.size();
}
//    среднее yi
public static double averageYi (Map<Integer,Double> statisticList) {
    double sum = 0;
    for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
        sum += pair.getValue();
    }

    return sum/statisticList.size();
}
//    среднее xi
public static double averageXi (Map<Integer,Double> statisticList) {
    double sum = 0;
    for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
        sum += pair.getKey();
    }

    return sum/statisticList.size();
}
// *********AVARAGES END*********//

// *********МНК*********//
//возвращает массив с а и b
    public static double[] LeastSquareMethod (Map<Integer,Double> statisticList) {
        double[] straight ;
        straight = new double[2];
        double a;
        double b;

        a = (averageXiYi(statisticList)-(averageXi(statisticList)*averageYi(statisticList)))/
                (averageXiSquared(statisticList)-pow(averageXi(statisticList),2));
        b = averageYi(statisticList)-a*averageXi(statisticList);

        straight[0] = a;
        straight[1] = b;
        return straight;
    }
    public static Map<Integer,Double> deviations (Map<Integer,Double> statisticList, double[] straight) {
        Map<Integer,Double> deviationsList = new HashMap<Integer,Double>();
        for (Map.Entry<Integer,Double> pair : statisticList.entrySet()) {
            deviationsList.put(pair.getKey(),pair.getValue()-straight[1]-straight[0]*pair.getKey());
        }
        return deviationsList ;
    }
// *********МНК END*********//

    public static StatisticModel[] findTwoBestModels(StatisticModel[] model, int countFamily) {
        StatisticModel[] twoModel =  new StatisticModel[2];
        StatisticModel[] modelSort;
        modelSort = model;

        for (int i = 0; i < countFamily; i++) {
            for (int j = countFamily - 1; j > i; j--) {
                if (modelSort[j].getBestModelR()>modelSort[j-1].getBestModelR()) {
                    if (modelSort[j].getBestModelDW()<modelSort[j-1].getBestModelDW()) {
                        if (!modelSort[j].isBestModelHeteroscedasticity() || modelSort[j].isBestModelHeteroscedasticity() == modelSort[j-1].isBestModelHeteroscedasticity()) {
                            StatisticModel tmp = modelSort[j];
                            modelSort[j] = modelSort[j-1];
                            modelSort[j-1] = tmp;
                        }
                    }
                }
            }
        }

        twoModel[0] = modelSort[0];
        twoModel[1] = modelSort[1];

        return twoModel;
    }

    public static double[][] createFactorsFromModels(StatisticModel[] models, int n) {

        double[][] factors = new double[2][n];
        double[] firstBestFactors = models[0].getBestModelResultsY();
        double[] secondBestFactors = models[1].getBestModelResultsY();

        for (int i = 0; i < n; i++) {
            factors [0][i] = firstBestFactors[i];
            factors [1][i] = secondBestFactors[i];
        }
        return factors;
    }
}
