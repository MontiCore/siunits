package test.de.monticore.lang.testsijava.testsijava;

public class Main2 {


    public double func1() {
        int v1 = (int) (10.0 * 5.0);
        double v2 = (double) (10.0 * v1);

        return (double) (2.777777777777778E-5 * func2(1.0E-6 * v2) / 1.0);
    }

    public double func2(double x) {
        return (double) (3.6E10 * x / 2.0);
    }
}
