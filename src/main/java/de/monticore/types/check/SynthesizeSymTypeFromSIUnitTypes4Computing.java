/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4ComputingInt;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingHandler;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingTraverser;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.logging.Log;

import static de.monticore.types.check.TypeCheck.*;

/**
 * Visitor for Derivation of SymType from SIUnitTypes4Computing
 * i.e. for
 * types/SIUnitTypes4Computing.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes4Computing extends AbstractSynthesizeFromType
        implements SIUnitTypes4ComputingHandler {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    protected SIUnitTypes4ComputingTraverser traverser;

    @Override
    public SIUnitTypes4ComputingTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(SIUnitTypes4ComputingTraverser traverser) {
        this.traverser = traverser;
    }

//    @Override
//    public void traverse(ASTSIUnit node) {
//        typeCheckResult.setResult(SIUnitSymTypeExpressionFactory.createSIUnit(UnitPrettyPrinter.printUnit(node), getScope(node.getEnclosingScope())));
//    }

    @Override
    public void traverse(ASTSIUnitType4Computing node) {
        traverseSIUnitType4Computing(node);
    }

    protected void traverseSIUnitType4Computing(ASTSIUnitType4ComputingInt node) {
        SymTypeExpression numericType = null;
        SymTypeExpression siunitType = null;

        node.getMCPrimitiveType().accept(getTraverser());
        if (!typeCheckResult.isPresentResult() || !isNumericType(typeCheckResult.getResult())) {
            Log.error("0xA0495");
        }
        numericType = typeCheckResult.getResult();
        typeCheckResult.reset();

        siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(
                UnitPrettyPrinter.printUnit(node.getSIUnit()), getScope(node.getEnclosingScope()));
        typeCheckResult.setResult(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(numericType, siunitType, getScope(node.getEnclosingScope())));
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
