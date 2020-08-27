/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.commonexpressions._ast.*;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.Converter;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.types.check.*;
import de.se_rwth.commons.logging.Log;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;
import java.util.*;

import static de.monticore.lang.testsijava.testsijava.generator.prettyprint.TestSIJavaPrettyPrinter.*;

public class MyCommonExpressionsPrettyPrinter extends CommonExpressionsPrettyPrinter {

    private Set<String> preDefined;
    private final String print = "print";
    private final String value = "value";
    private final String basevalue = "basevalue";

    TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJava(),
            new DeriveSymTypeOfTestSIJava());


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
            // callExpression is either print, value or basevalue

            if (node.getArguments().getExpressionList().size() != 1)
                Log.error("0xE5672871 " + node.get_SourcePositionStart() +
                        " invalid arguments number");

            String name = ((ASTNameExpression) node.getExpression()).getName();
            SymTypeExpression argumentType =
                    tc.typeOf(node.getArguments().getExpression(0));
            if (name.equals(print)) {
                String typePrint = "";
                if (argumentType instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) argumentType).getUnit();
                    typePrint = " + \"" + UnitPrettyPrinter.printUnit(un) + "\"";
                }
                this.getPrinter().print("System.out.println(");
                node.getArguments().getExpression(0).accept(getRealThis());
                this.getPrinter().print(typePrint + ")");
            } else if (name.equals(value)) {
                node.getArguments().getExpression(0).accept(getRealThis());
            } else if (name.equals(basevalue)) {
                UnitConverter converter = UnitConverter.IDENTITY;
                if (argumentType instanceof SymTypeOfNumericWithSIUnit) {
                    Unit un = ((SymTypeOfNumericWithSIUnit) argumentType).getUnit();
                    converter = Converter.getConverterFrom(un);
                }
                this.getPrinter().print(factorStart(converter));
                node.getArguments().getExpression(0).accept(getRealThis());
                this.getPrinter().print(factorEnd(converter));
            }
        } else {
            node.getExpression().accept(getRealThis());
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
            if (node.getArguments().getExpressionList().size() != methodSymbol.getParameterList().size()) {
                Log.error("0xE9332412 Wrong number of arguments");
            }

            DeriveSymTypeOfTestSIJava der = new DeriveSymTypeOfTestSIJava();
            TypeCheck tc = new TypeCheck(null, der);

            for (int i = 0; i < node.getArguments().getExpressionList().size(); i++) {
                ASTExpression givenParameter = node.getArguments().getExpression(i);
                SymTypeExpression givenParameterType = tc.typeOf(givenParameter);
                VariableSymbol methodParameter = methodSymbol.getParameterList().get(i);
                SymTypeExpression methodParameterType = methodParameter.getType();

                UnitConverter converter = UnitConverter.IDENTITY;

                if (methodParameterType instanceof SymTypeOfNumericWithSIUnit
                        && givenParameterType instanceof SymTypeOfNumericWithSIUnit) {
                    converter = Converter.getConverter(((SymTypeOfNumericWithSIUnit) givenParameterType).getUnit(),
                            ((SymTypeOfNumericWithSIUnit) methodParameterType).getUnit());
                }

                if (i > 0)
                    printer.print(", ");
                printer.print(factorStartSimple(converter));
                givenParameter.accept(getRealThis());
                printer.print(factorEndSimple(converter));
            }
            printer.print(")");
        }

        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }

    @Override
    public void handle(ASTPlusExpression node) {
        SymTypeExpression symType = tc.typeOf(node);
        if (symType instanceof SymTypeOfNumericWithSIUnit) {
            handlePlusMinusModulo(node, "+", (SymTypeOfNumericWithSIUnit) symType);
        } else
            super.handle(node);
    }

    @Override
    public void handle(ASTMinusExpression node) {
        SymTypeExpression symType = tc.typeOf(node);
        if (symType instanceof SymTypeOfNumericWithSIUnit) {
            handlePlusMinusModulo(node, "-", (SymTypeOfNumericWithSIUnit) symType);
        } else
            super.handle(node);
    }

    @Override
    public void handle(ASTModuloExpression node) {
        SymTypeExpression symType = tc.typeOf(node);
        if (symType instanceof SymTypeOfNumericWithSIUnit) {
            handlePlusMinusModulo(node, "%", (SymTypeOfNumericWithSIUnit) symType);
        } else
            super.handle(node);
    }

    private void handlePlusMinusModulo(ASTInfixExpression node, String operator, SymTypeOfNumericWithSIUnit symType) {
        CommentPrettyPrinter.printPreComments(node, this.getPrinter());

        UnitConverter leftConverter = UnitConverter.IDENTITY;
        UnitConverter rightConverter = UnitConverter.IDENTITY;
        Unit unit = symType.getUnit();
        Unit leftUnit = ((SymTypeOfNumericWithSIUnit) tc.typeOf(node.getLeft())).getUnit();
        Unit rightUnit = ((SymTypeOfNumericWithSIUnit) tc.typeOf(node.getRight())).getUnit();

        if (!leftUnit.equals(unit)) {
            leftConverter = Converter.getConverter(leftUnit, unit);
        }
        if (!rightUnit.equals(unit)) {
            rightConverter = Converter.getConverter(rightUnit, unit);
        }

        getPrinter().print(factorStart(leftConverter));
        node.getLeft().accept(this.getRealThis());
        getPrinter().print(factorEnd(leftConverter));
        this.getPrinter().print(" " + operator + " ");
        getPrinter().print(factorStart(rightConverter));
        node.getRight().accept(this.getRealThis());
        getPrinter().print(factorEnd(rightConverter));

        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
