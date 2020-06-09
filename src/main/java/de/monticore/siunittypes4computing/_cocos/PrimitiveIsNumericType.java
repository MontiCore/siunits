/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4computing._cocos;

import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4ComputingInt;
import de.se_rwth.commons.logging.Log;

/**
 * Checks whether the PrimitiveType is a numeric type.
 */
public class PrimitiveIsNumericType implements SIUnitTypes4ComputingASTSIUnitType4ComputingIntCoCo {

    @Override
    public void check(ASTSIUnitType4ComputingInt node) {
        if (node.getMCPrimitiveType().isBoolean())
            Log.error("0xA0499 Primitive type must be a numeric type");
    }
}
