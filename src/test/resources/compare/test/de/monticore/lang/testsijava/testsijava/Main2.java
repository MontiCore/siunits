/* (c) https://github.com/MontiCore/monticore */

package test.de.monticore.lang.testsijava.testsijava;

public class Main2 {

    public double func1() {
        int v1 = (int) ((5.0) * 10.0);
        double v2 = (double) ((v1) * 10.0);

        return (double) ((func2((v2) / 1000000.0) / 1.0) / 36000.0);
    }


    public double func2(double x) {
        return (double) ((x / 2.0) * 3.6E10);
    }
}
