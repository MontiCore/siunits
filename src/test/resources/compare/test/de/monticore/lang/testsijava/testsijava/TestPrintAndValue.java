/* (c) https://github.com/MontiCore/monticore */

package test.de.monticore.lang.testsijava.testsijava;

public class TestPrintAndValue {

    public void main() {
        double var = (double) ((7200.0) / 3600.0);
        System.out.println(((20.0) * 1000.0) + 30.0 + "m");
        System.out.println(var + "h");
        System.out.println(var);
        System.out.println(var + 10);
        System.out.println(((var) * 60.0) + 30.0);
        System.out.println(((var) * 3600.0));
        System.out.println(((var) * 3600.0) + 10);
        System.out.println(((((var) * 60.0) + 30.0) * 60.0));
        System.out.println(2000.0 / var + "km/h");
        System.out.println(((var) * 3600000.0) + 10.0 + "ms");
    }
}
