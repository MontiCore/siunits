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

  private DeriveSymTypeOfCombineExpressions deriveSymTypeOfCombineExpressions;

  private DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals;

  private SynthesizeSymTypeOfCombinedTypes synthesize;

  private IDerivePrettyPrinter prettyPrinter;

  private LastResult lastResult = new LastResult();

  IExpressionsBasisScope scope;

  public DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(IExpressionsBasisScope scope, IDerivePrettyPrinter prettyPrinter){
    this.realThis=this;
    this.prettyPrinter = prettyPrinter;
    this.scope = scope;

    init();
  }

  /**
   * main method to calculate the type of an expression
   */
  public Optional<SymTypeExpression> calculateType(ASTExpression e){
    e.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (lastResult.isPresentLast()) {
      result = Optional.ofNullable(lastResult.getLast());
    }
    lastResult.reset();
    return result;
  }

  @Override
  public CombineExpressionsWithLiteralsDelegatorVisitor getRealThis(){
    return realThis;
  }

  /**
   * set the last result of all calculators to the same object
   */
  public void setLastResult(LastResult lastResult){
    deriveSymTypeOfAssignmentExpressions.setLastResult(lastResult);
    deriveSymTypeOfMCCommonLiterals.setResult(lastResult);
    deriveSymTypeOfCommonExpressions.setLastResult(lastResult);
    deriveSymTypeOfExpression.setLastResult(lastResult);
    deriveSymTypeOfLiterals.setResult(lastResult);
    deriveSymTypeOfMCCommonLiterals.setResult(lastResult);
    deriveSymTypeOfSIUnitLiterals.setResult(lastResult);
    deriveSymTypeOfBitExpressions.setLastResult(lastResult);
    deriveSymTypeOfJavaClassExpressions.setLastResult(lastResult);
    deriveSymTypeOfSetExpressions.setLastResult(lastResult);
    deriveSymTypeOfCombineExpressions.setLastResult(lastResult);
  }

  /**
   * set the scope of the typescalculator, important for resolving for e.g. NameExpression
   */
  public void setScope(IExpressionsBasisScope scope){
    this.scope = scope;
    deriveSymTypeOfAssignmentExpressions.setScope(scope);
    deriveSymTypeOfExpression.setScope(scope);
    deriveSymTypeOfCommonExpressions.setScope(scope);
    deriveSymTypeOfBitExpressions.setScope(scope);
    deriveSymTypeOfJavaClassExpressions.setScope(scope);
    deriveSymTypeOfSetExpressions.setScope(scope);
    deriveSymTypeOfSIUnitLiterals.setScope(scope);
    synthesize.setScope(scope);
  }

  /**
   * set the scope of the typescalculator, important for resolving for e.g. NameExpression
   */
  public void setPrettyPrinter(IDerivePrettyPrinter prettyPrinter){
    deriveSymTypeOfCommonExpressions.setPrettyPrinter(prettyPrinter);
    deriveSymTypeOfAssignmentExpressions.setPrettyPrinter(prettyPrinter);
    deriveSymTypeOfBitExpressions.setPrettyPrinter(prettyPrinter);
    deriveSymTypeOfExpression.setPrettyPrinter(prettyPrinter);
    deriveSymTypeOfJavaClassExpressions.setPrettyPrinter(prettyPrinter);
    deriveSymTypeOfSetExpressions.setPrettyPrinter(prettyPrinter);
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

    synthesize = new SynthesizeSymTypeOfCombinedTypes(scope);
    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(synthesize);
    setCombineExpressionsWithLiteralsVisitor(deriveSymTypeOfCombineExpressions);

    setPrettyPrinter(prettyPrinter);
    setLastResult(lastResult);
    setScope(scope);
  }

  /**
   * main method to calculate the type of a literal
   */
  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (lastResult.isPresentLast()) {
      result = Optional.ofNullable(lastResult.getLast());
    }
    lastResult.reset();
    return result;
  }

  /**
   * main method to calculate the type of a signed literal
   */
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(realThis);
    Optional<SymTypeExpression> result = Optional.empty();
    if (lastResult.isPresentLast()) {
      result = Optional.ofNullable(lastResult.getLast());
    }
    lastResult.setLastAbsent();
    return result;
  }
}
