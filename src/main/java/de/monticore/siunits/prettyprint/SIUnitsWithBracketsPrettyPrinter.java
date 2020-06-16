/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.prettyprint;

import de.monticore.siunits._ast.*;
import de.monticore.siunits._visitor.SIUnitsVisitor;
import de.monticore.prettyprint.IndentPrinter;

/**
 * Prettyprints a {@link de.monticore.siunits._ast.ASTSIUnit} from the
 * de.monticore.SIUnits grammar with brackets around each SIUnit.
 * E.g. km/s*kg -> ((km/s)*kg).
 */
public class SIUnitsWithBracketsPrettyPrinter implements SIUnitsVisitor {

    private SIUnitsVisitor realThis = this;

    // printer to use
    protected IndentPrinter printer;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitsWithBracketsPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    /**
     * @return the printer
     */
    public IndentPrinter getPrinter() {
        return printer;
    }

    /**
     * Prints a SIUnitMult
     * @param node SIUnitMult
     */
    @Override
    public void traverse(ASTSIUnitMult node) {
        printer.print("(");
        node.getLeft().accept(getRealThis());
        printer.print("*");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a SIUnitDiv
     * @param node SIUnitDiv
     */
    @Override
    public void traverse(ASTSIUnitDiv node) {
        printer.print("(");
        node.getLeft().accept(getRealThis());
        printer.print("/");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a ASTSIUnitOneDiv
     * @param node ASTSIUnitOneDiv
     */
    @Override
    public void traverse(ASTSIUnitOneDiv node) {
        printer.print("(1/");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a SIUnitExponent
     * @param node SIUnitExponent
     */
    @Override
    public void visit(ASTSIUnitExponent node) {
        printer.print("(");
    }

    /**
     * Prints a SIUnitExponent
     * @param node SIUnitExponent
     */
    @Override
    public void endVisit(ASTSIUnitExponent node) {
        printer.print("^" + node.getExponent().getValue() + ")");
    }

    @Override
    public void visit(ASTSIUnitBracket node) {
        printer.print("(");
    }

    @Override
    public void endVisit(ASTSIUnitBracket node) {
        printer.print(")");
    }

    @Override
    public void visit(ASTSIUnitWithPrefix node) {
        if (node.isPresentName())
            printer.print(node.getName());
        else
            printer.print(node.getNonNameUnit());
    }

    @Override
    public void visit(ASTOfficallyAcceptedUnit node) {
        printer.print(node.getUnit());
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
        if (node.isPresentName())
            printer.print("°" + node.getName());
        else
            printer.print("°" + node.getNonNameUnit());
//        printer.print("°" + node.getUnit());
    }


    /**
     * This method prettyprints a given node from SIUnit grammar.
     *
     * @param node A node from SIUnit grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitsNode node) {
        SIUnitsWithBracketsPrettyPrinter pp = new SIUnitsWithBracketsPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }

    /**
     * @see SIUnitsVisitor#setRealThis(SIUnitsVisitor)
     */
    @Override
    public void setRealThis(SIUnitsVisitor realThis) {
        this.realThis = realThis;
    }

    /**
     * @see SIUnitsVisitor#getRealThis()
     */
    @Override
    public SIUnitsVisitor getRealThis() {
        return realThis;
    }
}
