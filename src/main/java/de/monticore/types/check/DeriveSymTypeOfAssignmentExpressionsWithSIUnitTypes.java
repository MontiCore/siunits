package de.monticore.types.check;

import de.monticore.expressions.assignmentexpressions._ast.ASTAssignmentExpression;
import de.monticore.expressions.assignmentexpressions._ast.ASTConstantsAssignmentExpressions;

import java.util.Optional;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.*;

public class DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes extends DeriveSymTypeOfAssignmentExpressions {


    /**
     * helper method for the basic arithmetic assignment operations +=,-=,%=
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeArithmetic(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isPrimitiveWithSIUnitType(leftResult) && isPrimitiveWithSIUnitType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.PLUSEQUALS ||
                expr.getOperator() == ASTConstantsAssignmentExpressions.MINUSEQUALS ||
                expr.getOperator() == ASTConstantsAssignmentExpressions.PERCENTEQUALS)) {
            Optional<SymTypeExpression> leftPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> leftSIUnit = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightSIUnit = getSIUnitOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> primitveType = calculateTypeArithmetic(expr, leftPrimitive.get(), rightPrimitive.get());
            if (primitveType.isPresent() && isNumericType(primitveType.get())
                    && TypeCheck.compatible(leftSIUnit.get(), rightSIUnit.get())) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createPrimitiveWithSIUnitType((SymTypeConstant) primitveType.get(), leftSIUnit.get(), scope));
            }
        } else if (isPrimitiveWithSIUnitType(leftResult) && isNumericType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.STAREQUALS ||
                expr.getOperator() == ASTConstantsAssignmentExpressions.SLASHEQUALS)) {
            Optional<SymTypeExpression> leftPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> primitveType = calculateTypeArithmetic(expr, leftPrimitive.get(), rightResult);
            Optional<SymTypeExpression> siUnitType = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
            if (primitveType.isPresent() && isNumericType(primitveType.get()) && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType((SymTypeConstant) primitveType.get(), siUnitType.get(), scope));
            }
        }
        return super.calculateTypeArithmetic(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of a regular assignment (=)
     */
    @Override
    protected Optional<SymTypeExpression> calculateRegularAssignment(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        //option three: both are primitiveWithSIUnit types and are assignable
        if (isPrimitiveWithSIUnitType(leftResult) && isPrimitiveWithSIUnitType(rightResult)) {
            Optional<SymTypeExpression> leftPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> leftSIUnit = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightSIUnit = getSIUnitOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> primitveType = super.calculateRegularAssignment(expr, leftPrimitive.get(), rightPrimitive.get());
            Optional<SymTypeExpression> siUnitType = super.calculateRegularAssignment(expr, leftSIUnit.get(), rightSIUnit.get());
            if (primitveType.isPresent() && isSymTypeConstant(primitveType.get()) && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(
                        (SymTypeConstant) primitveType.get(), siUnitType.get(), scope));
            }
            return Optional.empty();
        }
        return super.calculateRegularAssignment(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the type of the binary operations (&=,|=,^=)
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeBinaryOperations(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isPrimitiveWithSIUnitType(leftResult)
                && isIntegralType(getPrimitiveOfPrimitiveWithSIUnitType(leftResult).get())
                && isIntegralType(rightResult)) {
            return Optional.of(leftResult);
        }
        return super.calculateTypeBinaryOperations(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the type of the bitshift operations (<<=, >>=, >>>=)
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeBitOperation(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        //the bitshift operations are only defined for integers --> long, int, char, short, byte
        if (isPrimitiveWithSIUnitType(leftResult)
                && isIntegralType(getPrimitiveOfPrimitiveWithSIUnitType(leftResult).get())
                && isIntegralType(rightResult)) {
            return Optional.of(leftResult);
        }
        return super.calculateTypeBitOperation(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected Optional<SymTypeExpression> getUnaryNumericPromotionType(SymTypeExpression type) {
        if (isPrimitiveWithSIUnitType(type)) {
            Optional<SymTypeExpression> primitiveType = getPrimitiveOfPrimitiveWithSIUnitType(type);
            Optional<SymTypeExpression> siUnitType = getSIUnitOfPrimitiveWithSIUnitType(type);
            primitiveType = super.getUnaryNumericPromotionType(primitiveType.get());
            if (primitiveType.isPresent() && isSymTypeConstant(primitiveType.get())
                    && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createPrimitiveWithSIUnitType((SymTypeConstant) primitiveType.get(), siUnitType.get(), scope));
            }
            return Optional.empty();
        }
        return super.getUnaryNumericPromotionType(type);
    }
}
