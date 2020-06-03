package de.monticore.lang.types.prettyprint;

import de.monticore.lang.types.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.lang.types.customsiunittypes4computing._ast.ASTCustomSIUnitTypes4ComputingNode;
import de.monticore.lang.types.customsiunittypes4computing._visitor.CustomSIUnitTypes4ComputingVisitor;
import de.monticore.lang.types.siunittypes4computing.prettyprint.SIUnitTypes4MathPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class CustomSIUnitTypes4ComputingPrettyPrinter extends MCBasicTypesPrettyPrinter
        implements CustomSIUnitTypes4ComputingVisitor {
    private CustomSIUnitTypes4ComputingVisitor realThis;

    @Override
    public void setRealThis(CustomSIUnitTypes4ComputingVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public CustomSIUnitTypes4ComputingVisitor getRealThis() {
        return realThis;
    }

    public CustomSIUnitTypes4ComputingPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from SIUnitTypes4Computing grammar.
     *
     * @param node A node from SIUnitTypes4Computing grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTCustomSIUnitTypes4ComputingNode node) {
        CustomSIUnitTypes4ComputingPrettyPrinter pp = new CustomSIUnitTypes4ComputingPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }

    @Override
    public void traverse(ASTCustomSIUnitType4Computing node) {
        node.getMCPrimitiveType().accept(getRealThis());
        printer.print(" <");
        SIUnitTypes4MathPrettyPrinter sipp = new SIUnitTypes4MathPrettyPrinter(printer);
        node.getSIUnitType4Math().accept(sipp);
        printer.print(">");
    }
}
