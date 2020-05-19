/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.lang.siunits._visitor.SIUnitsVisitor;

public class DeriveSymTypeOfSIUnit implements SIUnitsVisitor {

    // ---------------------------------------------------------- Storage result

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    protected TypeCheckResult result;

    public TypeCheckResult getTypeCheckResult() {
        return result;
    }

    public void init() {
        result = new TypeCheckResult();
    }

}
