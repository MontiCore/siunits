package test.de.monticore.lang.testsijava.testsijava;

public class Main {


    public int main() {
        return (int) main_();
    }
    public double main_() {
        double v = 8.333333333333333E-5;
        double distance = 0.2;
        double time = 180.0;

        return calculateTime_(v,distance) * 20 + time;
    }

    public double calculateTime(double v, double d) {
        double v_ = (double) v * 0.2777777777777778;
        double d_ = (double) d * 0.001;

        return (double) (calculateTime_(v_, d_) * 2.777777777777778E-4);
    }
    public double calculateTime_(double v, double d) {
        double d_R = 1000.0 * d;
        double d_B = d;
        System.out.println("" + d_R);
        System.out.println("" + d_B);
        System.out.println("" + 1000.0 * d + " mm");

        return d / v;
    }
}
