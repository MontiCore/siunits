package de.monticore.types.check;

import java.util.Optional;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.*;

public class DeriveSymTypeOfAssignmentExpressionsWithPrimitiveWithSIUnitTypes extends DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes {

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected Optional<SymTypeExpression> getUnaryNumericPromotionType(SymTypeExpression type) {
        if (isSIUnitType(type)) {
            return Optional.of(type);
        } else if (isPrimitiveWithSIUnitType(type))
            return Optional.of(type);
        return super.getUnaryNumericPromotionType(type);
    }

    /**
     * helper method for the five basic arithmetic assignment operations (+=,-=,*=,/=,%=)
     */
    @Override
    protected Optional<SymTypeExpression> calculateTypeArithmetic(int operation, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isNumericType(rightResult)) {
            return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(leftResult, (SymTypeConstant) rightResult, scope));
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult)) {
            return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(rightResult, (SymTypeConstant) leftResult, scope));
        } else if (isPrimitiveWithSIUnitType(leftResult) || isPrimitiveWithSIUnitType(rightResult)) {
            // main case to handle primitiveWithSIUnitTypes
            // left -and right primitive -and siunits can be null
            Optional<SymTypeExpression> leftPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightPrimitive = getPrimitiveOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> leftSIUnit = getSIUnitOfPrimitiveWithSIUnitType(leftResult);
            Optional<SymTypeExpression> rightSIUnit = getSIUnitOfPrimitiveWithSIUnitType(rightResult);
            Optional<SymTypeExpression> primitveType;
            Optional<SymTypeExpression> siUnitType;

            if (leftPrimitive.isPresent() && rightPrimitive.isPresent()) {
                primitveType = calculateTypeArithmetic(operation, leftPrimitive.get(), rightPrimitive.get());
            } else if (leftPrimitive.isPresent()) {
                primitveType = leftPrimitive;
            } else {
                primitveType = rightPrimitive;
            }

            if (leftSIUnit.isPresent() && rightSIUnit.isPresent()) {
                siUnitType = calculateTypeArithmetic(operation, leftSIUnit.get(), rightSIUnit.get());
            } else if(leftSIUnit.isPresent()) {
                siUnitType = leftSIUnit;
            } else {
                siUnitType = rightSIUnit;
            }

            if (primitveType.isPresent() && isPrimitive(primitveType.get()) && siUnitType.isPresent()) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createPrimitiveWithSIUnitType(siUnitType.get(), (SymTypeConstant) primitveType.get(), scope));
            }
        }
        return super.calculateTypeArithmetic(operation, leftResult, rightResult);
    }
}
