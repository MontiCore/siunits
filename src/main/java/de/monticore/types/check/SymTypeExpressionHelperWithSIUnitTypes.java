package de.monticore.types.check;

import java.util.Optional;

public class SymTypeExpressionHelperWithSIUnitTypes extends SymTypeExpressionHelper {
    public static boolean isSIUnitType(SymTypeExpression type) {
        return type instanceof SymTypeOfSIUnit;
    }

    public static boolean isPrimitiveWithSIUnitType(SymTypeExpression type) {
        return type instanceof SymTypeOfPrimitiveWithSIUnit;
    }

    /**
     * Helper method to extract primitiveTyüe of an SymTypeExpression
     */
    public static Optional<SymTypeExpression> getPrimitiveOfPrimitiveWithSIUnitType(SymTypeExpression type) {
        if (isPrimitiveWithSIUnitType(type)) {
            return Optional.ofNullable(((SymTypeOfPrimitiveWithSIUnit) type).getPrimitive());
        } else if (isPrimitive(type)) {
            return Optional.ofNullable((SymTypeConstant) type);
        }
        return Optional.empty();
    }

    /**
     * Helper method to extract siunit type of an SymTypeExpression
     */
    public static Optional<SymTypeExpression> getSIUnitOfPrimitiveWithSIUnitType(SymTypeExpression type) {
        if (isPrimitiveWithSIUnitType(type)) {
            return Optional.ofNullable(((SymTypeOfPrimitiveWithSIUnit) type).getSIUnit());
        } else if(isSIUnitType(type)) {
            return Optional.of(type);
        }
        return Optional.empty();
    }
}
