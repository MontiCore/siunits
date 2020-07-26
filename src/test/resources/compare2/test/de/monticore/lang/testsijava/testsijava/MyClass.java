package test.de.monticore.lang.testsijava.testsijava;

public class MyClass {

    double var = (double) 3.6 * (3.0);
    int varInt = (int) (3);
    int y = (int) (3);
    int m = (int) (3);
    int km = (int) (3);

    public int method1(int varInt2) {
        int var2 = (int) 1.0E-9 * (4.0E18);
        var2 = 1.0E-9 * (5.0 / (2.0 * 0.005));
        var2 = 1.0E-9 * (0.005);
        var2 *= 1.0E-9 * (varInt2);
        int varS = (int) 2.777777777777778E-4 * (3.0);
        int varI_M = (int) 10.0 * (10.0);
        var2 = 1.0E-9 * ((0.1 * varI_M) / (3600.0 * varS) * (3600.0 * varS));

        return (int) 0.001 * ((0.1 * varI_M));
    }
}
