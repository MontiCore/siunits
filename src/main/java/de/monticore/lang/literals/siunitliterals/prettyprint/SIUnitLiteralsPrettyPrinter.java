package de.monticore.lang.literals.siunitliterals.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._visitor.SIUnitLiteralsVisitor;
import de.monticore.lang.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class SIUnitLiteralsPrettyPrinter extends MCCommonLiteralsPrettyPrinter
                                        implements SIUnitLiteralsVisitor {

    private SIUnitLiteralsVisitor realThis = this;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitLiteralsPrettyPrinter(IndentPrinter printer) {
        super(printer);
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
        node.getNum().accept(getRealThis());
        printer.print(" ");
        node.getUn().accept(new SIUnitsPrettyPrinter(printer));
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral node) {
        node.getNum().accept(getRealThis());
        printer.print(" ");
        node.getUn().accept(new SIUnitsPrettyPrinter(printer));
    }

    /**
     * This method prettyprints a given SIUnitLiteral.
     *
     * @param node A SIUnitLiteral.
     * @return String representation.
     */
    public String prettyprint(ASTSIUnitLiteral node) {
        node.accept(getRealThis());
        return printer.getContent();
    }

    /**
     * This method prettyprints a given SignedSIUnitLiterals.
     *
     * @param node A SignedSIUnitLiterals.
     * @return String representation.
     */
    public String prettyprint(ASTSignedSIUnitLiteral node) {
        node.accept(getRealThis());
        return printer.getContent();
    }

    /**
     * @see SIUnitLiteralsVisitor#setRealThis(SIUnitLiteralsVisitor)
     */
    @Override
    public void setRealThis(SIUnitLiteralsVisitor realThis) {
        this.realThis = realThis;
    }

    /**
     * @see SIUnitLiteralsVisitor#getRealThis()
     */
    @Override
    public SIUnitLiteralsVisitor getRealThis() {
        return realThis;
    }
}
