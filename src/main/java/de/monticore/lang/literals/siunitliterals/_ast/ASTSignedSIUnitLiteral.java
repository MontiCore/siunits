package de.monticore.lang.literals.siunitliterals._ast;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.siunits.siunits.prettyprint.SIUnitPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

import java.text.NumberFormat;
import java.text.ParseException;

public class ASTSignedSIUnitLiteral extends ASTSignedSIUnitLiteralTOP {
    public Number getNumber() {
        MCCommonLiteralsPrettyPrinter literalsPrinter = new MCCommonLiteralsPrettyPrinter(new IndentPrinter());
        Number num = null;
        try {
            num = NumberFormat.getInstance().parse(literalsPrinter.prettyprint(getNum()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return num;
    }

    public String getUnit() {
        return SIUnitPrettyPrinter.prettyprint(getUn());
    }

    public String toString() {
        return "" + getNumber() + " " + getUnit();
    }
}
