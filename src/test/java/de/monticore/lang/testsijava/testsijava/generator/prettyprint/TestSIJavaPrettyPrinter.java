/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunitliterals.utility.Converter;
import de.monticore.types.check.DeriveSymTypeOfTestSIJava;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;
import de.monticore.types.check.TypeCheck;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

public class TestSIJavaPrettyPrinter implements TestSIJavaVisitor {

    TestSIJavaVisitor realThis;
    IndentPrinter printer;

    public TestSIJavaPrettyPrinter(IndentPrinter printer) {
        realThis = this;
        this.printer = printer;
    }

    @Override
    public TestSIJavaVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(TestSIJavaVisitor realThis) {
        this.realThis = realThis;
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

        for (ASTSIJavaClassStatement statement : node.getSIJavaClassStatementsList()) {
            if (statement instanceof ASTFieldDeclaration) {
                statement.accept(getRealThis());
                printer.println(";");
                firstStatement = false;
            } else if (statement instanceof ASTMethodDeclaration) {
                if (!firstStatement) {
                    printer.println();
                    printer.println();
                }
                statement.accept(getRealThis());
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
            node.getExpression().accept(getRealThis());
            printer.print(factorEndSimple(converter) + ")");
        }
    }

    @Override
    public void traverse(ASTMethodDeclaration node) {
        String typePrint = printNumericType(node.getSymbol().getReturnType());
        printer.print("public " + typePrint + " " + node.getName() + "(");

        boolean first = true;
        for (ASTSIJavaParameter parameter : node.getSIJavaParametersList()) {
            if (!first) {
                printer.print(", ");
            } else {
                first = false;
            }
            parameter.accept(getRealThis());
        }
        printer.println(") {");
        printer.indent();

        for (ASTSIJavaMethodStatement statement : node
                .getSIJavaMethodStatementsList()) {
            statement.accept(getRealThis());
            printer.println(";");
        }

        if (node.isPresentSIJavaMethodReturn()) {
            if (!node.getSIJavaMethodStatementsList().isEmpty())
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
            node.getSIJavaMethodReturn().accept(getRealThis());
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
