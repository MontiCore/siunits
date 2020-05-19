package de.monticore.lang.types.primitivewithsiunittypes._cocos;

import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.se_rwth.commons.logging.Log;

/**
 * Checks whether the PrimitiveType is a numeric type.
 */
public class PrimitiveIsNumericType implements PrimitiveWithSIUnitTypesASTPrimitiveWithSIUnitTypeCoCo {

    @Override
    public void check(ASTPrimitiveWithSIUnitType node) {
        if (node.getPrimitiveType().isBoolean())
            Log.error("0xA0499 Primitive type must be a numeric type");
    }
}
