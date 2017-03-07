import java.util.Arrays;

/**
 * Created by biruzka on 07.03.17.
 */
public class Sort {

    public Sort() {
    }

    public void quickSort(double arr[], int left, int right, double b[], double[][] x) {
        int index = partition(arr, left, right, b, x);
        if (left < index - 1)
            quickSort(arr, left, index - 1, b, x);
        if (index < right)
            quickSort(arr, index, right, b, x);
    }

    public int partition(double arr[], int left, int right, double b[], double[][] x)
    {
        int i = left, j = right;
        double tmp;
        double tmp2;
        double pivot = arr[(left + right) / 2];

        while (i <= j) {
            while (arr[i] < pivot)
                i++;
            while (arr[j] > pivot)
                j--;

            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;

                tmp = b[i];
                b[i] = b[j];
                b[j] = tmp;

                for (int k = 1; k < 3; k++) {
                    tmp = x[k][i];
                    x[k][i] = x[k][j];
                    x[k][j] = tmp;
                }

                i++;
                j--;
            }
        }

        return i;
    }

    public void testQuickSort( double a[],  double b[], double x[][], int n) {


        for (int i = 0; i < 3; i++) {
            // Цикл по второй размерности выводит колонки - вывод одной строки
            for (int j = 0; j < n; j++) {
                // Используем оператор print - без перехода на следующую строку
                System.out.print(x[i][j]);
                System.out.print("  ");
            }
            // Переход на следующую строку
            System.out.println();
        }

        quickSort(a, 0, a.length-1, b, x);


    }

    public static void main(String[] args) {
        double a[] = {1, 12, 5, 26};
        double b[] = {99, 98, 4, 6};
        double[][] x =  {{1,1,1,1},{4,7,3,2},{2,40,8,90}};

        Sort s = new Sort();
        s.testQuickSort(a,b,x, 4);

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
        for (int i = 0; i < 3; i++) {
            // Цикл по второй размерности выводит колонки - вывод одной строки
            for (int j = 0; j < 4; j++) {
                // Используем оператор print - без перехода на следующую строку
                System.out.print(x[i][j]);
                System.out.print("  ");
            }
            // Переход на следующую строку
            System.out.println();
        }
    }

}
