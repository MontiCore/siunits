/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.siunitliterals._symboltable.ISIUnitLiteralsScope;
import de.monticore.siunitliterals._visitor.SIUnitLiteralsHandler;
import de.monticore.siunitliterals._visitor.SIUnitLiteralsTraverser;
import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.se_rwth.commons.logging.Log;

/**
 * This class is used to derive the type of an SIUnitLiteral
 */
public class DeriveSymTypeOfSIUnitLiterals implements SIUnitLiteralsHandler {

    protected SIUnitLiteralsTraverser traverser;

    @Override
    public SIUnitLiteralsTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(SIUnitLiteralsTraverser traverser) {
        this.traverser = traverser;
    }

    protected TypeCheckResult typeCheckResult;

    public void setTypeCheckResult(TypeCheckResult result) {
        this.typeCheckResult = result;
    }

    @Override
    public void traverse(ASTSIUnitLiteral node) {
        node.getNumericLiteral().accept(getTraverser());
        traverseSIUnitLiteral(typeCheckResult.getResult(), node.getSIUnit(), node.getEnclosingScope());
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getSignedNumericLiteral().accept(getTraverser());
        traverseSIUnitLiteral(typeCheckResult.getResult(), node.getSIUnit(), node.getEnclosingScope());
    }

    private void traverseSIUnitLiteral(SymTypeExpression literalType, ASTSIUnit astsiUnit, ISIUnitLiteralsScope enclosingScope) {
        SymTypeExpression siunitType = SIUnitSymTypeExpressionFactory._deprecated_createSIUnit(SIUnitsPrettyPrinter.prettyprint(astsiUnit), getScope(enclosingScope));
        if (siunitType instanceof SymTypeOfSIUnit)
            typeCheckResult.setResult(SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(
                    literalType, siunitType, getScope(enclosingScope)));
        else // case for siunit m/m
            typeCheckResult.setResult(literalType);
    }

    public IBasicSymbolsScope getScope (ISIUnitLiteralsScope expressionsBasisScope){
        // is accepted only here, decided on 07.04.2020
        if(!(expressionsBasisScope instanceof IBasicSymbolsScope)){
            Log.error("0xAE0307 the enclosing scope of the expression does not implement the interface IBasicSymbolsScope");
        }
        // is accepted only here, decided on 07.04.2020
        return (IBasicSymbolsScope) expressionsBasisScope;
    }
}
