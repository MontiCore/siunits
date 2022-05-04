/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._visitor.CombineExpressionsWithSIUnitLiteralsTraverser;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;

import java.util.Optional;

/**
 * Delegator Visitor to test the combination of the grammars
 */
public class DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator extends AbstractDerive {

  public DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(){
    this(CombineExpressionsWithSIUnitLiteralsMill.traverser());
  }

  public DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(CombineExpressionsWithSIUnitLiteralsTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  public void init(CombineExpressionsWithSIUnitLiteralsTraverser traverser){

    DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4CommonExpressions(deriveSymTypeOfCommonExpressions);
    traverser.setCommonExpressionsHandler(deriveSymTypeOfCommonExpressions);

    DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
    deriveSymTypeOfAssignmentExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4AssignmentExpressions(deriveSymTypeOfAssignmentExpressions);
    traverser.setAssignmentExpressionsHandler(deriveSymTypeOfAssignmentExpressions);

    DeriveSymTypeOfBitExpressions deriveSymTypeOfBitExpressions = new DeriveSymTypeOfBitExpressions();
    deriveSymTypeOfBitExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4BitExpressions(deriveSymTypeOfBitExpressions);
    traverser.setBitExpressionsHandler(deriveSymTypeOfBitExpressions);

    DeriveSymTypeOfExpression deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfExpression.setTypeCheckResult(typeCheckResult);
    traverser.add4ExpressionsBasis(deriveSymTypeOfExpression);
    traverser.setExpressionsBasisHandler(deriveSymTypeOfExpression);

    DeriveSymTypeOfJavaClassExpressions deriveSymTypeOfJavaClassExpressions = new DeriveSymTypeOfJavaClassExpressions();
    deriveSymTypeOfJavaClassExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4JavaClassExpressions(deriveSymTypeOfJavaClassExpressions);
    traverser.setJavaClassExpressionsHandler(deriveSymTypeOfJavaClassExpressions);

    DeriveSymTypeOfLiterals deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    deriveSymTypeOfLiterals.setTypeCheckResult(typeCheckResult);
    traverser.add4MCLiteralsBasis(deriveSymTypeOfLiterals);

    DeriveSymTypeOfMCCommonLiterals commonLiteralsTypesCalculator = new DeriveSymTypeOfMCCommonLiterals();
    commonLiteralsTypesCalculator.setTypeCheckResult(typeCheckResult);
    traverser.add4MCCommonLiterals(commonLiteralsTypesCalculator);


    FullSynthesizeFromCombineExpressionsWithLiterals symTypeFromCombineExpressionsWithLiteralsDelegator = new FullSynthesizeFromCombineExpressionsWithLiterals();
    DeriveSymTypeOfCombineExpressions deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(symTypeFromCombineExpressionsWithLiteralsDelegator);
    deriveSymTypeOfCombineExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4CombineExpressionsWithLiterals(deriveSymTypeOfCombineExpressions);
    traverser.setCombineExpressionsWithLiteralsHandler(deriveSymTypeOfCombineExpressions);

    DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals = new DeriveSymTypeOfSIUnitLiterals();
    deriveSymTypeOfSIUnitLiterals.setTypeCheckResult(typeCheckResult);
    traverser.setSIUnitLiteralsHandler(deriveSymTypeOfSIUnitLiterals);
  }
}
