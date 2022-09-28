/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.se_rwth.commons.SourcePosition;

import static de.monticore.types.check.TypeCheck.isString;

/**
 * This class is used to derive the symtype of assignment expressions where SI
 * unit types are used, including primitive with SI unit types.
 */
public class DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes extends DeriveSymTypeOfAssignmentExpressions {


  @Override
  protected SymTypeExpression addAssignment(SymTypeExpression left,
                                            SymTypeExpression right,
                                            SourcePosition pos) {
    if (isString(left)) {
      return left;
    }
    return this.arithmeticBinarySIAssignment(left, right, "+=", pos);
  }

  @Override
  protected SymTypeExpression subtractAssignment(SymTypeExpression left,
                                                 SymTypeExpression right,
                                                 SourcePosition pos) {
    return this.arithmeticBinarySIAssignment(left, right, "-=", pos);
  }

  @Override
  protected SymTypeExpression moduloAssignment(SymTypeExpression left,
                                               SymTypeExpression right,
                                               SourcePosition pos) {
    return this.arithmeticBinarySIAssignment(left, right, "%=", pos);
  }

  @Override
  protected SymTypeExpression multiplyAssignment(SymTypeExpression left,
                                                 SymTypeExpression right,
                                                 SourcePosition pos) {
    return this.arithmeticUnarySIAssignment(left, right, "*=", pos);
  }

  @Override
  protected SymTypeExpression divideAssignment(SymTypeExpression left,
                                               SymTypeExpression right,
                                               SourcePosition pos) {
    return this.arithmeticUnarySIAssignment(left, right, "/=", pos);
  }

  protected SymTypeExpression arithmeticBinarySIAssignment(SymTypeExpression left,
                                                           SymTypeExpression right,
                                                           String op,
                                                           SourcePosition pos) {
    // case for +=, -= and %=, can only be calculated for equal si-unit types
    if (isNumericWithSIUnitType(left) && isNumericWithSIUnitType(right)) {
      SymTypeExpression leftNumericType = getNumeric(left);
      SymTypeExpression rightNumericType = getNumeric(right);
      SymTypeExpression leftSIUnit = getSIUnit(left);
      SymTypeExpression rightSIUnit = getSIUnit(right);
      if (leftNumericType.isObscureType() || rightNumericType.isObjectType()
        || leftSIUnit.isObscureType() || rightSIUnit.isObscureType()) {
        // if inner obscure then error already logged
        return SymTypeExpressionFactory.createObscureType();
      } else {
        SymTypeExpression numericType = super.arithmeticAssignment(leftNumericType, rightNumericType, op, pos);
        if (numericType.isObscureType()) {
          // if numeric obscure then error already logged
          return SymTypeExpressionFactory.createObscureType();
        } else if (isNumericType(numericType) && TypeCheck.compatible(leftSIUnit, rightSIUnit)) {
          // si units must be compatible
          return left;
        } else {
          return super.arithmeticAssignment(left, right, op, pos);
        }
      }
    } else {
      return super.arithmeticAssignment(left, right, op, pos);
    }
  }


  protected SymTypeExpression arithmeticUnarySIAssignment(SymTypeExpression left,
                                                          SymTypeExpression right,
                                                          String op,
                                                          SourcePosition pos) {
    // case for *= and /= can only be calculated if the right type is a numeric type, e.g. var_Int_M *= 3
    if (isNumericWithSIUnitType(left) && isNumericType(right)) {
      SymTypeExpression leftNumericType = getNumeric(left);
      SymTypeExpression siUnitType = getSIUnit(left);
      if (leftNumericType.isObscureType() || siUnitType.isObscureType() || right.isObscureType()) {
        // if inner obscure then error already logged
        return SymTypeExpressionFactory.createObscureType();
      } else {
        SymTypeExpression numericType = super.arithmeticAssignment(leftNumericType, right, op, pos);
        if (numericType.isObscureType()) {
          // if numeric obscure then error already logged
          return SymTypeExpressionFactory.createObscureType();
        } else if (isNumericType(numericType)) {
          return left;
        } else {
          return super.arithmeticAssignment(left, right, op, pos);
        }
      }
    } else {
      return super.arithmeticAssignment(left, right, op, pos);
    }
  }

  @Override
  protected SymTypeExpression assignment(SymTypeExpression left,
                                         SymTypeExpression right,
                                         SourcePosition pos) {
    if (isNumericWithSIUnitType(left) && isNumericWithSIUnitType(right)) {
      // both are numericWithSIUnit types and are assignable
      SymTypeExpression leftNumericType = getNumeric(left);
      SymTypeExpression rightNumericType = getNumeric(right);
      SymTypeExpression leftSIUnit = getSIUnit(left);
      SymTypeExpression rightSIUnit = getSIUnit(right);
      if (leftNumericType.isObscureType() || rightNumericType.isObscureType()
        || leftSIUnit.isObscureType() || rightSIUnit.isObscureType()) {
        // if inner obscure then error already logged
        return SymTypeExpressionFactory.createObscureType();
      } else {
        SymTypeExpression numericType = super.assignment(leftNumericType, rightNumericType, pos);
        SymTypeExpression siUnitType = super.assignment(leftSIUnit, rightSIUnit, pos);
        if (numericType.isObscureType() || siUnitType.isObscureType()) {
          // if inner obscure then error already logged
          return SymTypeExpressionFactory.createObscureType();
        } else if (isNumericType(numericType)) {
          return left;
        } else {
          return super.assignment(left, right, pos);
        }
      }
    } else {
      return super.assignment(left, right, pos);
    }
  }

  @Override
  protected SymTypeExpression binaryAssignment(SymTypeExpression left,
                                               SymTypeExpression right,
                                               String op,
                                               SourcePosition pos) {
    // if left numeric then left and right should be integral
    if (isNumericWithSIUnitType(left)
      && isIntegralType(getNumeric(left))
      && isIntegralType(right)) {
      return left;
    } else {
      return super.binaryAssignment(left, right, op, pos);
    }
  }

  @Override
  protected SymTypeExpression bitAssignment(SymTypeExpression left,
                                            SymTypeExpression right,
                                            String op,
                                            SourcePosition src) {
    if (isNumericWithSIUnitType(left)
      && isIntegralType(getNumeric(left))
      && isIntegralType(right)) {
      return left;
    } else {
      return super.bitAssignment(left, right, op, src);
    }
  }

  @Override
  protected SymTypeExpression affix(SymTypeExpression inner,
                                    String op,
                                    SourcePosition pos) {
    if (isNumericWithSIUnitType(inner)) {
      SymTypeExpression numeric = getNumeric(inner);
      SymTypeExpression siUnit = getSIUnit(inner);
      if (numeric.isObscureType() || siUnit.isObscureType()) {
        // if inner obscure then error already logged
        return SymTypeExpressionFactory.createObscureType();
      } else {
        numeric = super.affix(numeric, op, pos);
        if (numeric.isObscureType()) {
          // if numeric obscure then error already logged
          return SymTypeExpressionFactory.createObscureType();
        } else if (isNumericType(numeric)) {
          return inner;
        } else {
          return super.affix(numeric, op, pos);
        }
      }
    } else {
      return super.affix(inner, op, pos);
    }
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
