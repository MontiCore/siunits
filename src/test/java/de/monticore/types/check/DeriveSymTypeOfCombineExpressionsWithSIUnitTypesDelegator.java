/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsDelegatorVisitor;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;

import java.util.Optional;

/**
 * Delegator Visitor to test the combination of the grammars
 */
public class DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator extends CombineExpressionsWithLiteralsDelegatorVisitor implements ITypesCalculator {

  private CombineExpressionsWithLiteralsDelegatorVisitor realThis;

  private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

  private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

  private DeriveSymTypeOfBitExpressions deriveSymTypeOfBitExpressions;

  private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

  private DeriveSymTypeOfJavaClassExpressions deriveSymTypeOfJavaClassExpressions;

  private DeriveSymTypeOfSetExpressions deriveSymTypeOfSetExpressions;

  private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

  private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

  private DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals;

  private DeriveSymTypeOfCombineExpressions deriveSymTypeOfCombineExpressions;

  private SynthesizeSymTypeOfCombinedTypes synthesize;

  private TypeCheckResult typeCheckResult = new TypeCheckResult();

  IExpressionsBasisScope scope;

  public DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(){
    this.realThis=this;
    init();
  }

  /**
   * main method to calculate the type of an expression
   */
  public Optional<SymTypeExpression> calculateType(ASTExpression e){
    e.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.reset();
    return result;
  }

  @Override
  public CombineExpressionsWithLiteralsDelegatorVisitor getRealThis(){
    return realThis;
  }

  /**
   * set the last result of all calculators to the same object
   */
  public void setTypeCheckResult(TypeCheckResult typeCheckResult){
    deriveSymTypeOfAssignmentExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfExpression.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfSIUnitLiterals.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfBitExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfJavaClassExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfSetExpressions.setTypeCheckResult(typeCheckResult);
    deriveSymTypeOfCombineExpressions.setTypeCheckResult(typeCheckResult);
  }

  /**
   * initialize the typescalculator
   */
  @Override
  public void init() {
    deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
    setCommonExpressionsVisitor(deriveSymTypeOfCommonExpressions);
    deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
    setAssignmentExpressionsVisitor(deriveSymTypeOfAssignmentExpressions);
    deriveSymTypeOfBitExpressions = new DeriveSymTypeOfBitExpressions();
    setBitExpressionsVisitor(deriveSymTypeOfBitExpressions);
    deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    setExpressionsBasisVisitor(deriveSymTypeOfExpression);
    deriveSymTypeOfJavaClassExpressions = new DeriveSymTypeOfJavaClassExpressions();
    setJavaClassExpressionsVisitor(deriveSymTypeOfJavaClassExpressions);
    deriveSymTypeOfSetExpressions = new DeriveSymTypeOfSetExpressions();
    setSetExpressionsVisitor(deriveSymTypeOfSetExpressions);
    deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    setMCLiteralsBasisVisitor(deriveSymTypeOfLiterals);
    deriveSymTypeOfMCCommonLiterals = new DeriveSymTypeOfMCCommonLiterals();
    setMCCommonLiteralsVisitor(deriveSymTypeOfMCCommonLiterals);
    deriveSymTypeOfSIUnitLiterals = new DeriveSymTypeOfSIUnitLiterals();
    setSIUnitLiteralsVisitor(deriveSymTypeOfSIUnitLiterals);

    synthesize = new SynthesizeSymTypeOfCombinedTypes();
    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(synthesize);
    setCombineExpressionsWithLiteralsVisitor(deriveSymTypeOfCombineExpressions);

    setTypeCheckResult(typeCheckResult);
  }

  /**
   * main method to calculate the type of a literal
   */
  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.reset();
    return result;
  }

  /**
   * main method to calculate the type of a signed literal
   */
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (typeCheckResult.isPresentCurrentResult()) {
      result = Optional.ofNullable(typeCheckResult.getCurrentResult());
    }
    typeCheckResult.setCurrentResultAbsent();
    return result;
  }
}
