/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4math.prettyprint;

import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4math._ast.ASTSIUnitTypes4MathNode;
import de.monticore.siunittypes4math._visitor.SIUnitTypes4MathVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SIUnitTypes4MathPrettyPrinter extends SIUnitsPrettyPrinter implements SIUnitTypes4MathVisitor {

    private SIUnitTypes4MathVisitor realThis;

 @Override
    public void setRealThis(SIUnitTypes4MathVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitTypes4MathVisitor getRealThis() {
        return realThis;
    }

    public SIUnitTypes4MathPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from SIUnitTypes grammar.
     *
     * @param node A node from SIUnitTypes grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitTypes4MathNode node) {
        SIUnitTypes4MathPrettyPrinter pp = new SIUnitTypes4MathPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }
}
