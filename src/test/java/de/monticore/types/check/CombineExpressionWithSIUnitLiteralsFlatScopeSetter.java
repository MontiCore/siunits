/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals._symboltable.ICombineExpressionsWithSIUnitLiteralsScope;
import de.monticore.expressions.combineexpressionswithsiunitliterals._visitor.CombineExpressionsWithSIUnitLiteralsVisitor;

public class CombineExpressionWithSIUnitLiteralsFlatScopeSetter extends FlatExpressionScopeSetterAbs
        implements CombineExpressionsWithSIUnitLiteralsVisitor {

    public CombineExpressionWithSIUnitLiteralsFlatScopeSetter(ICombineExpressionsWithSIUnitLiteralsScope scope) {
        super(scope);
    }

    // ************************* Visitor *************************

    private CombineExpressionsWithSIUnitLiteralsVisitor realThis = this;

    @Override
    public CombineExpressionsWithSIUnitLiteralsVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(CombineExpressionsWithSIUnitLiteralsVisitor realThis) {
        this.realThis = realThis;
    }
}
