package test.de.monticore.lang.testsijava.testsijava;

public class Main {


    public int main() {
        double v = (double) (1.0E-4 * 3.0);
        double distance = (double) (9.999999999999999E-6 * 200000.0);
        double time = (double) (3.0);

        return (int) (3600.0 * calculateTime(v, 100.0 * distance) * 20 + time);
    }

    public double calculateTime(double v, double d) {
        double d_R = (double) (d);
        double d_B = (double) (0.001 * d);
        System.out.println(d_R);
        System.out.println(d_B);
        System.out.println(d + "mm");

        return (double) (1.0E-6 * d / v);
    }
}
