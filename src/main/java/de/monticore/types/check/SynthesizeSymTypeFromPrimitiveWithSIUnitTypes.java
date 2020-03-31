package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.types.primitivewithsiunittypes._visitor.PrimitiveWithSIUnitTypesVisitor;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.siunittypes._visitor.SIUnitTypesVisitor;
import de.se_rwth.commons.logging.Log;

import static de.monticore.types.check.SymTypeExpressionHelper.isNumericType;

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
        SymTypeExpression primitiveType = null;
        SymTypeExpression siunitType = null;
        node.getMCPrimitiveType().accept(getRealThis());
        if (!lastResult.isPresentLast() || !isNumericType(lastResult.getLast())) {
            Log.error("0x"); // TODO
        }
        primitiveType = lastResult.getLast();
        lastResult.reset();
        node.getSIUnitType().accept(getRealThis());
        if (!lastResult.isPresentLast()){
            Log.error("0x"); // TODO
        }
        siunitType = lastResult.getLast();
        lastResult.setLast(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType((SymTypeConstant) primitiveType, siunitType, scope));
    }
}