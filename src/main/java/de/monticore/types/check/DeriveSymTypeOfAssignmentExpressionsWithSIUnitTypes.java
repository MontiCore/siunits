package de.monticore.types.check;

import de.monticore.expressions.assignmentexpressions._ast.ASTAssignmentExpression;
import de.monticore.expressions.assignmentexpressions._ast.ASTConstantsAssignmentExpressions;

import java.util.Optional;

/**
 * This class is used to derive the symtype of assignment expressions where SI unit types are used, including primitive
 * with SI unit types.
 */
public class DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes extends DeriveSymTypeOfAssignmentExpressions {

    /**
     * helper method for the basic arithmetic assignment operations +=,-=,%=,*=,/=
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeArithmetic(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.PLUSEQUALS
                || expr.getOperator() == ASTConstantsAssignmentExpressions.MINUSEQUALS
                || expr.getOperator() == ASTConstantsAssignmentExpressions.PERCENTEQUALS)) {
            // case for +=, -= and %=, can only be calculated for equal siunit types
            Optional<SymTypeExpression> leftNumericType = getNumeric(leftResult);
            Optional<SymTypeExpression> rightNumericType = getNumeric(rightResult);
            Optional<SymTypeExpression> leftSIUnit = getSIUnit(leftResult);
            Optional<SymTypeExpression> rightSIUnit = getSIUnit(rightResult);
            Optional<SymTypeExpression> numericType = super.calculateTypeArithmetic(
                    expr, leftNumericType.get(), rightNumericType.get());
            if (numericType.isPresent() && isNumericType(numericType.get())
                    && TypeCheck.compatible(leftSIUnit.get(), rightSIUnit.get())) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createNumericWithSIUnitType(numericType.get(), leftSIUnit.get(), getScope(expr.getEnclosingScope())));
            }
        } else if (isNumericWithSIUnitType(leftResult) && isNumericType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.STAREQUALS ||
                expr.getOperator() == ASTConstantsAssignmentExpressions.SLASHEQUALS)) {
            // case for *= and /= can only be calculated if the right type is a numeric type, e.g. var_Int_M *= 3
            Optional<SymTypeExpression> leftNumericType = getNumeric(leftResult);
            Optional<SymTypeExpression> numericType = calculateTypeArithmetic(expr, leftNumericType.get(), rightResult);
            Optional<SymTypeExpression> siUnitType = getSIUnit(leftResult);
            if (numericType.isPresent() && isNumericType(numericType.get()) && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                        numericType.get(), siUnitType.get(), getScope(expr.getEnclosingScope())));
            }
        }
        return super.calculateTypeArithmetic(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of a regular assignment (=)
     */
    @Override
    protected Optional<SymTypeExpression> calculateRegularAssignment(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            // both are numericWithSIUnit types and are assignable
            Optional<SymTypeExpression> leftNumericType = getNumeric(leftResult);
            Optional<SymTypeExpression> rightNumericType = getNumeric(rightResult);
            Optional<SymTypeExpression> leftSIUnit = getSIUnit(leftResult);
            Optional<SymTypeExpression> rightSIUnit = getSIUnit(rightResult);
            Optional<SymTypeExpression> numericType = super.calculateRegularAssignment(expr, leftNumericType.get(), rightNumericType.get());
            // assignments of SIUnit types are covered by the super class
            Optional<SymTypeExpression> siUnitType = super.calculateRegularAssignment(expr, leftSIUnit.get(), rightSIUnit.get());
            if (numericType.isPresent() && isNumericType(numericType.get()) && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                        numericType.get(), siUnitType.get(), getScope(expr.getEnclosingScope())));
            }
        }
        return super.calculateRegularAssignment(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the type of the binary operations (&=,|=,^=)
     * for numeric with SIUnit types the result can be the numeric with SIUnit type again
     * e.g. 5 m |= 3 => 7 m
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeBinaryOperations(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult)
                && isIntegralType(getNumeric(leftResult).get())
                && isIntegralType(rightResult)) {
            return Optional.of(leftResult);
        }
        return super.calculateTypeBinaryOperations(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the type of the bitshift operations (<<=, >>=, >>>=)
     * for numeric with SIUnit types the result can be the numeric with SIUnit type again
     * e.g. 4 m >>= 1 => 2 m
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeBitOperation(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        //the bitshift operations are only defined for integers --> long, int, char, short, byte
        if (isNumericWithSIUnitType(leftResult)
                && isIntegralType(getNumeric(leftResult).get())
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
        if (isNumericWithSIUnitType(type)) {
            Optional<SymTypeExpression> numericType = getNumeric(type);
            Optional<SymTypeExpression> siUnitType = getSIUnit(type);
            numericType = super.getUnaryNumericPromotionType(numericType.get());
            if (numericType.isPresent() && isNumericType(numericType.get())) {
                return Optional.of(SIUnitSymTypeExpressionFactory.
                        createNumericWithSIUnitType(numericType.get(), siUnitType.get(), type.getTypeInfo().getEnclosingScope()));
            }
            return Optional.empty();
        }
        return super.getUnaryNumericPromotionType(type);
    }

    public boolean isSIUnitType(SymTypeExpression type) {
        return type instanceof SymTypeOfSIUnit;
    }

    public boolean isNumericWithSIUnitType(SymTypeExpression type) {
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
    public Optional<SymTypeExpression> getSIUnit(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return Optional.ofNullable(((SymTypeOfNumericWithSIUnit) type).getSIUnit());
        } else if (isSIUnitType(type)) {
            return Optional.of(type);
        }
        return Optional.empty();
    }
}
