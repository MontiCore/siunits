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
public class DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator implements ITypesCalculator {
  
  private CombineExpressionsWithSIUnitLiteralsTraverser traverser;

  @Override
  public CombineExpressionsWithSIUnitLiteralsTraverser getTraverser() {
    return traverser;
  }

  private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

  private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

  private DeriveSymTypeOfBitExpressions deriveSymTypeOfBitExpressions;

  private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

  private DeriveSymTypeOfJavaClassExpressions deriveSymTypeOfJavaClassExpressions;

  private DeriveSymTypeOfSetExpressions deriveSymTypeOfSetExpressions;

  private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

  private DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals;

  private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

  private DeriveSymTypeOfMCCommonLiterals commonLiteralsTypesCalculator;

  private DeriveSymTypeOfCombineExpressions deriveSymTypeOfCombineExpressions;

  private SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator symTypeFromCombineExpressionsWithLiteralsDelegator;


  private TypeCheckResult typeCheckResult = new TypeCheckResult();


  public DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(){
    this.traverser= CombineExpressionsWithSIUnitLiteralsMill.traverser();

    deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4CommonExpressions(deriveSymTypeOfCommonExpressions);
    traverser.setCommonExpressionsHandler(deriveSymTypeOfCommonExpressions);

    deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
    deriveSymTypeOfAssignmentExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4AssignmentExpressions(deriveSymTypeOfAssignmentExpressions);
    traverser.setAssignmentExpressionsHandler(deriveSymTypeOfAssignmentExpressions);

    deriveSymTypeOfBitExpressions = new DeriveSymTypeOfBitExpressions();
    deriveSymTypeOfBitExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4BitExpressions(deriveSymTypeOfBitExpressions);
    traverser.setBitExpressionsHandler(deriveSymTypeOfBitExpressions);

    deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfExpression.setTypeCheckResult(typeCheckResult);
    traverser.add4ExpressionsBasis(deriveSymTypeOfExpression);
    traverser.setExpressionsBasisHandler(deriveSymTypeOfExpression);

    deriveSymTypeOfJavaClassExpressions = new DeriveSymTypeOfJavaClassExpressions();
    deriveSymTypeOfJavaClassExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4JavaClassExpressions(deriveSymTypeOfJavaClassExpressions);
    traverser.setJavaClassExpressionsHandler(deriveSymTypeOfJavaClassExpressions);

    deriveSymTypeOfSetExpressions = new DeriveSymTypeOfSetExpressions();
    deriveSymTypeOfSetExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4SetExpressions(deriveSymTypeOfSetExpressions);
    traverser.setSetExpressionsHandler(deriveSymTypeOfSetExpressions);

    deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    deriveSymTypeOfLiterals.setTypeCheckResult(typeCheckResult);
    traverser.add4MCLiteralsBasis(deriveSymTypeOfLiterals);

    commonLiteralsTypesCalculator = new DeriveSymTypeOfMCCommonLiterals();
    commonLiteralsTypesCalculator.setTypeCheckResult(typeCheckResult);
    traverser.add4MCCommonLiterals(commonLiteralsTypesCalculator);


    symTypeFromCombineExpressionsWithLiteralsDelegator = new SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator();
    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(symTypeFromCombineExpressionsWithLiteralsDelegator);
    deriveSymTypeOfCombineExpressions.setTypeCheckResult(typeCheckResult);
    traverser.add4CombineExpressionsWithLiterals(deriveSymTypeOfCombineExpressions);
    traverser.setCombineExpressionsWithLiteralsHandler(deriveSymTypeOfCombineExpressions);

    deriveSymTypeOfSIUnitLiterals = new DeriveSymTypeOfSIUnitLiterals();
    deriveSymTypeOfSIUnitLiterals.setTypeCheckResult(typeCheckResult);
    traverser.setSIUnitLiteralsHandler(deriveSymTypeOfSIUnitLiterals);
  }

  /**
   * main method to calculate the type of an expression
   */
  public Optional<SymTypeExpression> calculateType(ASTExpression e){
    e.accept(getTraverser());
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.reset();
    return result;
  }

  /**
   * set the last typeCheckResult of all calculators to the same object
   */
  public void setTypeCheckResult(TypeCheckResult typeCheckResult){
    deriveSymTypeOfAssignmentExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfExpression.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfBitExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfJavaClassExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfSetExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfCombineExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfSIUnitLiterals.setTypeCheckResult(typeCheckResult);
  }

  /**
   * initialize the typescalculator
   */
  @Override
  public void init() {
    deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
    deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
    deriveSymTypeOfMCCommonLiterals = new DeriveSymTypeOfMCCommonLiterals();
    deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    deriveSymTypeOfBitExpressions = new DeriveSymTypeOfBitExpressions();
    deriveSymTypeOfJavaClassExpressions = new DeriveSymTypeOfJavaClassExpressions();
    deriveSymTypeOfSetExpressions = new DeriveSymTypeOfSetExpressions();
    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(symTypeFromCombineExpressionsWithLiteralsDelegator);
    deriveSymTypeOfSIUnitLiterals = new DeriveSymTypeOfSIUnitLiterals();
    setTypeCheckResult(typeCheckResult);
  }

  /**
   * main method to calculate the type of a literal
   */
  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(getTraverser());
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.reset();
    return result;
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(getTraverser());
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.reset();
    return result;
  }
}
