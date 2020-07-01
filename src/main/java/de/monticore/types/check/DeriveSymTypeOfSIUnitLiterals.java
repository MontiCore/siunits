/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.siunitliterals._symboltable.ISIUnitLiteralsScope;
import de.monticore.siunitliterals._visitor.SIUnitLiteralsVisitor;
import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;

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
        traverseSIUnitLiteral(result.getCurrentResult(), node.getSIUnit(), node.getEnclosingScope());
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getSignedNumericLiteral().accept(getRealThis());
        traverseSIUnitLiteral(result.getCurrentResult(), node.getSIUnit(), node.getEnclosingScope());
    }

    private void traverseSIUnitLiteral(SymTypeExpression literalType, ASTSIUnit astsiUnit, ISIUnitLiteralsScope enclosingScope) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(SIUnitsPrettyPrinter.prettyprint(astsiUnit), getScope(enclosingScope));
        if (siunitType instanceof SymTypeOfSIUnit)
            result.setCurrentResult(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                    literalType, siunitType, getScope(enclosingScope)));
        else // case for siunit m/m
            result.setCurrentResult(literalType);
    }
}
