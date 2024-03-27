/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.siunittypes4math.SIUnitTypes4MathMill;
import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.monticore.siunittypes4math._visitor.SIUnitTypes4MathHandler;
import de.monticore.siunittypes4math._visitor.SIUnitTypes4MathTraverser;

/**
 * Visitor for Derivation of SymType from SIUnitTypes
 * i.e. for
 * types/SIUnitTypes.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes4Math extends AbstractSynthesizeFromType
        implements SIUnitTypes4MathHandler {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    protected SIUnitTypes4MathTraverser traverser;

    @Override
    public SIUnitTypes4MathTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(SIUnitTypes4MathTraverser traverser) {
        this.traverser = traverser;
    }

    // ---------------------------------------------------------- Storage result

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    public TypeCheckResult typeCheckResult = new TypeCheckResult();

    public void init() {
        if (traverser == null)
            traverser = SIUnitTypes4MathMill.traverser();
        typeCheckResult = new TypeCheckResult();
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
    }

    @Override
    public void traverse(ASTSIUnitType node) {
        SymTypeExpression numericType = SymTypeExpressionFactory.createPrimitive("double");
        SymTypeExpression siunitType = null;

        siunitType = SIUnitSymTypeExpressionFactory._deprecated_createSIUnit(
                UnitPrettyPrinter.printUnit(node.getSIUnit()), getScope(node.getEnclosingScope()));

        typeCheckResult.setResult(SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(numericType, siunitType, getScope(node.getEnclosingScope())));
    }

}
