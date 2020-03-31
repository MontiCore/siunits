package de.monticore.types.check;

import de.monticore.expressions.commonexpressions._ast.ASTDivideExpression;
import de.monticore.expressions.commonexpressions._ast.ASTInfixExpression;
import de.monticore.expressions.commonexpressions._ast.ASTMultExpression;

import java.util.Optional;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.*;

public class DeriveSymTypeOfCommonExpressionsWithSIUnitTypes extends DeriveSymTypeOfCommonExpressions {

    @Override
    protected Optional<SymTypeExpression> calculateMultExpression(ASTMultExpression expr) {
        SymTypeExpression leftResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getLeft(), "0x");
        SymTypeExpression rightResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getRight(), "0x");
        Optional<SymTypeExpression> wholeResult = calculateMultDivideExpression("*", leftResult, rightResult);
        if (wholeResult.isPresent())
            return wholeResult;
        return super.calculateMultExpression(expr);
    }

    @Override
    protected Optional<SymTypeExpression> calculateDivideExpression(ASTDivideExpression expr) {
        SymTypeExpression leftResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getLeft(), "0x");
        SymTypeExpression rightResult = acceptThisAndReturnSymTypeExpressionOrLogError(expr.getRight(), "0x");
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
            return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType((SymTypeConstant) rightResult, leftResult, scope));
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult)) {
            SymTypeExpression siUnitType;
            if ("*".equals(operator))
                siUnitType = rightResult;
            else {
                siUnitType = SIUnitSymTypeExpressionFactory.createSIUnit("(" + rightResult.print() + ")^-1");
            }
            return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType((SymTypeConstant) leftResult, siUnitType, scope));
        } else if (isPrimitiveWithSIUnitType(leftResult) || isPrimitiveWithSIUnitType(rightResult)) {
            // The result is again a PrimitiveWithSIUnitType
            Optional<SymTypeExpression> primitiveType = getBinaryNumericPromotionOfPrimitive(leftResult, rightResult);
            if (primitiveType.isPresent() && isSymTypeConstant(primitiveType.get())) {
                Optional<SymTypeExpression> leftSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
                Optional<SymTypeExpression> rightSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(rightResult);
                Optional<SymTypeExpression> siUnitType;
                if (!leftSIUnitType.isPresent()) {
                    siUnitType = rightSIUnitType;
                } else if (!rightSIUnitType.isPresent()) {
                    siUnitType = leftSIUnitType;
                } else {
                    siUnitType = calculateMultDivideExpression(operator, leftSIUnitType.get(), rightSIUnitType.get());
                }
                return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(
                        (SymTypeConstant) primitiveType.get(), siUnitType.get(), scope));
            }
        }
        return Optional.empty();
    }

    /**
     * helper method for <=, >=, <, > -> calculates the result of these expressions
     */
    protected Optional<SymTypeExpression> calculateTypeCompare(ASTInfixExpression expr, SymTypeExpression rightResult, SymTypeExpression leftResult) {
        if (isPrimitiveWithSIUnitType(leftResult) && isPrimitiveWithSIUnitType(rightResult)) {
            SymTypeExpression leftSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(leftResult).get();
            SymTypeExpression rightSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(rightResult).get();
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
        } else if (isPrimitiveWithSIUnitType(leftResult) && isPrimitiveWithSIUnitType(rightResult)) {
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
        if (isPrimitiveWithSIUnitType(leftResult) && isPrimitiveWithSIUnitType(rightResult)) {
            Optional<SymTypeExpression> leftSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightSIUnitType = getSIUnitOfPrimitiveWithSIUnitType(rightResult);
            if (leftSIUnitType.isPresent() && rightSIUnitType.isPresent()
                    && TypeCheck.compatible(leftSIUnitType.get(), rightSIUnitType.get())) {
                Optional<SymTypeExpression> primitiveType = getBinaryNumericPromotionOfPrimitive(leftResult, rightResult);
                if (primitiveType.isPresent() && isSymTypeConstant(primitiveType.get())) {
                    return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(
                            (SymTypeConstant) primitiveType.get(), leftSIUnitType.get(), scope));
                }
            }
            // Should not happen, will be handled in traverse
            return Optional.empty();
        }
        return super.getBinaryNumericPromotion(leftResult, rightResult);
    }

    private Optional<SymTypeExpression> getBinaryNumericPromotionOfPrimitive(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        Optional<SymTypeExpression> leftPrimitiveType = getPrimitiveOfPrimitiveWithSIUnitType(leftResult);
        Optional<SymTypeExpression> rightPrimitiveType = getPrimitiveOfPrimitiveWithSIUnitType(rightResult);
        if (!leftPrimitiveType.isPresent())
            return rightPrimitiveType;
        if (!rightPrimitiveType.isPresent())
            return leftPrimitiveType;
        return super.getBinaryNumericPromotion(leftPrimitiveType.get(), rightPrimitiveType.get());
    }

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected Optional<SymTypeExpression> getUnaryIntegralPromotionType(SymTypeExpression type) {
        if (isPrimitiveWithSIUnitType(type)) {
            Optional<SymTypeExpression> primitiveType = getPrimitiveOfPrimitiveWithSIUnitType(type);
            Optional<SymTypeExpression> siUnitType = getSIUnitOfPrimitiveWithSIUnitType(type);
            primitiveType = super.getUnaryIntegralPromotionType(primitiveType.get());
            if (primitiveType.isPresent() && isSymTypeConstant(primitiveType.get())
                    && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createPrimitiveWithSIUnitType((SymTypeConstant) primitiveType.get(), siUnitType.get(), scope));
            }
            return Optional.empty();
        }
        return super.getUnaryIntegralPromotionType(type);
    }
}
