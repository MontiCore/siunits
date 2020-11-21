/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.SIUnitsMill;
import de.monticore.siunits._ast.*;
import de.monticore.siunits._visitor.SIUnitsHandler;
import de.monticore.siunits._visitor.SIUnitsTraverser;
import de.monticore.siunits._visitor.SIUnitsVisitor2;

public class SIUnitsPrettyPrinter implements SIUnitsVisitor2, SIUnitsHandler {

    // printer to use
    protected IndentPrinter printer;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitsPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    /**
     * @return the printer
     */
    public IndentPrinter getPrinter() {
        return printer;
    }


    @Override
    public void traverse(ASTSIUnit node) {
        if (node.isPresentOne())
            printer.print("1");
        else if (node.isPresentNumerator())
            node.getNumerator().accept(getTraverser());
        else if (node.isPresentSIUnitPrimitive())
            node.getSIUnitPrimitive().accept(getTraverser());
        if (node.isPresentDenominator()) {
            printer.print("/");
            node.getDenominator().accept(getTraverser());
        }
    }

    @Override
    public void visit(ASTSIUnitWithPrefix node) {
        if (node.isPresentName())
            printer.print(node.getName());
        else if (node.isPresentNonNameUnit())
            printer.print(node.getNonNameUnit());
    }


    @Override
    public void visit(ASTSIUnitWithoutPrefix node) {
        if (node.isPresentName())
            printer.print(node.getName());
        else if (node.isPresentNonNameUnit())
            printer.print(node.getNonNameUnit());
    }

    @Override
    public void traverse(ASTSIUnitKindGroupWithExponent node) {
        int j = 0;
        for (int i = 0; i < node.getSIUnitGroupPrimitiveList().size(); i++) {
            node.getSIUnitGroupPrimitive(i).accept(getTraverser());
            if (j < node.getExponentList().size())
                printer.print("^" + node.getExponent(j++).getSource());
        }
    }
    /**
     * Prints a SiUnitDimensionless
     * @param node SiUnitDimensionless
     */
    @Override
    public void visit(ASTSIUnitDimensionless node) {
        if (node.isPresentUnit())
            printer.print(node.getUnit());
        else
            printer.print("°");
    }

    /**
     * Prints a Celsius or Fahrenheit Degree
     * @param node CelciusFahrenheit
     */
    @Override
    public void visit(ASTCelsiusFahrenheit node) {
        printer.print("°" + node.getUnit());
    }


    /**
     * This method prettyprints a given node from SIUnit grammar.
     *
     * @param node A node from SIUnit grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitsNode node) {
        SIUnitsTraverser traverser = SIUnitsMill.traverser();

        SIUnitsPrettyPrinter siUnitsPrettyPrinter = new SIUnitsPrettyPrinter(new IndentPrinter());

        traverser.setSIUnitsHandler(siUnitsPrettyPrinter);
        traverser.setSIUnitsVisitor(siUnitsPrettyPrinter);

        node.accept(traverser);
        return siUnitsPrettyPrinter.getPrinter().getContent();
    }
}
