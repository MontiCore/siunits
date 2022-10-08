/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.commonexpressions._ast.ASTDivideExpression;
import de.monticore.expressions.commonexpressions._ast.ASTInfixExpression;
import de.monticore.expressions.commonexpressions._ast.ASTMultExpression;
import de.monticore.siunits.utility.Converter;
import de.se_rwth.commons.SourcePosition;

import javax.measure.unit.Unit;

/**
 * This class is used to derive the symtype of common expressions where SI unit types are used, including primitive
 * with SI unit types.
 */
public class DeriveSymTypeOfCommonExpressionsWithSIUnitTypes extends DeriveSymTypeOfCommonExpressions {

     @Override
    protected SymTypeExpression calculateMultExpression(ASTMultExpression expr, SymTypeExpression left, SymTypeExpression right) {
         SymTypeExpression wholeResult = calculateMultDivideExpression(expr, "*", left, right);
         if(wholeResult.isObscureType()) {
             return super.calculateMultExpression(expr, left, right);
         }else{
             return wholeResult;
         }
    }

    @Override
    protected SymTypeExpression calculateDivideExpression(ASTDivideExpression expr, SymTypeExpression left, SymTypeExpression right) {
        SymTypeExpression wholeResult = calculateMultDivideExpression(expr, "/", left, right);
        if(wholeResult.isObscureType()) {
            return super.calculateDivideExpression(expr, left, right);
        }else{
            return wholeResult;
        }
    }

