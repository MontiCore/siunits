package de.monticore.types.check;

import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._symboltable.ISIUnitLiteralsScope;
import de.monticore.lang.literals.siunitliterals._visitor.SIUnitLiteralsVisitor;
import de.monticore.lang.siunits._ast.ASTSIUnit;
import de.monticore.lang.siunits.prettyprint.SIUnitsPrettyPrinter;

/**
 * This class is used to derive the type of an SIUnitLiteral
 */
public class DeriveSymTypeOfSIUnitLiterals extends DeriveSymTypeOfMCCommonLiterals
        implements SIUnitLiteralsVisitor {

    SIUnitLiteralsVisitor realThis = this;

    @Override
    public void setRealThis(SIUnitLiteralsVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitLiteralsVisitor getRealThis() {
        return realThis;
    }

    // visit methods

    @Override
    public void traverse(ASTSIUnitLiteral node) {
        node.getNumericLiteral().accept(getRealThis());
        traverseSIUnitLiteral(result.getLast(), node.getSIUnit(), node.getEnclosingScope());
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getSignedNumericLiteral().accept(getRealThis());
        traverseSIUnitLiteral(result.getLast(), node.getSIUnit(), node.getEnclosingScope());
    }

    private void traverseSIUnitLiteral(SymTypeExpression literalType, ASTSIUnit astsiUnit, ISIUnitLiteralsScope enclosingScope) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(SIUnitsPrettyPrinter.prettyprint(astsiUnit), getScope(enclosingScope));
        if (siunitType instanceof SymTypeOfSIUnit)
            result.setLast(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                    literalType, siunitType, getScope(enclosingScope)));
        else // case for siunit m/m
            result.setLast(literalType);
    }
}
