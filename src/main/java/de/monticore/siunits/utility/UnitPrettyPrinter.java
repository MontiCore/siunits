/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits.utility;

import de.monticore.siunits._ast.ASTSIUnit;

import javax.measure.unit.Unit;
import java.util.Arrays;
import java.util.List;


/**
 * This class is intended to provide the String representation of Units.
 */
public class UnitPrettyPrinter {

    /**
     * Prints the unit of a String in a former way, e.g. m*km^-1/g -> 1/kg
     *
     * @param unit as String to print
     * @return the String representation of the unit
     */
    public static String printUnit(String unit) {
        return getInstance()._printUnit(unit);
    }

    private String _printUnit(String unit) {
        return _printUnit(UnitFactory.createUnit(unit));
    }

    /**
     * Prints the standard unit of a String in a former way, e.g. km*kg/g -> m
     *
     * @param unit the unit as String to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(String unit) {
        return getInstance()._printStandardUnit(unit);
    }

    private String _printStandardUnit(String unit) {
        return _printUnit(UnitFactory.createStandardUnit(unit));
    }

    public static String printBaseUnit(String unit) {
        return getInstance()._printBaseUnit(unit);
    }

    private String _printBaseUnit(String unit) {
        return _printUnit(UnitFactory.createBaseUnit(unit));
    }

    public static String printWithoutPrefixes(String unit) {
        return getInstance()._printWithoutPrefixes(unit);
    }

    private String _printWithoutPrefixes(String unit) {
        return _printUnit(UnitFactory.removePrefixes(UnitFactory.createUnit(unit)));
    }

    /**
     * Prints a {@link de.monticore.siunits._ast.ASTSIUnit} in a former way, e.g. m*km^-1/g -> 1/kg
     *
     * @param siunit the {@link de.monticore.siunits._ast.ASTSIUnit} to print
     * @return the String representation of the unit
     */
    public static String printUnit(ASTSIUnit siunit) {
        return getInstance()._printUnit(siunit);
    }

    private String _printUnit(ASTSIUnit siunit) {
        return _printUnit(UnitFactory.createUnit(siunit));
    }

    /**
     * Prints a the standard unit of a {@link de.monticore.siunits._ast.ASTSIUnit} in a former way, e.g. km*kg/g -> m
     *
     * @param siunit the {@link de.monticore.siunits._ast.ASTSIUnit} to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(ASTSIUnit siunit) {
        return getInstance()._printStandardUnit(siunit);
    }

    private String _printStandardUnit(ASTSIUnit siunit) {
        return _printUnit(UnitFactory.createStandardUnit(siunit));
    }

    public static String printBaseUnit(ASTSIUnit siunit) {
        return getInstance()._printBaseUnit(siunit);
    }

    private String _printBaseUnit(ASTSIUnit siunit) {
        return _printUnit(UnitFactory.createBaseUnit(siunit));
    }

    public static String printWithoutPrefixes(ASTSIUnit unit) {
        return getInstance()._printWithoutPrefixes(unit);
    }

    private String _printWithoutPrefixes(ASTSIUnit unit) {
        return _printUnit(UnitFactory.removePrefixes(UnitFactory.createUnit(unit)));
    }

    /**
     * Prints a {@link javax.measure.unit.Unit} in a former way, e.g. m*km^-1/g -> 1/kg
     *
     * @param unit the {@link javax.measure.unit.Unit} to print
     * @return the String representation of the unit
     */
    public static String printUnit(Unit unit) {
        return getInstance()._printUnit(unit);
    }

    private String _printUnit(Unit unit) {
        if ("".equals(unit.toString()))
            return "1";
        String res = unit.toString()
                .replace("²", "^2")
                .replace("³", "^3")
                .replace("·", "*")
                .replace("℃", "°C");

        if (res.contains("/")) {
            String numerator = res.split("/")[0];
            String denominator = res.split("/")[1];
            List<String> numerators = Arrays.asList(numerator.split("\\*"));
            List<String> denominators = Arrays.asList(denominator.replace("(", "").replace(")", "")
                    .split("\\*"));
            numerators.sort(String::compareTo);
            denominators.sort(String::compareTo);
            if (denominators.size() == 1)
                return String.join("*", numerators) + "/" + String.join("*", denominators);
            else
                return String.join("*", numerators) + "/(" + String.join("*", denominators) + ")";
        } else {
            return res;
        }
    }

    /**
     * Prints a the standard unit of a {@link javax.measure.unit.Unit} in a former way, e.g. km*kg/g -> m
     *
     * @param unit the {@link javax.measure.unit.Unit} to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(Unit unit) {
        return getInstance()._printStandardUnit(unit);
    }

    private String _printStandardUnit(Unit unit) {
        return _printUnit(UnitFactory.createStandardUnit(unit));
    }

    public static String printBaseUnit(Unit unit) {
        return getInstance()._printBaseUnit(unit);
    }

    private String _printBaseUnit(Unit unit) {
        return _printUnit(UnitFactory.createBaseUnit(unit));
    }

    public static String printWithoutPrefixes(Unit unit) {
        return getInstance()._printWithoutPrefixes(unit);
    }

    private String _printWithoutPrefixes(Unit unit) {
        return _printUnit(UnitFactory.removePrefixes(unit));
    }

    private static UnitPrettyPrinter instance;

    private static UnitPrettyPrinter getInstance() {
        if (instance == null) {
            instance = new UnitPrettyPrinter();
        }
        return instance;
    }
}
