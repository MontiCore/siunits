package de.monticore.lang.siunits.utility;

import javax.measure.converter.LogConverter;
import javax.measure.converter.MultiplyConverter;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.unit.*;

public class UnitFactory {

    private static UnitFactory instance = null;

    private static UnitFactory getInstance() {
        if (instance == null) {
            instance = new UnitFactory();
            init();
        }
        return instance;
    }

    public static Unit createStandardUnit(String s) {
        return getInstance()._createStandardUnit(s);
    }

    private Unit _createStandardUnit(String s) {
        return _createUnit(s).getStandardUnit();
    }

    public static Unit createUnit(String s) {
        return getInstance()._createUnit(s);
    }

    private Unit _createUnit(String s) {
        if ("1".equals(s))
            return Dimensionless.UNIT;
        String str = s
                .replace("1/","(m/m)/");
        return Unit.valueOf(str);
    }

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

    public static String printFormatted(String s) {
        return getInstance()._printFormatted(s);
    }

    private String _printFormatted(String s) {
        return printUnit(createUnit(s));
    }

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
