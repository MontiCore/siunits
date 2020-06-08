/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.siunits.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingVisitor;

/**
 * Visitor for Derivation of SymType from SIUnitTypes4Computing
 * i.e. for
 * types/SIUnitTypes4Computing.mc4
 */
public class SynthesizeSymTypeFromCustomSIUnitTypes4Computing extends SynthesizeSymTypeFromSIUnitTypes4Computing
        implements CustomSIUnitTypes4ComputingVisitor {

    CustomSIUnitTypes4ComputingVisitor realThis = this;

    @Override
    public void setRealThis(CustomSIUnitTypes4ComputingVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public CustomSIUnitTypes4ComputingVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void traverse(ASTCustomSIUnitType4Computing node) {
        traverseSIUnitType4Computing(node);
    }
}