    protected SymTypeExpression calculateMultDivideExpression(ASTInfixExpression expr, String operator, SymTypeExpression leftResult, SymTypeExpression rightResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult)) {
            // if both are SI unit types the result the result has to be calculated depending on the operator
            String newUnitName = "(" + printType(leftResult) + ")" + operator + "(" + printType(rightResult) + ")";
            return SIUnitSymTypeExpressionFactory.createSIUnit(newUnitName, getScope(expr.getEnclosingScope()));
        } else if (isSIUnitType(leftResult) && isNumericType(rightResult)) {
            return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(rightResult, leftResult, getScope(expr.getEnclosingScope()));
        } else if (isNumericType(leftResult) && isSIUnitType(rightResult)) {
            SymTypeExpression siUnitType;
            if ("*".equals(operator))
                siUnitType = rightResult;
            else {
                siUnitType = SIUnitSymTypeExpressionFactory.createSIUnit("(" + printType(rightResult) + ")^-1", getScope(expr.getEnclosingScope()));
            }
            return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(leftResult, siUnitType, getScope(expr.getEnclosingScope()));
        } else if (isNumericWithSIUnitType(leftResult) || isNumericWithSIUnitType(rightResult)) {
            // The result is again a SIUnitType4Computing
            SymTypeExpression numericType = getBinaryNumericPromotionOfNumeric(leftResult, rightResult);
            if (!numericType.isObscureType() && isNumericType(numericType)) {
                SymTypeExpression leftSIUnitType = getSIUnit(leftResult);
                SymTypeExpression rightSIUnitType = getSIUnit(rightResult);
                SymTypeExpression siUnitType;
                if (leftSIUnitType.isObscureType()) {
                    siUnitType = rightSIUnitType;
                } else if (rightSIUnitType.isObscureType()) {
                    siUnitType = leftSIUnitType;
                } else {
                    siUnitType = calculateMultDivideExpression(expr, operator, leftSIUnitType, rightSIUnitType);
                }
                return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                        numericType, siUnitType, getScope(expr.getEnclosingScope()));
            }
        }
        return SymTypeExpressionFactory.createObscureType();
    }

    /**
     * helper method for <=, >=, <, > -> calculates the result of these expressions
     */
    protected SymTypeExpression calculateTypeCompare(ASTInfixExpression expr, SymTypeExpression rightResult, SymTypeExpression leftResult) {
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            SymTypeExpression leftSIUnitType = getSIUnit(leftResult);
            SymTypeExpression rightSIUnitType = getSIUnit(rightResult);
            if (TypeCheck.compatible(leftSIUnitType, rightSIUnitType)) {
                return SymTypeExpressionFactory.createPrimitive("boolean");
            }
            return SymTypeExpressionFactory.createObscureType();
        }
        return super.calculateTypeCompare(expr, leftResult, rightResult);
    }

    /**
     * helper method for the calculation of the ASTEqualsExpression and the ASTNotEqualsExpression
     */
    protected SymTypeExpression calculateTypeLogical(ASTInfixExpression expr, SymTypeExpression rightResult, SymTypeExpression leftResult) {
        if (isSIUnitType(leftResult) && isSIUnitType(rightResult)) {
            return SymTypeExpressionFactory.createPrimitive("boolean");
        } else if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            return SymTypeExpressionFactory.createPrimitive("boolean");
        }
        return super.calculateTypeLogical(expr, leftResult, rightResult);
    }

    /**
     * return the result for the basic arithmetic operations +,-,%
     */
    @Override
    protected SymTypeExpression getBinaryNumericPromotion(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        // Case for +,-,% and as result of a Conditional Expression
        if (isNumericWithSIUnitType(leftResult) && isNumericWithSIUnitType(rightResult)) {
            SymTypeExpression leftSIUnitType = getSIUnit(leftResult);
            SymTypeExpression rightSIUnitType = getSIUnit(rightResult);
            if (!leftSIUnitType.isObscureType() && !rightSIUnitType.isObscureType()
                    && TypeCheck.compatible(leftSIUnitType, rightSIUnitType)) {

                SymTypeExpression numericType = getBinaryNumericPromotionOfNumeric(leftResult, rightResult);
                if (!numericType.isObscureType() && isNumericType(numericType)) {
                    Unit leftUnit = null;
                    if (isSIUnitType(leftSIUnitType))
                        leftUnit = ((SymTypeOfSIUnit) leftSIUnitType).getUnit();
                    Unit rightUnit = null;
                    if (isSIUnitType(rightSIUnitType))
                        rightUnit = ((SymTypeOfSIUnit) rightSIUnitType).getUnit();
                    SymTypeExpression typeOfSIUnit = leftSIUnitType;
                    if (leftUnit != null && rightUnit != null)
                        if (Converter.convert(1, leftUnit, rightUnit) > 1)
                            typeOfSIUnit = rightSIUnitType;

                    return SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(
                            numericType, typeOfSIUnit, leftResult.getTypeInfo().getEnclosingScope());
                }
            }
            // Should not happen, will be handled in traverse
            return SymTypeExpressionFactory.createObscureType();
        }
        return super.getBinaryNumericPromotion(leftResult, rightResult);
    }

    private SymTypeExpression getBinaryNumericPromotionOfNumeric(SymTypeExpression leftResult, SymTypeExpression rightResult) {
        SymTypeExpression leftNumericType = getNumeric(leftResult);
        SymTypeExpression rightNumericType = getNumeric(rightResult);
        if (isSIUnitType(leftResult))
            return rightNumericType;
        if (isSIUnitType(rightResult))
            return leftNumericType;
        if (leftNumericType.isObscureType() || rightNumericType.isObscureType())
            return SymTypeExpressionFactory.createObscureType();
        return super.getBinaryNumericPromotion(leftNumericType, rightNumericType);
    }

    /**
     * helper method for the calculation of the ASTBooleanNotExpression
     */
    @Override
    protected SymTypeExpression getUnaryIntegralPromotionType(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            SymTypeExpression numericType = getNumeric(type);
            SymTypeExpression siUnitType = getSIUnit(type);
            if (!numericType.isObscureType()) {
                numericType = super.getUnaryIntegralPromotionType(numericType);
                if (!numericType.isObscureType() && isNumericType(numericType)
                  && !siUnitType.isObscureType()) {
                    return SIUnitSymTypeExpressionFactory.
                      createNumericWithSIUnitType(numericType, siUnitType, type.getTypeInfo().getEnclosingScope());
                }
                return SymTypeExpressionFactory.createObscureType();
            }
        }
        return super.getUnaryIntegralPromotionType(type);
    }

    @Override protected SymTypeExpression numericPrefix(SymTypeExpression inner,
                                                        String op,
                                                        SourcePosition pos) {
        if (isNumericWithSIUnitType(inner)) {
            SymTypeExpression numericType = getNumeric(inner);
            SymTypeExpression siUnitType = getSIUnit(inner);
            if (!numericType.isObscureType()) {
                numericType = super.numericPrefix(numericType, op, pos);
                if (!numericType.isObscureType() && isNumericType(numericType)
                  && !siUnitType.isObscureType()) {
                    return SIUnitSymTypeExpressionFactory.
                      createNumericWithSIUnitType(numericType, siUnitType, inner.getTypeInfo()
                        .getEnclosingScope());
                }
                return SymTypeExpressionFactory.createObscureType();
            } else {
                return SymTypeExpressionFactory.createObscureType();
            }
        } else {
            return super.numericPrefix(inner, op, pos);
        }
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
    private SymTypeExpression getSIUnit(SymTypeExpression type) {
        if (isNumericWithSIUnitType(type)) {
            return ((SymTypeOfNumericWithSIUnit) type).getSIUnit();
        } else if(isSIUnitType(type)) {
            return type;
        }
        return SymTypeExpressionFactory.createObscureType();
    }

    protected String printType(SymTypeExpression symType) {
        return symType.print();
    }
}
