package old;

/*
 * Для конкретного дня - возрастала ли акция и не достиг ли день среднего значения
*/

public class Daily {
    //если акция возрастала u == 1, среднее значение точек в возрастающем канале, сколько прошло дней
    public boolean daily(double average, double increasing, double countDays){
        boolean check = false;
        if (countDays<average && increasing==1){
            check = true;
        }
        return check;
    }
}
