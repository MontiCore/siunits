package de.monticore.types.check;

import de.monticore.expressions.assignmentexpressions._ast.ASTConstantsAssignmentExpressions;

import java.util.Optional;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.*;

public class DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes extends DeriveSymTypeOfAssignmentExpressions {

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
        //if the left and the right result are a siunit type then the type of the whole expression is the type of the left expression
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult)) {
            if ((operation == ASTConstantsAssignmentExpressions.PLUSEQUALS ||
                    operation == ASTConstantsAssignmentExpressions.MINUSEQUALS) &&
                    leftResult.print().equals(rightResult.print())) {
                return Optional.of(leftResult);
            } else if (operation == ASTConstantsAssignmentExpressions.STAREQUALS) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(leftResult.print() + "*" + rightResult.print(), scope));
            } else if (operation == ASTConstantsAssignmentExpressions.SLASHEQUALS) {
                return Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(leftResult.print() + "/(" + rightResult.print() + ")", scope));
            }
        } else if (isSIUnitType(leftResult) && isNumericType(rightResult)) {
            return Optional.of(leftResult);
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult)) {
            return Optional.of(rightResult);
        }
        return super.calculateTypeArithmetic(operation, leftResult, rightResult);
    }
}
