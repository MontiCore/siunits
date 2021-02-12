/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4math.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4math.SIUnitTypes4MathMill;
import de.monticore.siunittypes4math._ast.ASTSIUnitTypes4MathNode;
import de.monticore.siunittypes4math._visitor.SIUnitTypes4MathTraverser;

public class SIUnitTypes4MathPrettyPrinter {
    /**
     * This method prettyprints a given node from SIUnitTypes grammar.
     *
     * @param node A node from SIUnitTypes grammar.
     * @return String representation.
     */
    public static String prettyprint(ASTSIUnitTypes4MathNode node) {
        SIUnitTypes4MathTraverser traverser = SIUnitTypes4MathMill.traverser();

        SIUnitsPrettyPrinter siUnitsPrettyPrinter = new SIUnitsPrettyPrinter(new IndentPrinter());

        traverser.setSIUnitsHandler(siUnitsPrettyPrinter);
        traverser.add4SIUnits(siUnitsPrettyPrinter);

        node.accept(traverser);
        return siUnitsPrettyPrinter.getPrinter().getContent();
    }
}
