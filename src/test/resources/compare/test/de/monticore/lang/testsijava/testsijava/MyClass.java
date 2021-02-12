/* (c) https://github.com/MontiCore/monticore */
package test.de.monticore.lang.testsijava.testsijava;

public class MyClass {

    double var = (double) ((3.0) * 3.6);
    int varInt = (int) (3);
    int y = (int) (3);
    int m = (int) (3);
    int km = (int) (3);


    public int method1(int varInt2) {
        // Hier könnte 0 bei rauskommen double -> int
        int var2 = (int) ((4.0) * 1.0E9);
        var2 = (int) ((5.0 / (2.0 * 5.0)) / 1000000.0);
        var2 = (int) ((5.0) / 1.0E12);
        var2 *= (int) (varInt2);
        int varS = (int) ((3.0) / 3600.0);
        int varI_M = (int) ((10.0) * 10.0);
        var2 = (int) ((varI_M / varS * varS) / 1.296E17);

        return (int) ((varI_M) / 10000.0);
    }
}
