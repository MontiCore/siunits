/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.assignmentexpressions._ast.*;
import de.monticore.expressions.bitexpressions._ast.*;
import de.monticore.expressions.commonexpressions._ast.*;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisVisitor;
import de.monticore.expressions.javaclassexpressions._ast.*;
import de.monticore.expressions.setexpressions._ast.ASTIntersectionExpressionInfix;
import de.monticore.expressions.setexpressions._ast.ASTIsInExpression;
import de.monticore.expressions.setexpressions._ast.ASTSetInExpression;
import de.monticore.expressions.setexpressions._ast.ASTUnionExpressionInfix;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;

public abstract class FlatExpressionScopeSetterAbs implements ExpressionsBasisVisitor {
    protected IExpressionsBasisScope scope;

    public FlatExpressionScopeSetterAbs(IExpressionsBasisScope scope) {
        this.scope = scope;
    }

    // ************************* Visitor *************************

    private ExpressionsBasisVisitor realThis = this;

    @Override
    public ExpressionsBasisVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(ExpressionsBasisVisitor realThis) {
        this.realThis = realThis;
    }

    /*************************************************ASSIGNMENT EXPRESSIONS****************************************************/

    public void visit(ASTAssignmentExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTMinusPrefixExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTPlusPrefixExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTDecPrefixExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTDecSuffixExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTIncPrefixExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTIncSuffixExpression expr){
        expr.setEnclosingScope(scope);
    }

    /*************************************************COMMON EXPRESSIONS****************************************************/

    public void visit(ASTGreaterEqualExpression expr) {
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTLessEqualExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTGreaterThanExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTLessThanExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTPlusExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTMinusExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTMultExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTDivideExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTModuloExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTEqualsExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTNotEqualsExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTFieldAccessExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTCallExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTLogicalNotExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBooleanAndOpExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBooleanOrOpExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBooleanNotExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBracketExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTConditionalExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTArguments expr){
        expr.setEnclosingScope(scope);
    }

    /*************************************************BIT EXPRESSIONS****************************************************/

    public void visit(ASTLogicalRightShiftExpression expr) {
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTRightShiftExpression expr) {
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTLeftShiftExpression expr) {
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBinaryOrOpExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBinaryAndExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTBinaryXorExpression expr){
        expr.setEnclosingScope(scope);
    }

    /*************************************************SET EXPRESSIONS****************************************************/

    public void visit(ASTIsInExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTSetInExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTUnionExpressionInfix expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTIntersectionExpressionInfix expr){
        expr.setEnclosingScope(scope);
    }

    /*************************************************EXPRESSIONS BASIS****************************************************/

    public void visit(ASTLiteralExpression expr){
        expr.setEnclosingScope(scope);
        expr.getLiteral().setEnclosingScope(scope);
    }


    public void visit(ASTNameExpression expr){
        expr.setEnclosingScope(scope);
    }

    /*************************************************JAVA CLASS EXPRESSIONS****************************************************/

    public void visit(ASTPrimarySuperExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTPrimaryThisExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTSuperExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTThisExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTArrayExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTInstanceofExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTTypeCastExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTPrimaryGenericInvocationExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTGenericInvocationExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTClassExpression expr){
        expr.setEnclosingScope(scope);
    }


    public void visit(ASTMCQualifiedType type){
        type.setEnclosingScope(scope);
    }


    public void visit(ASTMCQualifiedName name) {
        name.setEnclosingScope(scope);
    }


    public void visit(ASTMCReturnType type){
        type.setEnclosingScope(scope);
    }
}
