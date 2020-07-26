package test.de.monticore.lang.testsijava.testsijava;

public class Main2 {


    public double func1() {
        int v1 = (int) 100.0 * (0.5);
        double v2 = (double) 1000.0 * ((0.01 * v1));

        return (double) (func2(1.0E-6 * v2) * 1.0);
    }

    public double func2(double x) {
        return (double) 36000.0 * ((1000.0 * x) * 0.002);
    }
}
