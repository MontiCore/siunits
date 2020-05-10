/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.siunits.utility;

import de.monticore.lang.siunits._ast.ASTSIUnit;
import de.monticore.lang.siunits.prettyprint.SIUnitWithBracketsPrettyPrinter;

import javax.measure.unit.Unit;


/**
 * This class is intended to provide the String representation of Units.
 */
public class UnitPrettyPrinter {

    /**
     * Prints the unit of a String in a former way, e.g. m*km^-1/g -> 1/kg
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
     * @param unit the unit as String to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(String unit) {
        return getInstance()._printStandardUnit(unit);
    }

    private String _printStandardUnit(String unit) {
        return _printStandardUnit(UnitFactory.createUnit(unit));
    }

    /**
     * Prints a {@link de.monticore.lang.siunits._ast.ASTSIUnit} in a former way, e.g. m*km^-1/g -> 1/kg
     * @param siunit the {@link de.monticore.lang.siunits._ast.ASTSIUnit} to print
     * @return the String representation of the unit
     */
    public static String printUnit(ASTSIUnit siunit) {
        return getInstance()._printUnit(siunit);
    }

    private String _printUnit(ASTSIUnit siunit) {
        return _printUnit(SIUnitWithBracketsPrettyPrinter.prettyprint(siunit));
    }

    /**
     * Prints a the standard unit of a {@link de.monticore.lang.siunits._ast.ASTSIUnit} in a former way, e.g. km*kg/g -> m
     * @param siunit the {@link de.monticore.lang.siunits._ast.ASTSIUnit} to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(ASTSIUnit siunit) {
        return getInstance()._printStandardUnit(siunit);
    }

    private String _printStandardUnit(ASTSIUnit siunit) {
        return _printStandardUnit(UnitFactory.createUnit(siunit));
    }

    /**
     * Prints a {@link javax.measure.unit.Unit} in a former way, e.g. m*km^-1/g -> 1/kg
     * @param unit the {@link javax.measure.unit.Unit} to print
     * @return the String representation of the unit
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
     * Prints a the standard unit of a {@link javax.measure.unit.Unit} in a former way, e.g. km*kg/g -> m
     * @param unit the {@link javax.measure.unit.Unit} to print
     * @return the String representation of the unit's standard unit
     */
    public static String printStandardUnit(Unit unit) {
        return getInstance()._printStandardUnit(unit);
    }

    private String _printStandardUnit(Unit unit) {
        return _printUnit(unit.getStandardUnit());
    }

    private static UnitPrettyPrinter instance;

    private static UnitPrettyPrinter getInstance() {
        if (instance == null) {
            instance = new UnitPrettyPrinter();
        }
        return instance;
    }
}
