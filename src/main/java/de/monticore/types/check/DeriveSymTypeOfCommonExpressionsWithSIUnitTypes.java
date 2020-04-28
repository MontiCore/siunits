package de.monticore.types.check;

import de.monticore.expressions.commonexpressions._ast.ASTDivideExpression;
import de.monticore.expressions.commonexpressions._ast.ASTInfixExpression;
import de.monticore.expressions.commonexpressions._ast.ASTMultExpression;

import java.util.Optional;

/**
 * This class is used to derive the symtype of common expressions where SI unit types are used, including primitive
 * with SI unit types.
 */
public class DeriveSymTypeOfCommonExpressionsWithSIUnitTypes extends DeriveSymTypeOfCommonExpressions {

     @Override
    protected Optional<SymTypeExpression> calculateMultExpression(ASTMultExpression expr) {
        SymTypeExpression leftResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getLeft(), "0xA0491");
        SymTypeExpression rightResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getRight(), "0xA0492");
        Optional<SymTypeExpression> wholeResult = calculateMultDivideExpression("*", leftResult, rightResult);
        if (wholeResult.isPresent())
            return wholeResult;
        return super.calculateMultExpression(expr);
    }

    @Override
    protected Optional<SymTypeExpression> calculateDivideExpression(ASTDivideExpression expr) {
        SymTypeExpression leftResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getLeft(), "0xA0493");
        SymTypeExpression rightResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getRight(), "0xA0494");
        Optional<SymTypeExpression> wholeResult = calculateMultDivideExpression("/", leftResult, rightResult);
        if (wholeResult.isPresent())
            return wholeResult;
        return super.calculateDivideExpression(expr);
    }

    protected Optional<SymTypeExpression> calculateMultDivideExpression(String operator, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult)) {
            // if both are SI unit types the result the result has to be calculated depending on the operator
            String newUnitName = leftResult.print() + operator + "(" + rightResult.print() + ")";
            return Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(newUnitName, scope));
        } else if (isSIUnitType(leftResult) && isNumericType(rightResult)) {
            return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(rightResult, leftResult, scope));
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult)) {
            SymTypeExpression siUnitType;
            if ("*".equals(operator))
                siUnitType = rightResult;
            else {
                siUnitType = SIUnitSymTypeExpressionFactory.createSIUnit("(" + rightResult.print() + ")^-1", scope);
            }
            return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(leftResult, siUnitType, scope));
        } else if (isNumericWithSIUnitType(leftResult) || isNumericWithSIUnitType(rightResult)) {
            // The result is again a PrimitiveWithSIUnitType
            Optional<SymTypeExpression> numericType = getBinaryNumericPromotionOfNumeric(leftResult, rightResult);
            if (numericType.isPresent() && isNumericType(numericType.get())) {
                Optional<SymTypeExpression> leftSIUnitType = getSIUnit(leftResult);
                Optional<SymTypeExpression> rightSIUnitType = getSIUnit(rightResult);
                Optional<SymTypeExpression> siUnitType;
                if (!leftSIUnitType.isPresent()) {
                    siUnitType = rightSIUnitType;
                } else if (!rightSIUnitType.isPresent()) {
                    siUnitType = leftSIUnitType;
                } else {
                    siUnitType = calculateMultDivideExpression(operator, leftSIUnitType.get(), rightSIUnitType.get());
                }
                return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                        numericType.get(), siUnitType.get(), scope));
            }
        }
        return Optional.empty();
    }

    /**
     * helper method for <=, >=, <, > -> calculates the result of these expressions
     */
    protected Optional<SymTypeExpression> calculateTypeCompare(ASTInfixExpression expr, SymTypeExpression rightResult, SymTypeExpression leftResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            SymTypeExpression leftSIUnitType = getSIUnit(leftResult).get();
            SymTypeExpression rightSIUnitType = getSIUnit(rightResult).get();
            if (TypeCheck.compatible(leftSIUnitType, rightSIUnitType)) {
                return Optional.of(SymTypeExpressionFactory.createTypeConstant("boolean"));
            }
            return Optional.empty();
        }
        return super.calculateTypeCompare(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the ASTEqualsExpression and the ASTNotEqualsExpression
     */
    protected Optional<SymTypeExpression> calculateTypeLogical(ASTInfixExpression expr, SymTypeExpression rightResult, SymTypeExpression leftResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult)) {
            return Optional.of(SymTypeExpressionFactory.createTypeConstant("boolean"));
        } else if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            return Optional.of(SymTypeExpressionFactory.createTypeConstant("boolean"));
        }
        return super.calculateTypeLogical(expr, leftResult, rightResult);
    }

    /**
     * return the result for the basic arithmetic operations +,-,%
     */
    @Override
    protected Optional<SymTypeExpression> getBinaryNumericPromotion(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        // Case for +,-,% and as result of a Conditional Expression
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            Optional<SymTypeExpression> leftSIUnitType = getSIUnit(leftResult);
            Optional<SymTypeExpression> rightSIUnitType = getSIUnit(rightResult);
            if (leftSIUnitType.isPresent() && rightSIUnitType.isPresent()
                    && TypeCheck.compatible(leftSIUnitType.get(), rightSIUnitType.get())) {
                Optional<SymTypeExpression> numericType = getBinaryNumericPromotionOfNumeric(leftResult, rightResult);
                if (numericType.isPresent() && isNumericType(numericType.get())) {
                    return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                            numericType.get(), leftSIUnitType.get(), scope));
                }
            }
            // Should not happen, will be handled in traverse
            return Optional.empty();
        }
        return super.getBinaryNumericPromotion(leftResult, rightResult);
    }

    private Optional<SymTypeExpression> getBinaryNumericPromotionOfNumeric(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        Optional<SymTypeExpression> leftNumericType = getNumeric(leftResult);
        Optional<SymTypeExpression> rightNumericType = getNumeric(rightResult);
        if (isSIUnitType(leftResult))
            return rightNumericType;
        if (isSIUnitType(rightResult))
            return leftNumericType;
        if (!leftNumericType.isPresent() || !rightNumericType.isPresent())
            return Optional.empty();
        return super.getBinaryNumericPromotion(leftNumericType.get(), rightNumericType.get());
    }

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected Optional<SymTypeExpression> getUnaryIntegralPromotionType(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            Optional<SymTypeExpression> numericType = getNumeric(type);
            Optional<SymTypeExpression> siUnitType = getSIUnit(type);
            numericType = super.getUnaryIntegralPromotionType(numericType.get());
            if (numericType.isPresent() && isNumericType(numericType.get())
                    && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createNumericWithSIUnitType(numericType.get(), siUnitType.get(), scope));
            }
            return Optional.empty();
        }
        return super.getUnaryIntegralPromotionType(type);
    }

    public boolean isSIUnitType(SymTypeExpression type) {
        return type instanceof SymTypeOfSIUnit;
    }

    private boolean isNumericWithSIUnitType(SymTypeExpression type) {
        return type instanceof SymTypeOfNumericWithSIUnit;
    }

    /**
     * Helper method to extract numeric type of an SymTypeExpression
     */
    private Optional<SymTypeExpression> getNumeric(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return Optional.ofNullable(((SymTypeOfNumericWithSIUnit) type).getNumericType());
        } else if (isNumericType(type)) {
            return Optional.ofNullable(type);
        }
        return Optional.empty();
    }

    /**
     * Helper method to extract siunit type of an SymTypeExpression
     */
    private Optional<SymTypeExpression> getSIUnit(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return Optional.ofNullable(((SymTypeOfNumericWithSIUnit) type).getSIUnit());
        } else if(isSIUnitType(type)) {
            return Optional.of(type);
        }
        return Optional.empty();
    }
}
