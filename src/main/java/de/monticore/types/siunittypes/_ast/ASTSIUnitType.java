package de.monticore.types.siunittypes._ast;

import javax.measure.unit.Unit;

public class ASTSIUnitType extends ASTSIUnitTypeTOP {
    public Unit getUnit() {
        return getSIUnit().getUnit();
    }

    public String toString() {
        return getSIUnit().toString();
    }
}
