package test.de.monticore.lang.testsijava.testsijava;

public class Main {


    public int main() {
        double v = (double) 3.6 * (8.333333333333333E-5);
        double distance = (double) 10.0 * (0.2);
        double time = (double) 0.016666666666666666 * (180.0);

        return (int) (calculateTime(v, 100.0 * distance) * 20 + (60.0 * time));
    }

    public double calculateTime(double v, double d) {
        double d_R = (double) (d);
        double d_B = (double) (0.001 * d);
        System.out.println(d_R);
        System.out.println(d_B);
        System.out.println(d + "mm");

        return (double) 2.777777777777778E-4 * ((0.001 * d) / (0.2777777777777778 * v));
    }
}
