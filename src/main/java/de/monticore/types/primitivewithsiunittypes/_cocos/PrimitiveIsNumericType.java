package de.monticore.types.primitivewithsiunittypes._cocos;

import de.monticore.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.se_rwth.commons.logging.Log;

public class PrimitiveIsNumericType implements PrimitiveWithSIUnitTypesASTPrimitiveWithSIUnitTypeCoCo {

    @Override
    public void check(ASTPrimitiveWithSIUnitType node) {
        if (node.getMCPrimitiveType().isBoolean())
            Log.error("0xA0499 Primitive type must be a numeric type");
    }
}
