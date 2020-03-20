package de.monticore.lang.siunits.utility;

import javax.measure.converter.LogConverter;
import javax.measure.converter.MultiplyConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.unit.*;

/**
 * This class is intended to handle the Units from the jscience package javax.measure.unit.
 * It should be the only class to create Units or to print them. It is mainly used to handle
 * SIUnits from the grammar de.monticore.lang.siunits.SIUnits.mc4
 */
public class UnitFactory {

    private static UnitFactory instance = null;

    private static UnitFactory getInstance() {
        if (instance == null) {
            instance = new UnitFactory();
            init();
        }
        return instance;
    }

    /**
     * Create a javax.measure.unit.Unit from a String, e.g. from km/(Ohm*s^2)
     */
    public static Unit createUnit(String s) {
        return getInstance()._createUnit(s);
    }

    private Unit _createUnit(String s) {
        if ("1".equals(s))
            return Dimensionless.UNIT;
        String str = s
                .replace("1/", "(m/m)/");
        return Unit.valueOf(str);
    }

    /**
     * Create a javax.measure.unit.Unit from a String, e.g. from km/(Ohm*s^2) and returns without prefixes
     */
    public static Unit createStandardUnit(String s) {
        return getInstance()._createStandardUnit(s);
    }

    private Unit _createStandardUnit(String s) {
        return _createUnit(s).getStandardUnit();
    }

    /**
     * Prints a unit in a former way
     */
    public static String printUnit(Unit unit) {
        return getInstance()._printUnit(unit);
    }

    private String _printUnit(Unit unit) {
        if ("".equals(unit.toString()))
            return "1";
        return unit.toString()
                .replace("²", "^2")
                .replace("³", "^3")
                .replace("·", "*")
                .replace("℃", "°C");
    }

    /**
     * Prints a unit in a former way from a String, e.g. m*km^-1/g -> 1/kg
     */
    public static String printFormatted(String s) {
        return getInstance()._printFormatted(s);
    }

    private String _printFormatted(String s) {
        return printUnit(createUnit(s));
    }

    /**
     * Prints a unit in a former way from a String without prefixes, e.g. m*km^-1/g -> 1/g
     */
    public static String printFormattedToStandard(String s) {
        return getInstance()._printFormattedToStandard(s);
    }

    private String _printFormattedToStandard(String s) {
        return printUnit(createStandardUnit(s));
    }

    private static void init() {
        Unit<Length> AU = SI.METRE.times(1495978707).times(100);
        Unit<Dimensionless> NEPER = Unit.ONE.transform((new LogConverter(2.718281828459045D)));
        Unit<Dimensionless> BEL = Unit.ONE.transform((new LogConverter(10.0D)));
        Unit DEG = Unit.valueOf("°");
        Unit OHM = SI.OHM;
        UnitFormat.getInstance().label(AU, "Au");
        UnitFormat.getInstance().label(NEPER, "Np");
        UnitFormat.getInstance().label(BEL, "B");
        UnitFormat.getInstance().label(DEG, "deg");
        UnitFormat.getInstance().label(OHM, "Ohm");

        // Add prefixes for Ohm
        String[] prefixes = new String[]{"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "d", "c", "m", "micro", "n", "p", "f", "a", "z", "y"};
        double[] converters = {1.0E24D, 1.0E21D, 1.0E18D, 1.0E15D, 1.0E12D, 1.0E9D, 1.0E6D, 1.0E3D, 1.0E2D, 1.0E1D, 1.0E-1D, 1.0E-2D, 1.0E-3D, 1.0E-6D, 1.0E-9D, 1.0E-12D, 1.0E-15D, 1.0E-18D, 1.0E-21D, 1.0E-24D};
        for (int i = 0; i < prefixes.length; i++) {
            Unit prefixUnit = OHM.transform(new MultiplyConverter(converters[i]));
            UnitFormat.getInstance().label(prefixUnit, prefixes[i] + "Ohm");
        }
    }
}
