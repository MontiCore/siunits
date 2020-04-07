package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._visitor.SIUnitLiteralsVisitor;
import de.monticore.lang.siunits.siunits._ast.ASTSIUnit;

public class DeriveSymTypeOfSIUnitLiterals extends DeriveSymTypeOfMCCommonLiterals
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
        node.getNum().accept(getRealThis());
        traverseSIUnitLiteral(result.getLast(), node.getUn());
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getNum().accept(getRealThis());
        traverseSIUnitLiteral(result.getLast(), node.getUn());
    }

    private void traverseSIUnitLiteral(SymTypeExpression literalType, ASTSIUnit astsiUnit) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(astsiUnit.toString(), this.enclosingScope);
        if (siunitType instanceof SymTypeOfSIUnit)
            result.setLast(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                    literalType, siunitType, this.enclosingScope));
        else // case for siunit m/m
            result.setLast(literalType);
    }
}
