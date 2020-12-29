/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4computing.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4computing.SIUnitTypes4ComputingMill;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4computing._ast.ASTSIUnitTypes4ComputingNode;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingHandler;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingTraverser;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class SIUnitTypes4ComputingPrettyPrinter
        implements SIUnitTypes4ComputingHandler {

    protected SIUnitTypes4ComputingTraverser traverser;

    @Override
    public SIUnitTypes4ComputingTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(SIUnitTypes4ComputingTraverser traverser) {
        this.traverser = traverser;
    }

    private IndentPrinter printer;

    public SIUnitTypes4ComputingPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void traverse(ASTSIUnitType4Computing node) {
        node.getSIUnit().accept(getTraverser());
        printer.print("<");
        node.getMCPrimitiveType().accept(getTraverser());
        printer.print(">");
    }

    /**
     * This method prettyprints a given node from SIUnitTypes4Computing grammar.
     *
     * @param node A node from SIUnitTypes4Computing grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitTypes4ComputingNode node) {
        SIUnitTypes4ComputingTraverser traverser = SIUnitTypes4ComputingMill.traverser();

        IndentPrinter printer = new IndentPrinter();
        SIUnitTypes4ComputingPrettyPrinter siUnitTypes4ComputingPrettyPrinter = new SIUnitTypes4ComputingPrettyPrinter(printer);
        SIUnitsPrettyPrinter siUnitsPrettyPrinter = new SIUnitsPrettyPrinter(printer);
        MCBasicTypesPrettyPrinter mCBasicTypesPrettyPrinter = new MCBasicTypesPrettyPrinter(printer);

        traverser.setSIUnitTypes4ComputingHandler(siUnitTypes4ComputingPrettyPrinter);
        traverser.setSIUnitsHandler(siUnitsPrettyPrinter);
        traverser.add4SIUnits(siUnitsPrettyPrinter);
        traverser.setMCBasicTypesHandler(mCBasicTypesPrettyPrinter);

        node.accept(traverser);
        return printer.getContent();
    }
}
