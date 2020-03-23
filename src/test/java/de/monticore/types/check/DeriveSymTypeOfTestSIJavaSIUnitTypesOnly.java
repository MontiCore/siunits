package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.testsijava.testsijava._visitor.TestSIJavaBasicVisitor;
import de.monticore.testsijava.testsijavasiunittypesonly._visitor.TestSIJavaSIUnitTypesOnlyDelegatorVisitor;

import java.util.Optional;

public class DeriveSymTypeOfTestSIJavaSIUnitTypesOnly extends TestSIJavaSIUnitTypesOnlyDelegatorVisitor
        implements ITypesCalculator, ISynthesize {

    private TestSIJavaSIUnitTypesOnlyDelegatorVisitor realThis;

    private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

    private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

    private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

    private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

    private DeriveSymTypeOfSIUnitLiteralsSIUnitOnly deriveSymTypeOfSIUnitLiteralsSIUnitOnly;

    private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

    private DeriveSymTypeOfMCCommonLiterals commonLiteralsTypesCalculator;

    private SynthesizeSymTypeFromSIUnitTypes synthesizeSymTypeFromSIUnitTypes;

    private LastResult lastResult = new LastResult();

    private IExpressionsBasisScope scope;


    public DeriveSymTypeOfTestSIJavaSIUnitTypesOnly(IExpressionsBasisScope scope) {
        this.realThis = this;
        this.scope = scope;
        init();
    }

    /**
     * main method to calculate the type of an expression
     */
    public Optional<SymTypeExpression> calculateType(ASTExpression e) {
        e.accept(realThis);
        Optional<SymTypeExpression> result = Optional.empty();
        if (lastResult.isPresentLast()) {
            result = Optional.ofNullable(lastResult.getLast());
        }
        lastResult.setLastAbsent();
        return result;
    }

    @Override
    public TestSIJavaSIUnitTypesOnlyDelegatorVisitor getRealThis() {
        return realThis;
    }

    /**
     * set the last result of all calculators to the same object
     */
    public void setLastResult(LastResult lastResult) {
        deriveSymTypeOfAssignmentExpressions.setLastResult(lastResult);
        deriveSymTypeOfMCCommonLiterals.setResult(lastResult);
        deriveSymTypeOfCommonExpressions.setLastResult(lastResult);
        deriveSymTypeOfExpression.setLastResult(lastResult);
        deriveSymTypeOfLiterals.setResult(lastResult);
        deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setResult(lastResult);
    }

    /**
     * set the scope of the typescalculator, important for resolving for e.g. NameExpression
     */
    public void setScope(IExpressionsBasisScope scope) {
        this.scope = scope;
        deriveSymTypeOfAssignmentExpressions.setScope(scope);
        deriveSymTypeOfExpression.setScope(scope);
        deriveSymTypeOfCommonExpressions.setScope(scope);
        deriveSymTypeOfSIUnitLiteralsSIUnitOnly.setScope(scope);
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
        deriveSymTypeOfSIUnitLiteralsSIUnitOnly = new DeriveSymTypeOfSIUnitLiteralsSIUnitOnly();
        synthesizeSymTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes();

        setCommonExpressionsVisitor(deriveSymTypeOfCommonExpressions);
        setAssignmentExpressionsVisitor(deriveSymTypeOfAssignmentExpressions);
        setExpressionsBasisVisitor(deriveSymTypeOfExpression);
        setMCLiteralsBasisVisitor(deriveSymTypeOfLiterals);
        setMCCommonLiteralsVisitor(deriveSymTypeOfMCCommonLiterals);
        setSIUnitLiteralsVisitor(deriveSymTypeOfSIUnitLiteralsSIUnitOnly);
        setSIUnitTypesVisitor(synthesizeSymTypeFromSIUnitTypes);
        setTestSIJavaVisitor(new TestSIJavaBasicVisitor());

        setScope(scope);
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
        lastResult.setLastAbsent();
        return result;
    }

    /**
     * main method to calculate the type of a signed literal
     */
    @Override
    public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
        lit.accept(realThis);
        Optional<SymTypeExpression> result = Optional.empty();
        if (lastResult.isPresentLast()) {
            result = Optional.ofNullable(lastResult.getLast());
        }
        lastResult.setLastAbsent();
        return result;
    }

    // ISynthesize methods
    @Override
    public Optional<SymTypeExpression> getResult() {
        if (lastResult.isPresentLast())
            return Optional.of(lastResult.getLast());
        else
            return Optional.empty();
    }
}
