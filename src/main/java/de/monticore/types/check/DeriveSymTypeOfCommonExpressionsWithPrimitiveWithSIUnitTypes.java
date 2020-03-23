package de.monticore.types.check;

import java.util.Optional;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.isNumericType;
import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.isSIUnitType;

public class DeriveSymTypeOfCommonExpressionsWithPrimitiveWithSIUnitTypes extends DeriveSymTypeOfCommonExpressionsWithSIUnitTypes {
    /**
     * helper method for <=, >=, <, > -> calculates the result of these expressions
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeCompare(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult) && leftResult.print().equals(rightResult.print())) {
            return Optional.of(SymTypeExpressionFactory.createTypeConstant("boolean"));
        }
        return super.calculateTypeCompare(leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the ASTEqualsExpression and the ASTNotEqualsExpression
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeLogical(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult) && leftResult.print().equals(rightResult.print())) {
            return Optional.of(SymTypeExpressionFactory.createTypeConstant("boolean"));
        }
        return super.calculateTypeLogical(leftResult, rightResult);
    }

    /**
     * return the result for the five basic arithmetic operations (+,-,*,/,%)
     */
    @Override
    protected Optional<SymTypeExpression> getBinaryNumericPromotion(String operation, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult) && ("*".equals(operation) || "/".equals(operation))) {
            // if both are SI unit types the result the result has to be calculated depending on the operation
            return Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(leftResult.print() + operation + rightResult.print(), scope));
        } else if (isSIUnitType(leftResult) && isSIUnitType(rightResult) && leftResult.print().equals(rightResult.print())) {
            // Case for +,-,% and as result of a Conditional Expression
            return Optional.of(leftResult);
        } else if (isSIUnitType(leftResult) && isNumericType(rightResult) && ("*".equals(operation) || "/".equals(operation))) {
            // For expressions like '4 km * 5', notice modulo is not defined for '4 km % 2'
            return Optional.of(leftResult);
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult) && ("*".equals(operation) || "/".equals(operation))) {
            // For expressions like '4 km * 5', notice modulo is not defined for '4 % 2 km'
            return Optional.of(rightResult);
        }
        return super.getBinaryNumericPromotion(operation, leftResult, rightResult);
    }
}
