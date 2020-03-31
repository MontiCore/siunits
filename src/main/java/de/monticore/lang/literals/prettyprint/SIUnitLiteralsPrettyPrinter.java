package de.monticore.lang.literals.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteralsNode;
import de.monticore.lang.literals.siunitliterals._ast.ASTSignedSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._visitor.SIUnitLiteralsVisitor;
import de.monticore.lang.siunits.siunits.prettyprint.SIUnitPrettyPrinter;
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
        return this.printer;
    }


    /**
     * Prints a SIUnitLiteral
     * Cannot be visit or endVisit because the MCCommonLiteralsPrettyPrinter would print "3 kg/s^2" as "32 kg/s^2"
     * @param a SIUnitLiteral
     */
    @Override
    public void traverse(ASTSIUnitLiteral a) {
        a.getNum().accept(getRealThis());
        printer.print(" ");
        a.getUn().accept(new SIUnitPrettyPrinter(printer));
    }

    @Override
    public void traverse(ASTSignedSIUnitLiteral a) {
        a.getNum().accept(getRealThis());
        printer.print(" ");
        a.getUn().accept(new SIUnitPrettyPrinter(printer));
    }

    /**
     * This method prettyprints a given node from SIUnitLiterals grammar.
     *
     * @param a A node from SIUnitLiterals grammar.
     * @return String representation.
     */
    public String prettyprint(ASTSIUnitLiteralsNode a) {
        a.accept(getRealThis());
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
