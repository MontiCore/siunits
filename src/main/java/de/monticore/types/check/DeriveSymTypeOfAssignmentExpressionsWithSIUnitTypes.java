/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import com.google.common.collect.Lists;
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
    protected SymTypeExpression calculateTypeArithmeticAssignment(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.PLUSEQUALS
                || expr.getOperator() == ASTConstantsAssignmentExpressions.MINUSEQUALS
                || expr.getOperator() == ASTConstantsAssignmentExpressions.PERCENTEQUALS)) {
            // case for +=, -= and %=, can only be calculated for equal siunit types
            SymTypeExpression leftNumericType = getNumeric(leftResult);
            SymTypeExpression rightNumericType = getNumeric(rightResult);
            SymTypeExpression leftSIUnit = getSIUnit(leftResult);
            SymTypeExpression rightSIUnit = getSIUnit(rightResult);
            if(checkNotObscure(Lists.newArrayList(leftNumericType, rightNumericType, leftSIUnit, rightSIUnit))) {
                SymTypeExpression numericType = super.calculateTypeArithmeticAssignment(
                  expr, leftNumericType, rightNumericType);
                if (isNumericType(numericType) && TypeCheck.compatible(leftSIUnit, rightSIUnit)) {
                    return SIUnitSymTypeExpressionFactory.
                      createNumericWithSIUnitType(numericType, leftSIUnit, getScope(expr.getEnclosingScope()));
                }
            }else{
                return SymTypeExpressionFactory.createObscureType();
            }
        } else if (isNumericWithSIUnitType(leftResult) && isNumericType(rightResult)
                && (expr.getOperator() == ASTConstantsAssignmentExpressions.STAREQUALS ||
                expr.getOperator() == ASTConstantsAssignmentExpressions.SLASHEQUALS)) {
            // case for *= and /= can only be calculated if the right type is a numeric type, e.g. var_Int_M *= 3
            SymTypeExpression leftNumericType = getNumeric(leftResult);
            SymTypeExpression siUnitType = getSIUnit(leftResult);
            if(!leftNumericType.isObscureType() && !siUnitType.isObscureType()) {
                SymTypeExpression numericType = calculateTypeArithmeticAssignment(expr, leftNumericType, rightResult);
                if (!numericType.isObscureType() && isNumericType(numericType)) {
                    return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                      numericType, siUnitType, getScope(expr.getEnclosingScope()));
                } else {
                    return SymTypeExpressionFactory.createObscureType();
                }
            }else{
                return SymTypeExpressionFactory.createObscureType();
            }
        }
        return super.calculateTypeArithmeticAssignment(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of a regular assignment (=)
     */
    @Override
    protected SymTypeExpression calculateRegularAssignment(ASTAssignmentExpression expr, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            // both are numericWithSIUnit types and are assignable
            SymTypeExpression leftNumericType = getNumeric(leftResult);
            SymTypeExpression rightNumericType = getNumeric(rightResult);
            SymTypeExpression leftSIUnit = getSIUnit(leftResult);
            SymTypeExpression rightSIUnit = getSIUnit(rightResult);
            if(checkNotObscure(Lists.newArrayList(leftNumericType, rightNumericType, leftSIUnit, rightSIUnit))) {
                SymTypeExpression numericType = super.calculateRegularAssignment(expr, leftNumericType, rightNumericType);
                // assignments of SIUnit types are covered by the super class
                SymTypeExpression siUnitType = super.calculateRegularAssignment(expr, leftSIUnit, rightSIUnit);
                if(!numericType.isObscureType() && !siUnitType.isObscureType()) {
                    if (isNumericType(numericType)) {
                        return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                          numericType, siUnitType, getScope(expr.getEnclosingScope()));
                    }
                }else{
                    return SymTypeExpressionFactory.createObscureType();
                }
            } else {
                return SymTypeExpressionFactory.createObscureType();
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
    protected SymTypeExpression calculateTypeBinaryOperationsAssignment(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isNumericWithSIUnitType(leftResult)
                && isIntegralType(getNumeric(leftResult))
                && isIntegralType(rightResult)) {
            return leftResult;
        }
        return super.calculateTypeBinaryOperationsAssignment(leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the type of the bitshift operations (<<=, >>=, >>>=)
     * for numeric with SIUnit types the result can be the numeric with SIUnit type again
     * e.g. 4 m >>= 1 => 2 m
     */
    @Override
    protected SymTypeExpression calculateTypeBitOperationAssignment(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        //the bitshift operations are only defined for integers --> long, int, char, short, byte
        if (isNumericWithSIUnitType(leftResult)
                && isIntegralType(getNumeric(leftResult))
                && isIntegralType(rightResult)) {
            return leftResult;
        }
        return super.calculateTypeBitOperationAssignment(leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected SymTypeExpression getUnaryNumericPromotionType(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            SymTypeExpression numericType = getNumeric(type);
            SymTypeExpression siUnitType = getSIUnit(type);
            if(!numericType.isObscureType() && !siUnitType.isObscureType()) {
                numericType = super.getUnaryNumericPromotionType(numericType);
                if (!numericType.isObscureType() && isNumericType(numericType)) {
                    return SIUnitSymTypeExpressionFactory.
                      createNumericWithSIUnitType(numericType, siUnitType, type.getTypeInfo().getEnclosingScope());
                }
                return SymTypeExpressionFactory.createObscureType();
            }else{
                return SymTypeExpressionFactory.createObscureType();
            }
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
    private SymTypeExpression getNumeric(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return ((SymTypeOfNumericWithSIUnit) type).getNumericType();
        } else if (isNumericType(type)) {
            return type;
        }
        return SymTypeExpressionFactory.createObscureType();
    }

    /**
     * Helper method to extract siunit type of an SymTypeExpression
     */
    public SymTypeExpression getSIUnit(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return ((SymTypeOfNumericWithSIUnit) type).getSIUnit();
        } else if (isSIUnitType(type)) {
            return type;
        }
        return SymTypeExpressionFactory.createObscureType();
    }
}
