/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.commonexpressions._ast.ASTCallExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.UnitFactory;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.se_rwth.commons.logging.Log;

import javax.measure.unit.Unit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MyCommonExpressionsPrettyPrinter extends CommonExpressionsPrettyPrinter {

    private Set<String> preDefined;
    private final String print = "print";
    private final String value = "value";
    private final String basevalue = "basevalue";


    public MyCommonExpressionsPrettyPrinter(IndentPrinter printer) {
        super(printer);
        this.preDefined = new HashSet<>();
        this.preDefined.add(print);
        this.preDefined.add(value);
        this.preDefined.add(basevalue);
    }

    @Override
    public void handle(ASTCallExpression node) {
        CommentPrettyPrinter.printPreComments(node, this.getPrinter());
        if (node.getExpression() instanceof ASTNameExpression
                && preDefined.contains(((ASTNameExpression) node.getExpression()).getName())) {
            String name = ((ASTNameExpression) node.getExpression()).getName();
            String argument = "";
            if (node.getArguments().getExpressionList().size() != 1
                    || !(node.getArguments().getExpression(0) instanceof ASTNameExpression)) {
                Log.error("0xE5672871 argument is not a Name");
            } else {
                argument = ((ASTNameExpression) node.getArguments().getExpression(0)).getName();
            }

            Optional<FieldSymbol> fieldSymbol = ((ITypeSymbolsScope) node.getEnclosingScope()).resolveField(argument);
            if (!fieldSymbol.isPresent()) {
                Log.error("0xE5672872 argument is invalid");
            }

            if (name.equals(print)) {
                String factor = "";
                String typePrint = "";
                if (fieldSymbol.get().getType() instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) fieldSymbol.get().getType()).getUnit();
                    double d = UnitFactory.getConverter(UnitFactory.createBaseUnit(un), un).convert(1);
                    if (d != 1)
                        factor = "" + d + " * ";
                    typePrint = " + \" " + UnitPrettyPrinter.printUnit(un) + "\"";
                }
                this.getPrinter().print("System.out.println(\"\" + " + factor + fieldSymbol.get().getName()
                + typePrint + ")");
            } else if (name.equals(value)) {
                String factor = "";
                if (fieldSymbol.get().getType() instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) fieldSymbol.get().getType()).getUnit();
                    double d = UnitFactory.getConverter(UnitFactory.createBaseUnit(un), un).convert(1);
                    if (d != 1)
                        factor = "" + d + " * ";
                }
                this.getPrinter().print(factor + fieldSymbol.get().getName());
            } else if (name.equals(basevalue)) {
                this.getPrinter().print(fieldSymbol.get().getName());
            }
        } else {
            node.getExpression().accept(getRealThis());
            this.getPrinter().print("_");
            this.handle((ASTArguments) node.getArguments());
        }
        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
