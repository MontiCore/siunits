package de.monticore.lang.types.prettyprint;

import de.monticore.lang.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.lang.types.customprimitivewithsiunittypes._ast.ASTCustomPrimitiveWithSIUnitType;
import de.monticore.lang.types.customprimitivewithsiunittypes._ast.ASTCustomPrimitiveWithSIUnitTypesNode;
import de.monticore.lang.types.customprimitivewithsiunittypes._visitor.CustomPrimitiveWithSIUnitTypesVisitor;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.lang.types.siunittypes._visitor.SIUnitTypesVisitor;

public class CustomPrimitiveWithSIUnitTypesPrettyPrinter extends MCBasicTypesPrettyPrinter
        implements CustomPrimitiveWithSIUnitTypesVisitor {
    private CustomPrimitiveWithSIUnitTypesVisitor realThis;

    /**
     * @see CustomPrimitiveWithSIUnitTypesVisitor#setRealThis(CustomPrimitiveWithSIUnitTypesVisitor)
     */
    @Override
    public void setRealThis(CustomPrimitiveWithSIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    /**
     * @see SIUnitTypesVisitor#getRealThis()
     */
    @Override
    public CustomPrimitiveWithSIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    public CustomPrimitiveWithSIUnitTypesPrettyPrinter(IndentPrinter printer) {
        super(printer);
        realThis = this;
    }

    /**
     * This method prettyprints a given node from PrimitiveWithSIUnitTypes grammar.
     *
     * @param node A node from PrimitiveWithSIUnitTypes grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTCustomPrimitiveWithSIUnitTypesNode node) {
        CustomPrimitiveWithSIUnitTypesPrettyPrinter pp = new CustomPrimitiveWithSIUnitTypesPrettyPrinter(new IndentPrinter());
        node.accept(pp);
        return pp.getPrinter().getContent();
    }

    @Override
    public void traverse(ASTCustomPrimitiveWithSIUnitType node) {
        node.getMCPrimitiveType().accept(getRealThis());
        printer.print(" in ");
        SIUnitsPrettyPrinter sipp = new SIUnitTypesPrettyPrinter(printer);
        node.getSIUnitType().getSIUnit().accept(sipp);
    }
}
