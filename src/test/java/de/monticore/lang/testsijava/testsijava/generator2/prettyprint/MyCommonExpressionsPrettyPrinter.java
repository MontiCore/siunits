/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator2.prettyprint;

import de.monticore.expressions.commonexpressions._ast.ASTCallExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.UnitFactory;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.types.check.*;
import de.se_rwth.commons.logging.Log;

import javax.measure.unit.Unit;
import java.util.*;

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
            if (node.getArguments().getExpressionsList().size() != 1
                    || !(node.getArguments().getExpressions(0) instanceof ASTNameExpression)) {
                Log.error("0xE5672871 argument is not a Name");
            } else {
                argument = ((ASTNameExpression) node.getArguments().getExpressions(0)).getName();
            }

            Optional<FieldSymbol> fieldSymbol = ((IOOSymbolsScope) node.getEnclosingScope()).resolveField(argument);
            if (!fieldSymbol.isPresent()) {
                Log.error("0xE5672872 argument is invalid");
            }

            if (name.equals(print)) {
                String typePrint = "";
                if (fieldSymbol.get().getType() instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) fieldSymbol.get().getType()).getUnit();
                    typePrint = " + \"" + UnitPrettyPrinter.printUnit(un) + "\"";
                }
                this.getPrinter().print("System.out.println(" + fieldSymbol.get().getName()
                        + typePrint + ")");
            } else if (name.equals(value)) {
                this.getPrinter().print(fieldSymbol.get().getName());
            } else if (name.equals(basevalue)) {
                String factor = "";
                if (fieldSymbol.get().getType() instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) fieldSymbol.get().getType()).getUnit();
                    double d = UnitFactory.getConverterFrom(un).convert(1);
                    if (d != 1)
                        factor = "" + d + " * ";
                }
                this.getPrinter().print(factor + fieldSymbol.get().getName());
            }
        } else {
            if (node.getExpression() instanceof ASTNameExpression) {
                printer.print(((ASTNameExpression) node.getExpression()).getName());
            } else {
                node.getExpression().accept(getRealThis());
            }
            printer.print("(");
            NameToCallExpressionVisitor visitor = new NameToCallExpressionVisitor();
            node.accept(visitor);

            Collection<MethodSymbol> methodcollection = ((ITestSIJavaScope) node.getEnclosingScope())
                    .resolveMethodMany(node.getName());
            List<MethodSymbol> methodlist = new ArrayList<>(methodcollection);

            if (methodlist.size() != 1) {
                Log.error("0xE9332411 Overloading of methods is not supported");
            }
            MethodSymbol methodSymbol = methodlist.get(0);
            if (node.getArguments().getExpressionsList().size() != methodSymbol.getParameterList().size()) {
                Log.error("0xE9332412 Wrong number of arguments");
            }

            DeriveSymTypeOfTestSIJava der = new DeriveSymTypeOfTestSIJava();
            TypeCheck tc = new TypeCheck(null, der);

            for (int i = 0; i < node.getArguments().getExpressionsList().size(); i++) {
                ASTExpression givenParameter = node.getArguments().getExpressionsList().get(i);
                SymTypeExpression givenParameterType = tc.typeOf(givenParameter);
                FieldSymbol methodParameter = methodSymbol.getParameterList().get(i);
                SymTypeExpression methodParameterType = methodParameter.getType();

                String factor = "";

                if (methodParameterType instanceof SymTypeOfNumericWithSIUnit
                        && givenParameterType instanceof SymTypeOfNumericWithSIUnit) {
                    double convert = UnitFactory.getConverter(((SymTypeOfNumericWithSIUnit) givenParameterType).getUnit(),
                            ((SymTypeOfNumericWithSIUnit) methodParameterType).getUnit())
                            .convert(1);
                    if (convert != 1) {
                        factor = "" + convert + " * ";
                    }
                }

                if (i > 0)
                    printer.print(", ");
                printer.print(factor);

                if (givenParameter instanceof ASTNameExpression) {
                    printer.print(((ASTNameExpression) givenParameter).getName());
                } else {
                    givenParameter.accept(getRealThis());
                }
            }
            printer.print(")");
        }
        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
