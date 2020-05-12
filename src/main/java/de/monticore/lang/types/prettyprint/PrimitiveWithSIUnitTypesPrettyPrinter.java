package de.monticore.lang.types.prettyprint;

import de.monticore.lang.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitTypesNode;
import de.monticore.lang.types.primitivewithsiunittypes._visitor.PrimitiveWithSIUnitTypesVisitor;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.lang.types.siunittypes._visitor.SIUnitTypesVisitor;

public class PrimitiveWithSIUnitTypesPrettyPrinter extends MCBasicTypesPrettyPrinter
        implements PrimitiveWithSIUnitTypesVisitor {
    private PrimitiveWithSIUnitTypesVisitor realThis;

    /**
     * @see PrimitiveWithSIUnitTypesVisitor#setRealThis(PrimitiveWithSIUnitTypesVisitor)
     */
    @Override
    public void setRealThis(PrimitiveWithSIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    /**
     * @see SIUnitTypesVisitor#getRealThis()
     */
    @Override
    public PrimitiveWithSIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    public PrimitiveWithSIUnitTypesPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from PrimitiveWithSIUnitTypes grammar.
     *
     * @param node A node from PrimitiveWithSIUnitTypes grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTPrimitiveWithSIUnitTypesNode node) {
        PrimitiveWithSIUnitTypesPrettyPrinter pp = new PrimitiveWithSIUnitTypesPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }

    @Override
    public void traverse(ASTPrimitiveWithSIUnitType node) {
        printer.print("<");
        node.getMCPrimitiveType().accept(getRealThis());
        printer.print(" ");
        SIUnitsPrettyPrinter sipp = new SIUnitTypesPrettyPrinter(printer);
        node.getSIUnitType().getSIUnit().accept(sipp);
        printer.print(">");
    }
}
