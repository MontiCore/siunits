/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesTraverser;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;

import java.util.Optional;

public class DeriveSymTypeOfTestSIJavaWithCustomSIUnitTypes4Computing implements ITypesCalculator {

    private TestSIJavaWithCustomTypesTraverser traverser;

    @Override
    public TestSIJavaWithCustomTypesTraverser getTraverser() {
        return traverser;
    }
    private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

    private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

    private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

    private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

    private DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals;

    private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

    private TypeCheckResult typeCheckResult = new TypeCheckResult();


    public DeriveSymTypeOfTestSIJavaWithCustomSIUnitTypes4Computing() {
        init();
    }

    /**
     * main method to calculate the type of an expression
     */
    public Optional<SymTypeExpression> calculateType(ASTExpression e) {
        e.accept(getTraverser());
        Optional<SymTypeExpression> result = Optional.empty();
        if (typeCheckResult.isPresentCurrentResult()) {
            result = Optional.ofNullable(typeCheckResult.getCurrentResult());
        }
        typeCheckResult.setCurrentResultAbsent();
        return result;
    }

    /**
     * set the last typeCheckResult of all calculators to the same object
     */
    public void setTypeCheckResult(TypeCheckResult typeCheckResult) {
        deriveSymTypeOfAssignmentExpressions.setTypeCheckResult(typeCheckResult);
        deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(typeCheckResult);
        deriveSymTypeOfCommonExpressions.setTypeCheckResult(typeCheckResult);
        deriveSymTypeOfExpression.setTypeCheckResult(typeCheckResult);
        deriveSymTypeOfLiterals.setTypeCheckResult(typeCheckResult);
        deriveSymTypeOfSIUnitLiterals.setTypeCheckResult(typeCheckResult);
    }

    /**
     * initialize the typescalculator
     */
    @Override
    public void init() {
        traverser = TestSIJavaWithCustomTypesMill.traverser();

        deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressionsWithSIUnitTypes();
        traverser.add4CommonExpressions(deriveSymTypeOfCommonExpressions);
        traverser.setCommonExpressionsHandler(deriveSymTypeOfCommonExpressions);

        deriveSymTypeOfAssignmentExpressions = new DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes();
        traverser.add4AssignmentExpressions(deriveSymTypeOfAssignmentExpressions);
        traverser.setAssignmentExpressionsHandler(deriveSymTypeOfAssignmentExpressions);

        deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
        traverser.add4ExpressionsBasis(deriveSymTypeOfExpression);
        traverser.setExpressionsBasisHandler(deriveSymTypeOfExpression);

        deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
        traverser.add4MCLiteralsBasis(deriveSymTypeOfLiterals);

        deriveSymTypeOfMCCommonLiterals = new DeriveSymTypeOfMCCommonLiterals();
        traverser.add4MCCommonLiterals(deriveSymTypeOfMCCommonLiterals);

        deriveSymTypeOfSIUnitLiterals = new DeriveSymTypeOfSIUnitLiterals();
        traverser.setSIUnitLiteralsHandler(deriveSymTypeOfSIUnitLiterals);

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
        typeCheckResult.setCurrentResultAbsent();
        return result;
    }

    /**
     * main method to calculate the type of a signed literal
     */
    @Override
    public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
        lit.accept(getTraverser());
        Optional<SymTypeExpression> result = Optional.empty();
        if (typeCheckResult.isPresentCurrentResult()) {
            result = Optional.ofNullable(typeCheckResult.getCurrentResult());
        }
        typeCheckResult.setCurrentResultAbsent();
        return result;
    }
}
