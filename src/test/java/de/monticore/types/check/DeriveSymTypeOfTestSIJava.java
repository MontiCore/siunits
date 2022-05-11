/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;

public class DeriveSymTypeOfTestSIJava extends AbstractDerive {

    private DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes deriveSymTypeOfAssignmentExpressions;

    private DeriveSymTypeOfCommonExpressionsWithSIUnitTypes deriveSymTypeOfCommonExpressions;

    private DeriveSymTypeOfExpression deriveSymTypeOfExpression;

    private DeriveSymTypeOfLiterals deriveSymTypeOfLiterals;

    private DeriveSymTypeOfSIUnitLiterals deriveSymTypeOfSIUnitLiterals;

    private DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals;

    public DeriveSymTypeOfTestSIJava(TestSIJavaTraverser traverser) {
        super(traverser);
        init(traverser);
    }

    public DeriveSymTypeOfTestSIJava(){
        this(TestSIJavaMill.traverser());
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
    public void init(TestSIJavaTraverser traverser) {
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
}
