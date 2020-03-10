package de.monticore.lang.siunits.siunits.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.lang.siunits.siunits._ast.*;
import de.monticore.lang.siunits.siunits._visitor.SIUnitsVisitor;

public class SIUnitPrettyPrinter implements SIUnitsVisitor {

    private SIUnitsVisitor realThis = this;

    // printer to use
    protected IndentPrinter printer;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    /**
     * @return the printer
     */
    public IndentPrinter getPrinter() {
        return this.printer;
    }

    /**
     * Prints a SIUnitMultExpression
     * @param node SIUnitMultExpression
     */
    @Override
    public void traverse(ASTSIUnitMultExpression node) {
        node.getLeft().accept(getRealThis());
        printer.print("*");
        node.getRight().accept(getRealThis());
    }

    /**
     * Prints a SIUnitDivExpression
     * @param node SIUnitDivExpression
     */
    @Override
    public void traverse(ASTSIUnitDivExpression node) {
        node.getLeft().accept(getRealThis());
        printer.print("/");
        node.getRight().accept(getRealThis());
    }

//    /**
//     * Prints a SIUnitOneDivExpression
//     * @param node SIUnitDivExpression
//     */
//    @Override
//    public void traverse(ASTSIUnitOneDivExpression node) {
//        node.getLeft().accept(getRealThis());
//        printer.print("/");
//        node.getRight().accept(getRealThis());
//    }
//
//    @Override
//    public void endVisit(ASTSIOne node) {
//        printer.print("1");
//    }

    /**
     * Prints a SIUnitExponentExpression
     * @param node SIUnitExponentExpression
     */
    @Override
    public void endVisit(ASTSIUnitExponentExpression node) {
        printer.print("^" + node.getExponent().getValue());
    }

    @Override
    public void visit(ASTSIUnitBracketExpression node) {
        printer.print("(");
    }

    @Override
    public void endVisit(ASTSIUnitBracketExpression node) {
        printer.print(")");
    }

    /**
     * Prints a UnitBaseDimWithPrefix
     * @param node UnitBaseDimWithPrefix
     */
    @Override
    public void visit(ASTUnitBaseDimWithPrefix node) {
        printer.print(node.getUnit());
    }

    /**
     * Prints a OfficallyAcceptedUnit
     * @param node OfficallyAcceptedUnit
     */
    @Override
    public void visit(ASTOfficallyAcceptedUnit node) {
        printer.print(node.getUnit());
    }

    /**
     * Prints a SiUnitDimensionless
     * @param node SiUnitDimensionless
     */
    @Override
    public void visit(ASTSiUnitDimensionless node) {
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
        SIUnitPrettyPrinter pp = new SIUnitPrettyPrinter(new IndentPrinter());
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
