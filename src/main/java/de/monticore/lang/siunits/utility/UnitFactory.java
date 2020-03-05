package de.monticore.lang.siunits.utility;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

public class UnitFactory {
    public static Unit createStandardUnit(String s) {
        return createUnit(s).getStandardUnit();
    }

    public static Unit createUnit(String s) {
        if ("1".equals(s))
            return Dimensionless.UNIT;
        String str = s
                .replace("1/","(m/m)/")
                .replace("deg", NonSI.DEGREE_ANGLE.toString());
        return Unit.valueOf(str);
    }

    public static String printUnit(Unit unit) {
        if ("".equals(unit.toString()))
            return "1";
        return unit.toString()
                .replace("²", "^2")
                .replace("³", "^3")
                .replace("·", "*");
    }

    public static String printFormatted(String s) {
        return printUnit(createUnit(s));
    }

    public static String printFormattedToStandard(String s) {
        return printUnit(createStandardUnit(s));
    }
}
