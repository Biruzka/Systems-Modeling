package trade;
/*
* рабоатетм с одним трендом
* Проходит по всем предыдущим дням, находит возрастающие тренды, высчитывает
* среднее количество дней в тренде, фиксирует последний день(возрастает ли)
*/

public class Average {
    double update = 0;
    double aver = 0;
    int n = 0;
    int k = 0;
    int s = 0;
    int count = 1;
    int countTrendChanals = 0; // количество каналов в тренде
    double[][] chanals = new double[100][4]; // сами каналы одного тренда - положительные
    double[] ab = new double[2];
    double[] result = new double[4];

    public double[] findAver(double[] y, int ii){
        line l= new line(); //одна акция
        ab = l.koeff(y, n, 2);
        k = 2;
        for (int i=0;s<200+ii;i++) //s - точка текущая
        {
            if (l.check(y, s) == false) // y данные дней, s - номер дня; проверка принадлежности точки к каналу
            {
                //System.out.println("Тренд #"+count+" : y = "+ab[0]+"x + "+ab[1]);
                count = count+1;
                if (ab[0]>0.2){
                    chanals[countTrendChanals][0] = ab[0];
                    chanals[countTrendChanals][1] = ab[1];
                    chanals[countTrendChanals][2] = k; //количество точек в канале
                    chanals[countTrendChanals][3] = count-1; //номер канала
                    countTrendChanals++; //количество каналов для тренда
                }
                n = s;
                ab = l.koeff(y, n, 2);
                k = 2;
                s = s+2;
                if(ab[0]>0.2){
                    update = 1;
                }
                else {
                    update = 0;
                }
            }
            else
            {
                //Обновление тренда
                ab = l.koeff(y, n, k);
                k = k+1;
                s = s+1;
                if(ab[0]>0.2){
                    update = 1;
                }
                else
                {update = 0;}
            }

        }

        for (int i=0;i<countTrendChanals;i++){
            aver = aver+chanals[i][2];
        }
        aver = Math.round(aver/countTrendChanals);
        result[0] = aver; //среднее количество точек
        result[1] = update; //возрастал ли канал
        result[2] = k; // сколько дней прошло с возрастания
        result[3] = ab[0]; //коэффициет a
        return result;
    }
}
