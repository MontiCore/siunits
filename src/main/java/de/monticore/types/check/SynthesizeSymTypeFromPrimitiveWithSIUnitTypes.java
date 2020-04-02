package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.types.primitivewithsiunittypes._visitor.PrimitiveWithSIUnitTypesVisitor;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
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

    public SynthesizeSymTypeFromPrimitiveWithSIUnitTypes(IExpressionsBasisScope scope) {
        super(scope);
    }

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
        lastResult.setLast(SIUnitSymTypeExpressionFactory.createSIUnit(node.toString()));
    }

    @Override
    public void traverse(ASTPrimitiveWithSIUnitType node) {
        SymTypeExpression numericType = null;
        SymTypeExpression siunitType = null;
        node.getMCPrimitiveType().accept(getRealThis());
        if (!lastResult.isPresentLast() || !isNumericType(lastResult.getLast())) {
            Log.error("0x"); // TODO
        }
        numericType = lastResult.getLast();
        lastResult.reset();
        node.getSIUnitType().accept(getRealThis());
        if (!lastResult.isPresentLast()){
            Log.error("0x"); // TODO
        }
        siunitType = lastResult.getLast();
        lastResult.setLast(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType((SymTypeConstant) numericType, siunitType, scope));
    }

    /**
     * test if the expression is of numeric type (double, float, long, int, char, short, byte)
     */
    private static boolean isNumericType(SymTypeExpression type) {
        return (isDouble(type) || isFloat(type) ||
                isLong(type) || isInt(type) ||
                isChar(type) || isShort(type) ||
                isByte(type));
    }
}
