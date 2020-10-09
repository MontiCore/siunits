/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunitliterals.utility;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.siunits.utility.UnitFactory;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

/**
 * This class is intended to getNumber the {@link java.lang.Number} and double value
 * from a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} or
 * a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}
 * from the de.monticore.SIUnitLiterals grammar. It is also meant to
 * calculate the value of a SIUnitLiteral, meaning valueOf(1 km) = 1000.
 * This class can be initialized with a {@link de.monticore.MCCommonLiteralsPrettyPrinter} to prettyprint
 * your own custom {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral} or
 * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
 *
 * It also extracts the {@link javax.measure.unit.Unit} from {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral}
 * or a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}.
 */
public class SIUnitLiteralDecoder {

    private MCCommonLiteralsDecoder decoder;
    private static MCCommonLiteralsPrettyPrinter prettyPrinter_ = new MCCommonLiteralsPrettyPrinter(new IndentPrinter());

    /**
     * Constructor with the standard {@link de.monticore.MCCommonLiteralsPrettyPrinter}
     */
    public SIUnitLiteralDecoder() {
        this.decoder = new MCCommonLiteralsDecoder(prettyPrinter_);
    }

    /**
     * Constructor with the PrettyPrinter which extends the standard {@link de.monticore.MCCommonLiteralsPrettyPrinter}
     */
    public SIUnitLiteralDecoder(MCCommonLiteralsPrettyPrinter prettyPrinter) {
        this.decoder = new MCCommonLiteralsDecoder(prettyPrinter);
    }

    public static void setPrettyPrinter(MCCommonLiteralsPrettyPrinter prettyPrinter) {
        SIUnitLiteralDecoder.prettyPrinter_ = prettyPrinter;
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as Base Unit
     * meaning valueOf(1 km) = 1000, valueOf(1 l) = 0.001 (1 l = 1 dm^3 = 0.001 m^3).
     */
    public static double valueOf(ASTSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getValue(lit);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as Base Unit
     * meaning valueOf(1 km) = 1000, valueOf(1 l) = 0.001 (1 l = 1 dm^3 = 0.001 m^3).
     */
    public double getValue(ASTSIUnitLiteral lit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(UnitFactory.createBaseUnit(unit));
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as Base Unit
     * meaning valueOf(1 km) = 1000, valueOf(1 l) = 0.001 (1 l = 1 dm^3 = 0.001 m^3).
     */
    public static double valueOf(ASTSignedSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getValue(lit);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as Base Unit
     * meaning valueOf(1 km) = 1000, valueOf(1 l) = 0.001 (1 l = 1 dm^3 = 0.001 m^3).
     */
    public double getValue(ASTSignedSIUnitLiteral lit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(UnitFactory.createBaseUnit(unit));
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral}
     * as the Unit given, meaning valueOf(1 km, m) = 1000, valueOf(1 km, mm) = 1000000.
     */
    public static double valueOf(ASTSIUnitLiteral lit, Unit asUnit) {
        return (new SIUnitLiteralDecoder()).getValue(lit, asUnit);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral}
     * as the Unit given, meaning valueOf(1 km, m) = 1000, valueOf(1 km, mm) = 1000000.
     */
    public double getValue(ASTSIUnitLiteral lit, Unit asUnit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(asUnit);
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}
     * as the Unit given, meaning valueOf(1 km, m) = 1000, valueOf(1 km, mm) = 1000000.
     */
    public static double valueOf(ASTSignedSIUnitLiteral lit, Unit asUnit) {
        return (new SIUnitLiteralDecoder()).getValue(lit, asUnit);
    }

    /**
     * Calculates the value of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}
     * as the Unit given, meaning valueOf(1 km, m) = 1000, valueOf(1 km, mm) = 1000000.
     */
    public double getValue(ASTSignedSIUnitLiteral lit, Unit asUnit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(asUnit);
        return converter.convert(number);
    }

    /**
     * Converts the double value given with the converter between two compatible units,
     * meaning valueOf(1, m, km) = 0.001, valueOf(1, m^3, l) = 1000.
     */
    public static double valueOf(double val, Unit fromUnit, Unit asUnit) {
        return (new SIUnitLiteralDecoder()).getValue(val, fromUnit, asUnit);
    }

    /**
     * Converts the double value given with the converter between two compatible units,
     * meaning valueOf(1, m, km) = 0.001, valueOf(1, m^3, l) = 1000.
     */
    public double getValue(double val, Unit fromUnit, Unit asUnit) {
        UnitConverter converter = fromUnit.getConverterTo(asUnit);
        return converter.convert(val);
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as double,
     * meaning doubleOf(3 km) = 3.
     */
    public static double doubleOf(ASTSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getDouble(lit);
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as double,
     * meaning doubleOf(3 km) = 3.
     */
    public double getDouble(ASTSIUnitLiteral lit) {
        return decoder.getDouble(lit.getNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as double,
     * meaning doubleOf(3 km) = 3.
     */
    public static double doubleOf(ASTSignedSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getDouble(lit);
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as double,
     * meaning doubleOf(3 km) = 3.
     */
    public double getDouble(ASTSignedSIUnitLiteral lit) {
        return decoder.getDouble(lit.getSignedNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as a {@link java.lang.Number},
     * meaning doubleOf(3 km) = 3.
     */
    public static Number numberOf(ASTSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getNumber(lit);
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral} as a {@link java.lang.Number},
     * meaning doubleOf(3 km) = 3.
     */
    public Number getNumber(ASTSIUnitLiteral lit) {
        return decoder.getNumber(lit.getNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as a {@link java.lang.Number},
     * meaning doubleOf(3 km) = 3.
     */
    public static Number numberOf(ASTSignedSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getNumber(lit);
    }

    /**
     * Returns the number of a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral} as a {@link java.lang.Number},
     * meaning doubleOf(3 km) = 3.
     */
    public Number getNumber(ASTSignedSIUnitLiteral lit) {
        return decoder.getNumber(lit.getSignedNumericLiteral());
    }

    /**
     * Returns the {@link javax.measure.unit.Unit} from a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral}
     * meaning getUnit(3 km) = km.
     */
    public static Unit unitOf(ASTSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getUnit(lit);
    }

    /**
     * Returns the {@link javax.measure.unit.Unit} from a {@link de.monticore.siunitliterals._ast.ASTSIUnitLiteral}
     * meaning getUnit(3 km) = km.
     */
    public Unit getUnit(ASTSIUnitLiteral lit) {
        return UnitFactory.createUnit(lit.getSIUnit());
    }

    /**
     * Returns the {@link javax.measure.unit.Unit} from a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}
     * meaning getUnit(3 km) = km.
     */
    public static Unit unitOf(ASTSignedSIUnitLiteral lit) {
        return (new SIUnitLiteralDecoder()).getUnit(lit);
    }

    /**
     * Returns the {@link javax.measure.unit.Unit} from a {@link de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral}
     * meaning getUnit(3 km) = km.
     */
    public Unit getUnit(ASTSignedSIUnitLiteral lit) {
        return UnitFactory.createUnit(lit.getSIUnit());
    }
}
