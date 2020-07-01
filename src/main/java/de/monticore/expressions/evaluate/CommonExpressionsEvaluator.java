/* (c) https://github.com/MontiCore/monticore */
package de.monticore.expressions.evaluate;

import de.monticore.expressions.commonexpressions._ast.*;
import de.monticore.expressions.commonexpressions._visitor.CommonExpressionsVisitor;

public class CommonExpressionsEvaluator implements CommonExpressionsVisitor {
    private CommonExpressionsVisitor realThis = this;

    public void setRealThis(CommonExpressionsVisitor realThis) {
        this.realThis = realThis;
    }

    public CommonExpressionsVisitor getRealThis() {
        return this.realThis;
    }


    private EvaluationResult evaluationResult = new EvaluationResult();

    public EvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }


    @Override
    public void visit(ASTCallExpression node) {
        // TODO
    }

    @Override
    public void visit(ASTFieldAccessExpression node) {
        // TODO
    }

    @Override
    public void visit(ASTPlusPrefixExpression node) {
        // TODO

    }

    @Override
    public void visit(ASTMinusPrefixExpression node) {
        // TODO

    }

    @Override
    public void visit(ASTBooleanNotExpression node) {
        // TODO

    }

    @Override
    public void visit(ASTLogicalNotExpression node) {
        // TODO

    }

    @Override
    public void visit(ASTMultExpression node) {
//        EvaluationResult leftResult = acceptThisOrLogError
    }

    @Override
    public void visit(ASTDivideExpression node) {

    }

    @Override
    public void visit(ASTModuloExpression node) {

    }

    @Override
    public void visit(ASTPlusExpression node) {

    }

    @Override
    public void visit(ASTMinusExpression node) {

    }

    @Override
    public void visit(ASTLessEqualExpression node) {

    }

    @Override
    public void visit(ASTGreaterEqualExpression node) {

    }

    @Override
    public void visit(ASTLessThanExpression node) {

    }

    @Override
    public void visit(ASTGreaterThanExpression node) {

    }

    @Override
    public void visit(ASTEqualsExpression node) {

    }

    @Override
    public void visit(ASTNotEqualsExpression node) {

    }

    @Override
    public void visit(ASTBooleanAndOpExpression node) {

    }

    @Override
    public void visit(ASTBooleanOrOpExpression node) {

    }

    @Override
    public void visit(ASTConditionalExpression node) {

    }

    @Override
    public void visit(ASTBracketExpression node) {

    }

    @Override
    public void visit(ASTInfixExpression node) {

    }

    @Override
    public void visit(ASTCommonExpressionsNode node) {

    }
}
