package de.monticore.lang.siunits.prettyprint;

import de.monticore.lang.siunits._ast.*;
import de.monticore.lang.siunits._visitor.SIUnitsVisitor;
import de.monticore.prettyprint.IndentPrinter;

/**
 * Prettyprints a {@link de.monticore.lang.siunits._ast.ASTSIUnit} from the
 * de.monticore.lang.SIUnits grammar with brackets around each SIUnitExpression.
 * E.g. km/s*kg -> ((km/s)*kg).
 */
public class SIUnitWithBracketsPrettyPrinter implements SIUnitsVisitor {

    private SIUnitsVisitor realThis = this;

    // printer to use
    protected IndentPrinter printer;

    /**
     * Constructor
     * @param printer
     */
    public SIUnitWithBracketsPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    /**
     * @return the printer
     */
    public IndentPrinter getPrinter() {
        return printer;
    }

    /**
     * Prints a SIUnitMultExpression
     * @param node SIUnitMultExpression
     */
    @Override
    public void traverse(ASTSIUnitMultExpression node) {
        printer.print("(");
        node.getLeft().accept(getRealThis());
        printer.print("*");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a SIUnitDivExpression
     * @param node SIUnitDivExpression
     */
    @Override
    public void traverse(ASTSIUnitDivExpression node) {
        printer.print("(");
        node.getLeft().accept(getRealThis());
        printer.print("/");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a ASTSIUnitOneDivExpression
     * @param node ASTSIUnitOneDivExpression
     */
    @Override
    public void traverse(ASTSIUnitOneDivExpression node) {
        printer.print("(1/");
        node.getRight().accept(getRealThis());
        printer.print(")");
    }

    /**
     * Prints a SIUnitExponentExpression
     * @param node SIUnitExponentExpression
     */
    @Override
    public void visit(ASTSIUnitExponentExpression node) {
        printer.print("(");
    }

    /**
     * Prints a SIUnitExponentExpression
     * @param node SIUnitExponentExpression
     */
    @Override
    public void endVisit(ASTSIUnitExponentExpression node) {
        printer.print("^" + node.getExponent().getValue() + ")");
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
        SIUnitWithBracketsPrettyPrinter pp = new SIUnitWithBracketsPrettyPrinter(new IndentPrinter());
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
