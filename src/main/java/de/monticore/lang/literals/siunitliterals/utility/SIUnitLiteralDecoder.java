/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.literals.siunitliterals.utility;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.siunits.utility.UnitFactory;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

/**
 * This class is intended to decode the {@link java.lang.Number} and double value
 * from a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral} or
 * a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral}
 * from the de.monticore.lang.literals.SIUnitLiterals grammar. It is also meant to
 * calculate the value of a SIUnitLiteral, meaning valueOf(1 km) = 1000.
 * This class can be initialized with a {@link de.monticore.MCCommonLiteralsPrettyPrinter} to prettyprint
 * your own custom {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral} or
 * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
 */
public class SIUnitLiteralDecoder {

    private NumberDecoder decoder;

    /**
     * Constructor with the standard {@link de.monticore.MCCommonLiteralsPrettyPrinter}
     */
    public SIUnitLiteralDecoder() {
        this.decoder = new NumberDecoder();
    }

    /**
     * Constructor with the PrettyPrinter which extends the standard {@link de.monticore.MCCommonLiteralsPrettyPrinter}
     */
    public SIUnitLiteralDecoder(MCCommonLiteralsPrettyPrinter prettyPrinter) {
        this.decoder = new NumberDecoder(prettyPrinter);
    }

    /**
     * Calculates the value of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral}, meaning valueOf(1 km) = 1000.
     */
    public double valueOf(ASTSIUnitLiteral lit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(UnitFactory.createBaseUnit(unit));
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral}, meaning valueOf(1 km) = 1000.
     */
    public double valueOf(ASTSignedSIUnitLiteral lit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(UnitFactory.createBaseUnit(unit));
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral}, meaning valueOf(1 km) = 1000.
     */
    public double valueOf(ASTSIUnitLiteral lit, Unit asUnit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(asUnit);
        return converter.convert(number);
    }

    /**
     * Calculates the value of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral}, meaning valueOf(1 km) = 1000.
     */
    public double valueOf(ASTSignedSIUnitLiteral lit, Unit asUnit) {
        double number = decoder.getDouble(getNumber(lit));
        Unit unit = UnitFactory.createUnit(lit.getSIUnit());
        UnitConverter converter = unit.getConverterTo(asUnit);
        return converter.convert(number);
    }

    /**
     * Returns the number of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral} as double,
     * meaning getDouble(3 km) = 3.
     */
    public double getDouble(ASTSIUnitLiteral lit) {
        return decoder.getDouble(lit.getNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral} as double,
     * meaning getDouble(3 km) = 3.
     */
    public double getDouble(ASTSignedSIUnitLiteral lit) {
        return decoder.getDouble(lit.getSignedNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral} as a {@link java.lang.Number},
     * meaning getDouble(3 km) = 3.
     */
    public Number getNumber(ASTSIUnitLiteral lit) {
        return decoder.decode(lit.getNumericLiteral());
    }

    /**
     * Returns the number of a {@link de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral} as a {@link java.lang.Number},
     * meaning getDouble(3 km) = 3.
     */
    public Number getNumber(ASTSignedSIUnitLiteral lit) {
        return decoder.decode(lit.getSignedNumericLiteral());
    }
}
