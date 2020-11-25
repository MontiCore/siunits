/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunitliterals.prettyprint;

import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunitliterals.SIUnitLiteralsMill;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteralsNode;
import de.monticore.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.siunitliterals._visitor.SIUnitLiteralsHandler;
import de.monticore.siunitliterals._visitor.SIUnitLiteralsTraverser;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;

public class SIUnitLiteralsPrettyPrinter implements SIUnitLiteralsHandler {

    protected SIUnitLiteralsTraverser traverser;

    @Override
    public SIUnitLiteralsTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(SIUnitLiteralsTraverser traverser) {
        this.traverser = traverser;
    }

    // printer to use
    protected IndentPrinter printer;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitLiteralsPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    /**
     * @return the printer
     */
    public IndentPrinter getPrinter() {
        return printer;
    }


    /**
     * Prints a SIUnitLiteral
     * Cannot be visit or endVisit because the MCCommonLiteralsPrettyPrinter (superclass) would print "3 kg/s^2" as "32 kg/s^2"
     * @param node SIUnitLiteral
     */
    @Override
    public void traverse(ASTSIUnitLiteral node) {
        node.getNumericLiteral().accept(getTraverser());
        printer.print(" ");
        node.getSIUnit().accept(getTraverser());
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getSignedNumericLiteral().accept(getTraverser());
        printer.print(" ");
        node.getSIUnit().accept(getTraverser());
    }


    /**
     * This method prettyprints a given node from SIUnitLiterals grammar.
     *
     * @param node A node from SIUnitLiterals grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitLiteralsNode node) {
        SIUnitLiteralsTraverser traverser = SIUnitLiteralsMill.traverser();

        IndentPrinter printer = new IndentPrinter();
        SIUnitsPrettyPrinter siUnitsPrettyPrinter = new SIUnitsPrettyPrinter(printer);
        SIUnitLiteralsPrettyPrinter siUnitLiteralsPrettyPrinter = new SIUnitLiteralsPrettyPrinter(printer);
        MCCommonLiteralsPrettyPrinter mcCommonLiteralsPrettyPrinter = new MCCommonLiteralsPrettyPrinter(printer);

        traverser.setSIUnitsHandler(siUnitsPrettyPrinter);
        traverser.addSIUnitsVisitor(siUnitsPrettyPrinter);
        traverser.setSIUnitLiteralsHandler(siUnitLiteralsPrettyPrinter);
        traverser.addMCCommonLiteralsVisitor(mcCommonLiteralsPrettyPrinter);

        node.accept(traverser);
        return printer.getContent();
    }
}
