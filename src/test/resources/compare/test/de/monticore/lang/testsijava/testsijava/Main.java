/* (c) https://github.com/MontiCore/monticore */
package test.de.monticore.lang.testsijava.testsijava;

public class Main {

    public int main() {
        double v = (double) ((3.0) / 10000.0);
        double distance = (double) ((200000.0) / 100000.0);
        double time = (double) (3.0);

        return (int) ((((calculateTime(v, (distance) * 100.0) * 20) * 60.0) + time) * 60.0);
    }


    public double calculateTime(double v, double d) {
        double d_R = (double) (d);
        double d_B = (double) (((d) / 1000.0));
        System.out.println(d_R);
        System.out.println(d_B);
        System.out.println(d + "mm");

        return (double) ((d / v) / 1000000.0);
    }
}
