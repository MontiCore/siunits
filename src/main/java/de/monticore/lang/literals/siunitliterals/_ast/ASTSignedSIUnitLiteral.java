package de.monticore.lang.literals.siunitliterals._ast;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.siunits.siunits.prettyprint.SIUnitPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class ASTSignedSIUnitLiteral extends ASTSignedSIUnitLiteralTOP {
    public double getNumber() {
        MCCommonLiteralsPrettyPrinter literalsPrinter = new MCCommonLiteralsPrettyPrinter(new IndentPrinter());
        return Double.parseDouble(literalsPrinter.prettyprint(getNum()));
    }

    public String getUnit() {
        return SIUnitPrettyPrinter.prettyprint(getUn());
    }

    public String toString() {
        return "" + getNumber() + " " + getUnit();
    }
}
