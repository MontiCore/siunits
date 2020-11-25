/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaHandler;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.Converter;
import de.monticore.types.check.DeriveSymTypeOfTestSIJava;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;
import de.monticore.types.check.TypeCheck;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

public class TestSIJavaPrettyPrinter implements TestSIJavaHandler, TestSIJavaVisitor2 {

    protected TestSIJavaTraverser traverser;

    @Override
    public TestSIJavaTraverser getTraverser() {
        return traverser;
    }

    @Override
    public void setTraverser(TestSIJavaTraverser traverser) {
        this.traverser = traverser;
    }

    IndentPrinter printer;

    public TestSIJavaPrettyPrinter(IndentPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void traverse(ASTSIJavaClass node) {
        printer.println(
                "package " + String.join(".", node.getPackageList()) + ";");
        printer.println();
        printer.println("public class " + node.getName() + " {");
        printer.println();
        printer.indent();
        boolean firstStatement = true;

        for (ASTSIJavaClassStatement statement : node.getSIJavaClassStatementList()) {
            if (statement instanceof ASTFieldDeclaration) {
                statement.accept(getTraverser());
                printer.println(";");
                firstStatement = false;
            } else if (statement instanceof ASTMethodDeclaration) {
                if (!firstStatement) {
                    printer.println();
                    printer.println();
                }
                statement.accept(getTraverser());
                firstStatement = false;
            }
        }

        printer.unindent();
        printer.println("}");
    }

    @Override
    public void traverse(ASTFieldDeclaration node) {
        String typePrint = printNumericType(node.getSymbol().getType());
        printer.print(typePrint);
        printer.print(" " + node.getName());

        if (node.isPresentExpression()) {
            UnitConverter converter = UnitConverter.IDENTITY;
            if (node.getSymbol().getType() instanceof SymTypeOfNumericWithSIUnit) {
                Unit unit = ((SymTypeOfNumericWithSIUnit) node.getSymbol().getType()).getUnit();
                TypeCheck tc = new TypeCheck(null, new DeriveSymTypeOfTestSIJava());
                SymTypeOfNumericWithSIUnit rightType = (SymTypeOfNumericWithSIUnit) tc.typeOf(node.getExpression());
                converter = Converter.getConverter(rightType.getUnit(), unit);
            }

            printer.print(" = (" + typePrint + ") (" + factorStartSimple(converter));
            node.getExpression().accept(getTraverser());
            printer.print(factorEndSimple(converter) + ")");
        }
    }

    @Override
    public void traverse(ASTMethodDeclaration node) {
        String typePrint = printNumericType(node.getSymbol().getReturnType());
        printer.print("public " + typePrint + " " + node.getName() + "(");

        boolean first = true;
        for (ASTSIJavaParameter parameter : node.getSIJavaParameterList()) {
            if (!first) {
                printer.print(", ");
            } else {
                first = false;
            }
            parameter.accept(getTraverser());
        }
        printer.println(") {");
        printer.indent();

        for (ASTSIJavaMethodStatement statement : node
                .getSIJavaMethodStatementList()) {
            statement.accept(getTraverser());
            printer.println(";");
        }

        if (node.isPresentSIJavaMethodReturn()) {
            if (!node.getSIJavaMethodStatementList().isEmpty())
                printer.println();

            UnitConverter converter = UnitConverter.IDENTITY;
            if (node.getSymbol().getReturnType() instanceof SymTypeOfNumericWithSIUnit) {
                Unit unit = ((SymTypeOfNumericWithSIUnit) node.getSymbol().getReturnType()).getUnit();
                TypeCheck tc = new TypeCheck(null, new DeriveSymTypeOfTestSIJava());
                SymTypeExpression rightType = tc.typeOf(node.getSIJavaMethodReturn().getExpression());
                if (rightType instanceof SymTypeOfNumericWithSIUnit)
                    converter = Converter.getConverter(((SymTypeOfNumericWithSIUnit) rightType).getUnit(), unit);
            }

            printer.print("return (" + typePrint + ") (" + factorStartSimple(converter));
            node.getSIJavaMethodReturn().accept(getTraverser());
            printer.println(factorEndSimple(converter) + ");");
        }

        printer.unindent();
        printer.println("}");
    }

    @Override
    public void traverse(ASTSIJavaParameter node) {
        printer.print(printNumericType(node.getSymbol().getType()) + " " +
                node.getName());
    }

    @Override
    public void endVisit(ASTSIJavaParameter node) {

    }

    @Override
    public void visit(ASTSIJavaMethodExpression node) {

    }

    @Override
    public void endVisit(ASTSIJavaMethodExpression node) {

    }

    @Override
    public void visit(ASTSIJavaMethodReturn node) {

    }

    @Override
    public void endVisit(ASTSIJavaMethodReturn node) {

    }

    @Override
    public void visit(ASTSIJavaClassStatement node) {

    }

    @Override
    public void endVisit(ASTSIJavaClassStatement node) {

    }

    @Override
    public void visit(ASTSIJavaMethodStatement node) {

    }

    @Override
    public void endVisit(ASTSIJavaMethodStatement node) {

    }

    private String printNumericType(SymTypeExpression symTypeExpression) {
        if (symTypeExpression instanceof SymTypeOfNumericWithSIUnit)
            return ((SymTypeOfNumericWithSIUnit) symTypeExpression)
                    .getNumericType().print();
        else return symTypeExpression.print();
    }

    public static String factorStart(UnitConverter converter) {
        if (converter != UnitConverter.IDENTITY && converter.convert(1) != 1.0)
            return "((";
        else return "";
    }

    public static String factorStartSimple(UnitConverter converter) {
        if (converter != UnitConverter.IDENTITY && converter.convert(1) != 1.0)
            return "(";
        else return "";
    }

    public static String factorEnd(UnitConverter converter) {
        if (converter != UnitConverter.IDENTITY && converter.convert(1) != 1.0) {
            String factor;
            if (converter.convert(1) > 1)
                factor = " * " + converter.convert(1);
            else
                factor = " / " + converter.inverse().convert(1);
            return ")" + factor + ")";
        } else
            return "";
    }

    public static String factorEndSimple(UnitConverter converter) {
        if (converter != UnitConverter.IDENTITY && converter.convert(1) != 1.0) {
            String factor;
            if (converter.convert(1) > 1)
                factor = " * " + converter.convert(1);
            else
                factor = " / " + converter.inverse().convert(1);
            return ")" + factor;
        } else
            return "";
    }
}
