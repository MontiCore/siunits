package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._visitor.SIUnitLiteralsVisitor;

public class DeriveSymTypeOfSIUnitLiteralsSIUnitOnly extends DeriveSymTypeOfMCCommonLiterals
        implements SIUnitLiteralsVisitor {

    private IExpressionsBasisScope enclosingScope;
    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    SIUnitLiteralsVisitor realThis = this;

    @Override
    public void setRealThis(SIUnitLiteralsVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitLiteralsVisitor getRealThis() {
        return realThis;
    }

    public void setScope(IExpressionsBasisScope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    // visit methods

    @Override
    public void traverse(ASTSIUnitLiteral node) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(node.getUn().toString(), this.enclosingScope);
        result.setLast(siunitType);
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(node.getUn().toString(), this.enclosingScope);
        result.setLast(siunitType);
    }
}
