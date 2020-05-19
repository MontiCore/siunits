package de.monticore.types.check;

import de.monticore.lang.types.customprimitivewithsiunittypes._ast.ASTCustomPrimitiveWithSIUnitType;
import de.monticore.lang.types.customprimitivewithsiunittypes._visitor.CustomPrimitiveWithSIUnitTypesVisitor;

/**
 * Visitor for Derivation of SymType from PrimitiveWithSIUnitTypes
 * i.e. for
 * types/PrimitiveWithSIUnitTypes.mc4
 */
public class SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes extends SynthesizeSymTypeFromPrimitiveWithSIUnitTypes
        implements CustomPrimitiveWithSIUnitTypesVisitor {

    CustomPrimitiveWithSIUnitTypesVisitor realThis = this;

    @Override
    public void setRealThis(CustomPrimitiveWithSIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public CustomPrimitiveWithSIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void traverse(ASTCustomPrimitiveWithSIUnitType node) {
        traversePrimitiveWithSIUnitType(node);
    }
}
