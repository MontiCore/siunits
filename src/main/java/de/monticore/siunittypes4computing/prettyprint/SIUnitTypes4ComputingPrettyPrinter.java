/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4computing.prettyprint;

import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4computing._ast.ASTSIUnitTypes4ComputingNode;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingVisitor;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class SIUnitTypes4ComputingPrettyPrinter extends MCBasicTypesPrettyPrinter
        implements SIUnitTypes4ComputingVisitor {

    private SIUnitTypes4ComputingVisitor realThis;

    @Override
    public void setRealThis(SIUnitTypes4ComputingVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitTypes4ComputingVisitor getRealThis() {
        return realThis;
    }

    public SIUnitTypes4ComputingPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from SIUnitTypes4Computing grammar.
     *
     * @param node A node from SIUnitTypes4Computing grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitTypes4ComputingNode node) {
        SIUnitTypes4ComputingPrettyPrinter pp = new SIUnitTypes4ComputingPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }

    @Override
    public void traverse(ASTSIUnitType4Computing node) {
        SIUnitsPrettyPrinter sipp = new SIUnitsPrettyPrinter(printer);
        node.getSIUnitType4Math().getSIUnit().accept(sipp);
        printer.print("<");
        node.getMCPrimitiveType().accept(getRealThis());
        printer.print(">");
    }
}
