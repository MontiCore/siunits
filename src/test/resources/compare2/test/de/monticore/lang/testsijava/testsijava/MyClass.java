package test.de.monticore.lang.testsijava.testsijava;

public class MyClass {

    double var = (double) (3.6 * 3.0);
    int varInt = (int) (3);
    int y = (int) (3);
    int m = (int) (3);
    int km = (int) (3);

    public int method1(int varInt2) {
        int var2 = (int) (1.0E9 * 4.0);
        var2 = (int) (1.0E-6 * 5.0 / (2.0 * 5.0));
        var2 = (int) (1.0E-12 * 5.0);
        var2 *= (int) (varInt2);
        int varS = (int) (2.777777777777778E-4 * 3.0);
        int varI_M = (int) (10.0 * 10.0);
        var2 = (int) (7.716049382716049E-18 * varI_M / varS * varS);

        return (int) (1.0E-4 * varI_M);
    }
}
