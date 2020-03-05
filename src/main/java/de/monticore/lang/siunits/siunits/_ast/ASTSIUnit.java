package de.monticore.lang.siunits.siunits._ast;

import de.monticore.lang.siunits.utility.UnitFactory;
import de.monticore.lang.siunits.siunits.prettyprint.SIUnitPrettyPrinter;

import javax.measure.unit.Unit;

public class ASTSIUnit extends ASTSIUnitTOP {

    public Unit getUnit() {
        String unitAsString = SIUnitPrettyPrinter.prettyprint(this);
        return UnitFactory.createUnit(unitAsString);
    }

    public String toString() {
        return UnitFactory.printUnit(getUnit());
    }
}
