package de.monticore.lang.types.prettyprint;

import de.monticore.lang.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitTypesNode;
import de.monticore.lang.types.siunittypes._visitor.SIUnitTypesVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SIUnitTypesPrettyPrinter extends SIUnitsPrettyPrinter implements SIUnitTypesVisitor {

    private SIUnitTypesVisitor realThis;

    /**
     * @see SIUnitTypesVisitor#setRealThis(SIUnitTypesVisitor)
     */
    @Override
    public void setRealThis(SIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    /**
     * @see SIUnitTypesVisitor#getRealThis()
     */
    @Override
    public SIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    public SIUnitTypesPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from SIUnitTypes grammar.
     *
     * @param node A node from SIUnitTypes grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitTypesNode node) {
        SIUnitTypesPrettyPrinter pp = new SIUnitTypesPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }
}
