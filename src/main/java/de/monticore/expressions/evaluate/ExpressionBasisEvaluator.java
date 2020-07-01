/* (c) https://github.com/MontiCore/monticore */
package de.monticore.expressions.evaluate;

import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisVisitor;
import de.monticore.literals.mccommonliterals._ast.ASTMCCommonLiteralsNode;
import de.monticore.literals.mccommonliterals._ast.ASTNumericLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedNumericLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteralsNode;
import de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.siunitliterals.utility.NumberDecoder;
import de.monticore.siunitliterals.utility.SIUnitLiteralDecoder;
import de.monticore.types.basictypesymbols._symboltable.VariableSymbol;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.se_rwth.commons.logging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExpressionBasisEvaluator implements ExpressionsBasisVisitor {
    private ExpressionsBasisVisitor realThis = this;

    public void setRealThis(ExpressionsBasisVisitor realThis) {
        this.realThis = realThis;
    }

    public ExpressionsBasisVisitor getRealThis() {
        return this.realThis;
    }

    Map<FieldSymbol, Double> fieldValues = new HashMap<>();
    Map<VariableSymbol, Double> variableValues = new HashMap<>();

    public void setFieldValues(Map<FieldSymbol, Double> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public void setVariableValues(Map<VariableSymbol, Double> variableValues) {
        this.variableValues = variableValues;
    }

    public ITypeSymbolsScope getScope (IExpressionsBasisScope expressionsBasisScope){
        // is accepted only here, decided on 07.04.2020
        if(!(expressionsBasisScope instanceof ITypeSymbolsScope)){
            Log.error("0xE03017 the enclosing scope of the expression does not implement the interface ITypeSymbolsScope");
        }
        // is accepted only here, decided on 07.04.2020
        return (ITypeSymbolsScope) expressionsBasisScope;
    }

    private EvaluationResult evaluationResult = new EvaluationResult();

    public EvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    @Override
    public void visit(ASTNameExpression node) {
        Optional<FieldSymbol> fieldSymbol = getScope(node.getEnclosingScope()).resolveField(node.getName());
        Optional<VariableSymbol> variableSymbol = getScope(node.getEnclosingScope()).resolveVariable(node.getName());

        if (fieldSymbol.isPresent()) {
            evaluationResult.setResult(this.fieldValues.get(fieldSymbol.get()));
        } else if (variableSymbol.isPresent()) {
            evaluationResult.setResult(this.variableValues.get(variableSymbol.get()));
        } else {
            Log.error("0xE6809644 No such variable or field name");
        }
    }

    @Override
    public void visit(ASTLiteralExpression node) {
        ASTLiteral literal = node.getLiteral();
        if (literal instanceof ASTMCCommonLiteralsNode) {
            NumberDecoder decoder = new NumberDecoder();
            if (literal instanceof ASTNumericLiteral)
                evaluationResult.setResult(decoder.getDouble((ASTNumericLiteral) literal));
            if (literal instanceof ASTSignedNumericLiteral)
                evaluationResult.setResult(decoder.getDouble((ASTSignedNumericLiteral) literal));
        } else if (literal instanceof ASTSIUnitLiteralsNode) {
            SIUnitLiteralDecoder decoder = new SIUnitLiteralDecoder();
            if (literal instanceof ASTSIUnitLiteral)
                evaluationResult.setResult(decoder.valueOf((ASTSIUnitLiteral) literal));
            if (literal instanceof ASTSignedSIUnitLiteral)
                evaluationResult.setResult(decoder.valueOf((ASTSignedSIUnitLiteral) literal));
        }
    }
}
