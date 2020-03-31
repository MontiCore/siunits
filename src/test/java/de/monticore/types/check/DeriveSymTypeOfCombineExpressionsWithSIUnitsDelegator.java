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
public class DeriveSymTypeOfCombineExpressionsWithSIUnitsDelegator extends CombineExpressionsWithLiteralsDelegatorVisitor implements ITypesCalculator {

  private CombineExpressionsWithLiteralsDelegatorVisitor realThis;

  private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

  private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

  private DeriveSymTypeOfBitExpressions deriveSymTypeOfBitExpressions;

  private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

  private DeriveSymTypeOfJavaClassExpressions deriveSymTypeOfJavaClassExpressions;

  private DeriveSymTypeOfSetExpressions deriveSymTypeOfSetExpressions;

  private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

  private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

  private DeriveSymTypeOfMCCommonLiterals commonLiteralsTypesCalculator;

  private DeriveSymTypeOfCombineExpressions deriveSymTypeOfCombineExpressions;

  private DeriveSymTypeOfSIUnitLiteralsSIUnitOnly deriveSymTypeOfSIUnitLiteralsSIUnitOnly;

  private SynthesizeSymTypeFromMCSimpleGenericOrSIUnitTypes synthesizer;

  private IDerivePrettyPrinter prettyPrinter;

  private LastResult lastResult = new LastResult();

  public DeriveSymTypeOfCombineExpressionsWithSIUnitsDelegator(IExpressionsBasisScope scope, IDerivePrettyPrinter prettyPrinter){
    this.realThis=this;
    this.prettyPrinter = prettyPrinter;

    deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
    deriveSymTypeOfCommonExpressions.setScope(scope);
    deriveSymTypeOfCommonExpressions.setLastResult(lastResult);
    deriveSymTypeOfCommonExpressions.setPrettyPrinter(prettyPrinter);
    setCommonExpressionsVisitor(deriveSymTypeOfCommonExpressions);

    deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
    deriveSymTypeOfAssignmentExpressions.setScope(scope);
    deriveSymTypeOfAssignmentExpressions.setLastResult(lastResult);
    deriveSymTypeOfAssignmentExpressions.setPrettyPrinter(prettyPrinter);
    setAssignmentExpressionsVisitor(deriveSymTypeOfAssignmentExpressions);

    deriveSymTypeOfBitExpressions = new DeriveSymTypeOfBitExpressions();
    deriveSymTypeOfBitExpressions.setScope(scope);
    deriveSymTypeOfBitExpressions.setLastResult(lastResult);
    deriveSymTypeOfBitExpressions.setPrettyPrinter(prettyPrinter);
    setBitExpressionsVisitor(deriveSymTypeOfBitExpressions);

    deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfExpression.setScope(scope);
    deriveSymTypeOfExpression.setLastResult(lastResult);
    deriveSymTypeOfExpression.setPrettyPrinter(prettyPrinter);
    setExpressionsBasisVisitor(deriveSymTypeOfExpression);

    deriveSymTypeOfJavaClassExpressions = new DeriveSymTypeOfJavaClassExpressions();
    deriveSymTypeOfJavaClassExpressions.setScope(scope);
    deriveSymTypeOfJavaClassExpressions.setLastResult(lastResult);
    deriveSymTypeOfJavaClassExpressions.setPrettyPrinter(prettyPrinter);
    setJavaClassExpressionsVisitor(deriveSymTypeOfJavaClassExpressions);

    deriveSymTypeOfSetExpressions = new DeriveSymTypeOfSetExpressions();
    deriveSymTypeOfSetExpressions.setScope(scope);
    deriveSymTypeOfSetExpressions.setLastResult(lastResult);
    deriveSymTypeOfSetExpressions.setPrettyPrinter(prettyPrinter);
    setSetExpressionsVisitor(deriveSymTypeOfSetExpressions);

    deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    setMCLiteralsBasisVisitor(deriveSymTypeOfLiterals);
    deriveSymTypeOfLiterals.setResult(lastResult);

    deriveSymTypeOfSIUnitLiteralsSIUnitOnly = new DeriveSymTypeOfSIUnitLiteralsSIUnitOnly();
    setSIUnitLiteralsVisitor(deriveSymTypeOfSIUnitLiteralsSIUnitOnly);
    deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setResult(lastResult);
    deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setScope(scope);

    commonLiteralsTypesCalculator = new DeriveSymTypeOfMCCommonLiterals();
    setMCCommonLiteralsVisitor(commonLiteralsTypesCalculator);
    commonLiteralsTypesCalculator.setResult(lastResult);

    synthesizer = new SynthesizeSymTypeFromMCSimpleGenericOrSIUnitTypes(scope);

    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(synthesizer);
    deriveSymTypeOfCombineExpressions.setLastResult(lastResult);
    setCombineExpressionsWithLiteralsVisitor(deriveSymTypeOfCombineExpressions);

    setScope(scope);
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
    deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setResult(lastResult);
    deriveSymTypeOfBitExpressions.setLastResult(lastResult);
    deriveSymTypeOfJavaClassExpressions.setLastResult(lastResult);
    deriveSymTypeOfSetExpressions.setLastResult(lastResult);
    deriveSymTypeOfCombineExpressions.setLastResult(lastResult);
  }

  /**
   * set the scope of the typescalculator, important for resolving for e.g. NameExpression
   */
  public void setScope(IExpressionsBasisScope scope){
    deriveSymTypeOfAssignmentExpressions.setScope(scope);
    deriveSymTypeOfExpression.setScope(scope);
    deriveSymTypeOfCommonExpressions.setScope(scope);
    deriveSymTypeOfBitExpressions.setScope(scope);
    deriveSymTypeOfJavaClassExpressions.setScope(scope);
    deriveSymTypeOfSetExpressions.setScope(scope);
    deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setScope(scope);
    synthesizer.setScope(scope);
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
    deriveSymTypeOfCombineExpressions = new DeriveSymTypeOfCombineExpressions(synthesizer);
    deriveSymTypeOfSIUnitLiteralsSIUnitOnly = new DeriveSymTypeOfSIUnitLiteralsSIUnitOnly();
    setLastResult(lastResult);
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