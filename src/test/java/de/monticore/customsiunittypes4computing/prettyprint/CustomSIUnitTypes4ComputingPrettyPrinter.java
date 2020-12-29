/* (c) https://github.com/MontiCore/monticore */

package de.monticore.customsiunittypes4computing.prettyprint;

import de.monticore.customsiunittypes4computing.CustomSIUnitTypes4ComputingMill;
import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitTypes4ComputingNode;
import de.monticore.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingHandler;
import de.monticore.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingTraverser;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4computing.prettyprint.SIUnitTypes4ComputingPrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class CustomSIUnitTypes4ComputingPrettyPrinter
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

    private IndentPrinter printer;

    public CustomSIUnitTypes4ComputingPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void traverse(ASTCustomSIUnitType4Computing node) {
        node.getMCPrimitiveType().accept(getTraverser());
        printer.print(" in ");
        node.getSIUnit().accept(getTraverser());
    }

    /**
     * This method prettyprints a given node from SIUnitTypes4Computing grammar.
     *
     * @param node A node from SIUnitTypes4Computing grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTCustomSIUnitTypes4ComputingNode node) {
        CustomSIUnitTypes4ComputingTraverser traverser = CustomSIUnitTypes4ComputingMill.traverser();

        IndentPrinter printer = new IndentPrinter();
        CustomSIUnitTypes4ComputingPrettyPrinter customSIUnitTypes4ComputingPrettyPrinter = new CustomSIUnitTypes4ComputingPrettyPrinter(printer);
        SIUnitTypes4ComputingPrettyPrinter siUnitTypes4ComputingPrettyPrinter = new SIUnitTypes4ComputingPrettyPrinter(printer);
        SIUnitsPrettyPrinter siUnitsPrettyPrinter = new SIUnitsPrettyPrinter(printer);
        MCBasicTypesPrettyPrinter mCBasicTypesPrettyPrinter = new MCBasicTypesPrettyPrinter(printer);

        traverser.setCustomSIUnitTypes4ComputingHandler(customSIUnitTypes4ComputingPrettyPrinter);
        traverser.setSIUnitTypes4ComputingHandler(siUnitTypes4ComputingPrettyPrinter);
        traverser.setSIUnitsHandler(siUnitsPrettyPrinter);
        traverser.add4SIUnits(siUnitsPrettyPrinter);
        traverser.setMCBasicTypesHandler(mCBasicTypesPrettyPrinter);

        node.accept(traverser);
        return printer.getContent();
    }
}
