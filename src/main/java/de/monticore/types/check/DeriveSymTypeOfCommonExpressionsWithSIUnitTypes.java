/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.utility.Converter;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.se_rwth.commons.SourcePosition;

import javax.measure.unit.Unit;

/**
 * This class is used to derive the symtype of common expressions where SI unit types are used, including primitive
 * with SI unit types.
 */
public class DeriveSymTypeOfCommonExpressionsWithSIUnitTypes extends DeriveSymTypeOfCommonExpressions {

    @Override
    protected SymTypeExpression calculateMultExpression(SymTypeExpression left, SymTypeExpression right, SourcePosition pos) {
        return this.calculateMultDivideExpression(left, right, "*", pos);
    }

    @Override
    protected SymTypeExpression calculateDivideExpression(SymTypeExpression left, SymTypeExpression right, SourcePosition pos) {
        return this.calculateMultDivideExpression(left, right, "/", pos);
    }

    protected SymTypeExpression calculateMultDivideExpression(SymTypeExpression left, SymTypeExpression right, String op, SourcePosition pos) {
        if (isSIUnitType(left) && isSIUnitType(right)) {
            // if both are SI unit types then the result has to be calculated depending on the operator
            String newUnitName = "(" + printType(left) + ")" + op + "(" + printType(right) + ")";
            return SIUnitSymTypeExpressionFactory._deprecated_createSIUnit(newUnitName);
        } else if (isSIUnitType(left) && isNumericType(right)) {
            return SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(right, left);
        } else if (isNumericType(left) && isSIUnitType(right)) {
            SymTypeExpression siUnitType;
            if ("*".equals(op))
                siUnitType = right;
            else {
                siUnitType = SIUnitSymTypeExpressionFactory._deprecated_createSIUnit("(" + printType(right) + ")^-1");
            }
            return SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(left, siUnitType);
        } else if (isNumericWithSIUnitType(left) || isNumericWithSIUnitType(right)) {
            // The result is again a SIUnitType4Computing
            SymTypeExpression numericType;
            SymTypeExpression leftNumeric = getNumeric(left);
            SymTypeExpression rightNumeric = getNumeric(right);
            if (isSIUnitType(left)) {
                numericType = rightNumeric;
            } else if (isSIUnitType(right)) {
                numericType = leftNumeric;
            } else if (leftNumeric.isObscureType() || rightNumeric.isObscureType()) {
                numericType = super.calculateArithmeticExpression(left, right, op, pos);
            } else {
                numericType = super.calculateArithmeticExpression(leftNumeric, rightNumeric, op, pos);
            }
            if (!numericType.isObscureType() && isNumericType(numericType)) {
                SymTypeExpression leftSIUnitType = getSIUnit(left);
                SymTypeExpression rightSIUnitType = getSIUnit(right);
                SymTypeExpression siUnitType;
                if (leftSIUnitType.isObscureType()) {
                    siUnitType = rightSIUnitType;
                } else if (rightSIUnitType.isObscureType()) {
                    siUnitType = leftSIUnitType;
                } else {
                    siUnitType = calculateMultDivideExpression(leftSIUnitType, rightSIUnitType, op, pos);
                }
                return SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(numericType, siUnitType);
            }
        } else {
            return super.calculateArithmeticExpression(left, right, op, pos);
        }
        return SymTypeExpressionFactory.createObscureType();
    }

    /**
     * helper method for <=, >=, <, > -> calculates the result of these expressions
     */
    @Override
    protected SymTypeExpression calculateTypeCompare(SymTypeExpression left, SymTypeExpression right, String op, SourcePosition pos) {
        if (isNumericWithSIUnitType(left) && isNumericWithSIUnitType(right)) {
            SymTypeExpression leftSIUnitType = getSIUnit(left);
            SymTypeExpression rightSIUnitType = getSIUnit(right);
            if (TypeCheck.compatible(leftSIUnitType, rightSIUnitType)) {
                return SymTypeExpressionFactory.createPrimitive("boolean");
            }
        }
        return super.calculateTypeCompare(left, right, op, pos);
    }

    /**
     * helper method for the calculation of the ASTEqualsExpression and the ASTNotEqualsExpression
     */
    @Override
    protected SymTypeExpression calculateTypeLogical(SymTypeExpression left, SymTypeExpression right, String op, SourcePosition pos) {
        if (isSIUnitType(left) && isSIUnitType(right)) {
            return SymTypeExpressionFactory.createPrimitive(BasicSymbolsMill.BOOLEAN);
        } else if (isNumericWithSIUnitType(left) && isNumericWithSIUnitType(right)) {
            return SymTypeExpressionFactory.createPrimitive(BasicSymbolsMill.BOOLEAN);
        }
        return super.calculateTypeLogical(left, right, op, pos);
    }

    /**
     * return the result for the basic arithmetic operations +,-,%
     */
    @Override
    protected SymTypeExpression calculateArithmeticExpression(SymTypeExpression left, SymTypeExpression right, String op, SourcePosition pos) {
        // Case for +,-,% and as result of a Conditional Expression
        if (isNumericWithSIUnitType(left) && isNumericWithSIUnitType(right)) {
            SymTypeExpression leftSIUnitType = getSIUnit(left);
            SymTypeExpression rightSIUnitType = getSIUnit(right);
            if (leftSIUnitType.isObscureType() || rightSIUnitType.isObscureType()) {
                // error already logged
                return SymTypeExpressionFactory.createObscureType();
            } else if (TypeCheck.compatible(leftSIUnitType, rightSIUnitType)) {
                SymTypeExpression numericType;
                if (isSIUnitType(left)) {
                    numericType = getNumeric(right);
                } else if (isSIUnitType(right)) {
                    numericType = getNumeric(left);
                } else {
                    numericType = super.calculateArithmeticExpression(getNumeric(left), getNumeric(right), op, pos);
                }
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

                    return SIUnitSymTypeExpressionFactory._deprecated_createNumericWithSIUnitType(
                      numericType, typeOfSIUnit, left.getTypeInfo().getEnclosingScope());
                } else {
                    return super.calculateArithmeticExpression(left, right, op, pos);
                }
            }
        }
        return super.calculateArithmeticExpression(left, right, op, pos);
    }

    @Override
    protected SymTypeExpression booleanNot(SymTypeExpression inner,
                                                     SourcePosition pos) {
        if (isNumericWithSIUnitType(inner)) {
            SymTypeExpression numericType = getNumeric(inner);
            SymTypeExpression siUnitType = getSIUnit(inner);
            if (!numericType.isObscureType()) {
                numericType = super.booleanNot(numericType, pos);
                if (!numericType.isObscureType() && isNumericType(numericType)
                  && !siUnitType.isObscureType()) {
                    return SIUnitSymTypeExpressionFactory.
                        _deprecated_createNumericWithSIUnitType(numericType, siUnitType, inner.getTypeInfo().getEnclosingScope());
                }
                return SymTypeExpressionFactory.createObscureType();
            }
        }
        return super.booleanNot(inner, pos);
    }


    @Override
    protected SymTypeExpression numericPrefix(SymTypeExpression inner,
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
                        _deprecated_createNumericWithSIUnitType(numericType, siUnitType, inner.getTypeInfo()
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
