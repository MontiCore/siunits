/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingHandler;
import de.monticore.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingTraverser;

/**
 * Visitor for Derivation of SymType from SIUnitTypes4Computing
 * i.e. for
 * types/SIUnitTypes4Computing.mc4
 */
public class SynthesizeSymTypeFromCustomSIUnitTypes4Computing extends SynthesizeSymTypeFromSIUnitTypes4Computing
        implements CustomSIUnitTypes4ComputingHandler {

    protected CustomSIUnitTypes4ComputingTraverser traverser;

    @Override
    public CustomSIUnitTypes4ComputingTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(CustomSIUnitTypes4ComputingTraverser traverser) {
        this.traverser = traverser;
    }

    @Override
    public void traverse(ASTCustomSIUnitType4Computing node) {
        traverseSIUnitType4Computing(node);
    }
}
