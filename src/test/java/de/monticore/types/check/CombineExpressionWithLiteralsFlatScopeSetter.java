/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._symboltable.ICombineExpressionsWithLiteralsScope;
import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsVisitor;

public class CombineExpressionWithLiteralsFlatScopeSetter extends FlatExpressionScopeSetterAbs
        implements CombineExpressionsWithLiteralsVisitor {

    public CombineExpressionWithLiteralsFlatScopeSetter(ICombineExpressionsWithLiteralsScope scope) {
        super(scope);
    }

    // ************************* Visitor *************************

    private CombineExpressionsWithLiteralsVisitor realThis = this;

    @Override
    public CombineExpressionsWithLiteralsVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(CombineExpressionsWithLiteralsVisitor realThis) {
        this.realThis = realThis;
    }
}
