package chanals;

/**
 * Created by biruzka on 10.03.17.
 */
public class Chanal {
    double a;
    int countPoints;

    double b;

    double firstPoints;
    double lastPoints;

    public Chanal(double a, int countPoints) {
        this.a = a;
        this.countPoints = countPoints;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getFirstPoints() {
        return firstPoints;
    }

    public void setFirstPoints(double firstPoints) {
        this.firstPoints = firstPoints;
    }

    public double getLastPoints() {
        return lastPoints;
    }

    public void setLastPoints(double lastPoints) {
        this.lastPoints = lastPoints;
    }

    public int getCountPoints() {
        return countPoints;
    }

    public void setCountPoints(int countPoints) {
        this.countPoints = countPoints;
    }
}
