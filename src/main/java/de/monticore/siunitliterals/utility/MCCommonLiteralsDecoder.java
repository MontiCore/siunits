/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunitliterals.utility;

import de.monticore.literals.mccommonliterals._ast.*;
import de.monticore.literals.prettyprint.MCCommonLiteralsFullPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

/**
 * This class is intended to getNumber the {@link java.lang.Number} and double value
 * from a {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral} or
 * a {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}
 * from the de.monticore.literals.MCCommonLiterals grammar.
 * This class can be initialized with a {@link de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter} to prettyprint
 * your own custom {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral} or
 * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
 */
public class MCCommonLiteralsDecoder {

    private static MCCommonLiteralsFullPrettyPrinter mcCommonLiteralsPrettyPrinter_ = new MCCommonLiteralsFullPrettyPrinter(new IndentPrinter());

    private MCCommonLiteralsFullPrettyPrinter mcCommonLiteralsPrettyPrinter;

    /**
     * Constructor with the standard {@link de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter}
     */
    public MCCommonLiteralsDecoder() {
        this(mcCommonLiteralsPrettyPrinter_);
    }

    /**
     * Constructor with the PrettyPrinter which extends the standard {@link de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter}
     */
    public MCCommonLiteralsDecoder(MCCommonLiteralsFullPrettyPrinter mcCommonLiteralsPrettyPrinter) {
        this.mcCommonLiteralsPrettyPrinter = mcCommonLiteralsPrettyPrinter;
    }

    public static void setPrettyPrinter(MCCommonLiteralsFullPrettyPrinter mcCommonLiteralsPrettyPrinter) {
        MCCommonLiteralsDecoder.mcCommonLiteralsPrettyPrinter_ = mcCommonLiteralsPrettyPrinter;
    }

    /**
     * Extract the double value of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral}.
     */
    public static double doubleOf(ASTNumericLiteral lit) {
        return (new MCCommonLiteralsDecoder()).getDouble(lit);
    }

    /**
     * Extract the double value of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral}.
     */
    public double getDouble(ASTNumericLiteral lit) {
        return getDouble(getNumber(lit));
    }

    /**
     * Extract the double value of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
     */
    public static double doubleOf(ASTSignedNumericLiteral lit) {
        return (new MCCommonLiteralsDecoder()).getDouble(lit);
    }

    /**
     * Extract the double value of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
     */
    public double getDouble(ASTSignedNumericLiteral lit) {
        return getDouble(getNumber(lit));
    }

    /**
     * Extract the {@link java.lang.Number} of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral}.
     */
    public static Number numberOf(ASTNumericLiteral lit) {
        return (new MCCommonLiteralsDecoder()).getNumber(lit);
    }

    /**
     * Extract the {@link java.lang.Number} of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral}.
     */
    public Number getNumber(ASTNumericLiteral lit) {
        if (lit instanceof ASTBasicLongLiteral) {
            return (((ASTBasicLongLiteral) lit).getValue());
        } else if (lit instanceof ASTBasicFloatLiteral) {
            return ((ASTBasicFloatLiteral) lit).getValue();
        } else if (lit instanceof ASTBasicDoubleLiteral) {
            return ((ASTBasicDoubleLiteral) lit).getValue();
        } else if (lit instanceof ASTNatLiteral) {
            return ((ASTNatLiteral) lit).getValue();
        } else {
            String numberAsString = mcCommonLiteralsPrettyPrinter.prettyprint(lit);
            Number res = null;
            try {
                res = Double.valueOf(numberAsString);
            } catch (NumberFormatException e) {
                Log.error("0xAE104 PrettyPrinter misconfigured, provide a custom prettyprinter for your own NumericalLiteral");
            }
            return res;
        }
    }

    /**
     * Extract the {@link java.lang.Number} of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
     */
    public static Number numberOf(ASTSignedNumericLiteral lit) {
        return (new MCCommonLiteralsDecoder()).getNumber(lit);
    }

    /**
     * Extract the {@link java.lang.Number} of a
     * {@link de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral}.
     */
    public Number getNumber(ASTSignedNumericLiteral lit) {
        if (lit instanceof ASTSignedBasicLongLiteral) {
            return (((ASTSignedBasicLongLiteral) lit).getValue());
        } else if (lit instanceof ASTSignedBasicFloatLiteral) {
            return ((ASTSignedBasicFloatLiteral) lit).getValue();
        } else if (lit instanceof ASTSignedBasicDoubleLiteral) {
            return ((ASTSignedBasicDoubleLiteral) lit).getValue();
        } else if (lit instanceof ASTSignedNatLiteral) {
            return ((ASTSignedNatLiteral) lit).getValue();
        } else {
            String numberAsString = mcCommonLiteralsPrettyPrinter.prettyprint(lit);
            Number res = null;
            try {
                res = Double.valueOf(numberAsString);
            } catch (NumberFormatException e) {
                Log.error("0xAE103 PrettyPrinter misconfigured, provide a custom prettyprinter for your own NumericalLiteral");
            }
            return res;
        }
    }

    /**
     * Calculate the double value of a {@link java.lang.Number}.
     */
    public double getDouble(Number num) {
        if (num instanceof Integer) {
            return Double.valueOf("" + num.intValue());
        } else if (num instanceof Short) {
            return Double.valueOf("" + num.shortValue());
        } else if (num instanceof Long) {
            return Double.valueOf("" + num.longValue());
        } else if (num instanceof Byte) {
            return Double.valueOf("" + num.byteValue());
        } else if (num instanceof Float) {
            return Double.valueOf("" + num.floatValue());
        } else {
            return Double.valueOf("" + num.doubleValue());
        }
    }
}
