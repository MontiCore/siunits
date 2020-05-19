package de.monticore.types.check;

import de.monticore.lang.siunits.utility.UnitPrettyPrinter;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitTypeInt;
import de.monticore.lang.types.primitivewithsiunittypes._visitor.PrimitiveWithSIUnitTypesVisitor;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitType;
import de.se_rwth.commons.logging.Log;

import static de.monticore.types.check.TypeCheck.*;

/**
 * Visitor for Derivation of SymType from PrimitiveWithSIUnitTypes
 * i.e. for
 * types/PrimitiveWithSIUnitTypes.mc4
 */
public class SynthesizeSymTypeFromPrimitiveWithSIUnitTypes extends SynthesizeSymTypeFromMCBasicTypes
        implements PrimitiveWithSIUnitTypesVisitor {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    PrimitiveWithSIUnitTypesVisitor realThis = this;

    @Override
    public void setRealThis(PrimitiveWithSIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public PrimitiveWithSIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void endVisit(ASTSIUnitType node) {
        typeCheckResult.setLast(SIUnitSymTypeExpressionFactory.createSIUnit(UnitPrettyPrinter.printUnit(node.getSIUnit()), getScope(node.getEnclosingScope())));
    }

    @Override
    public void traverse(ASTPrimitiveWithSIUnitType node) {
        traversePrimitiveWithSIUnitType(node);
    }

    protected void traversePrimitiveWithSIUnitType(ASTPrimitiveWithSIUnitTypeInt node) {
        SymTypeExpression numericType = null;
        SymTypeExpression siunitType = null;

        node.getPrimitiveType().accept(getRealThis());
        if (!typeCheckResult.isPresentLast() || !isNumericType(typeCheckResult.getLast())) {
            Log.error("0xA0495");
        }
        numericType = typeCheckResult.getLast();
        typeCheckResult.reset();
        node.getSiunitType().accept(getRealThis());
        if (!typeCheckResult.isPresentLast()){
            Log.error("0xA0496");
        }
        siunitType = typeCheckResult.getLast();
        typeCheckResult.setLast(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(numericType, siunitType, getScope(node.getEnclosingScope())));
    }

    /**
     * test if the expression is of numeric type (double, float, long, int, char, short, byte)
     */
    private boolean isNumericType(SymTypeExpression type) {
        return (isDouble(type) || isFloat(type) ||
                isLong(type) || isInt(type) ||
                isChar(type) || isShort(type) ||
                isByte(type));
    }
}
