package trade;

/*
Данные: 14 марта 2016 - 14 марта 2017
Акции за период: 14 марта 2016 - 24 декабря 2016
Прогноз на 22 дня: 24 декабря 2016 - 26 января 2017
*/

public class Main {
    public static void main(String[] args){
        double[][] data = new double[254][9];
        double[] y = new double[254];
        double[] result = new double[2];
        double[] average = new double[9];
        double[] increasing = new double[9];
        double[] countDays = new double[9];
        double[] a = new double[9]; //коэффициенты
        boolean dayIsSuitable = false;
        double[] max1 = {0,0};// номер акции и значение - то есть money
        double[] max2 = {0,0};
        double[] max3 = {0,0};
        ActionService actionService = new ActionService();
        data = actionService.importData();
        int day = 200;
        int analizedDataIndex = 200;
        double earning = 0;

        for (int action=0;action<9;action++){
            for (int i=0;i<254;i++){
                y[i]=data[i][action];
            }
            //Находим положительные каналы и среднее количество точек в них
            Average aver = new Average();
            result = aver.findAver(y, 0);
            average[action]=result[0];
            increasing[action]=result[1]; // возрастание
            countDays[action]=result[2]; //сколько дней прошло в возрастающем тренде
            a[action]=result[3];
        }
        //прогноз 3 лучших

        for (int l=1;l<23;l++){
            for (int i=0;i<9;i++){
                dayIsSuitable = actionService.isDaySuitable(average[i], increasing[i], countDays[i]);

                if(dayIsSuitable){
                    if (a[i]>max1[1]){//лучшая акция
                        max1[1] = a[i];
                        max1[0] = i+1;
                    }

                    if (a[i]>max2[1]&&a[i]<max1[1]){ //акция 2 места
                        max2[1] = a[i];
                        max2[0] = i+1;
                    }

                    if (a[i]>max3[1]&&a[i]<max2[1]&&a[i]<max1[1]){ //акция 3 места
                        max3[1] = a[i];
                        max3[0] = i+1;
                    }
                }
            }

            for (int i=0;i<9;i++){
                for (int ii=0;ii<254;ii++){
                    y[ii]=data[ii][i];
                }
                Average aver = new Average();
                result = aver.findAver(y, l);
                average[i]=result[0];
                increasing[i]=result[1];
                countDays[i]=result[2];
                a[i]=result[3];

            }
            System.out.println("==");
            System.out.println("Day number: "+l);
            System.out.println("Best actions(numbers):"+max1[0]+", "+max2[0]+", "+max3[0]+"");

            //Прибыль по лучшим акциям
            double money = 0;
            day++;
            if (max1[0]!=0){
                int indexOfMaxAction = (int)max1[0]-1;
                for (int i=0;i<254;i++){
                    y[i]=data[i][indexOfMaxAction]; //деньги и считает прирост
                }
                money = y[day]-y[day-1];
            }
            if (max2[0]!=0){
                int indexOfMaxAction = (int)max2[0]-1;
                for (int i=0;i<254;i++){
                    y[i]=data[i][indexOfMaxAction];
                }
                money = money + y[day]-y[day-1];
            }
            if (max3[0]!=0){
                int indexOfMaxAction = (int)max3[0]-1;
                for (int i=0;i<254;i++){
                    y[i]=data[i][indexOfMaxAction];
                    //System.out.println(y[i]);

                }
                money = money+ y[day]-y[day-1];
            }
            System.out.println("Profit(%): "+money);
            earning = earning+money;
            max3[1] = 0; //значение
            max3[0] = 0; // номер акции
            max2[1] = 0;
            max2[0] = 0;
            max1[1] = 0;
            max1[0] = 0;
            money = 0;
        }
        System.out.println("");
        System.out.println("Profit at all(%): "+earning);
/*Расчет прибыли по лучшим акциям (на основании реальных данных)*/
        double earningReal=0;
        double[] realMoney = new double[9];
        for( int i=0;i<22;i++){
            analizedDataIndex++;
            for (int j=0;j<9;j++){

                realMoney[j] = data[analizedDataIndex][j]-data[analizedDataIndex-1][j];
                //System.out.println(realMoney[j]);
            }
            double maxi1=0;
            double maxi2=0;
            double maxi3=0;
            for (int j=0;j<9;j++){
                if (realMoney[j]>maxi1){
                    maxi1 = realMoney[j];
                }
                if (realMoney[j]>maxi2&&realMoney[j]!=maxi1){
                    maxi1 = realMoney[j];
                }
                if (realMoney[j]>maxi3&&realMoney[j]!=maxi2&&realMoney[j]!=maxi1){
                    maxi1 = realMoney[j];
                }
            }
            earningReal = earningReal+maxi1+maxi2+maxi3;
        }
        System.out.println("");
        System.out.println("Real profit(%): "+earningReal);
    }
}
