/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator1.prettyprint;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.UnitFactory;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;

import javax.measure.unit.Unit;
import java.util.LinkedList;
import java.util.List;

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
        printer.println("package " + String.join(".", node.getPackageList()) + ";");
        printer.println();
        printer.println("public class " + node.getName() + " {");
        printer.println();
        printer.indent();

        for (ASTSIJavaClassStatement statement : node.getSIJavaClassStatementsList()) {
            if (statement instanceof ASTFieldDeclaration) {
                statement.accept(getRealThis());
                printer.println(";");
            } else if (statement instanceof ASTMethodDeclaration) {
                printer.println();
                statement.accept(getRealThis());
            }
        }
    }

    @Override
    public void endVisit(ASTSIJavaClass node) {
        printer.unindent();
        printer.println("}");
    }

    @Override
    public void traverse(ASTFieldDeclaration node) {
//        String typePrint = printNumericType(node.getSymbol().getType());
//        printer.print(typePrint);
        printer.print("double");
        printer.print(" " + node.getName());

        if (node.isPresentExpression()) {
//            printer.print(" = (" + typePrint + ") (");
            printer.print(" = ");
            node.getExpression().accept(getRealThis());
//            printer.print(")");
        }
    }

    @Override
    public void traverse(ASTMethodDeclaration node) {
        printWrapper(node);

        printIntern(node);
    }

    private void printIntern(ASTMethodDeclaration node) {
//        String typePrint = printNumericType(node.getSymbol().getReturnType());
        String typePrint = "double";
        printer.print("public " + typePrint + " " + node.getName() + "_(");

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

        for (ASTSIJavaMethodStatement statement : node.getSIJavaMethodStatementsList()) {
            statement.accept(getRealThis());
            printer.println(";");
        }

        if (node.isPresentSIJavaMethodReturn()) {
            if (!node.getSIJavaMethodStatementsList().isEmpty())
                printer.println();
//            printer.print("return (" + typePrint + ") (");
            printer.print("return ");

            node.getSIJavaMethodReturn().accept(getRealThis());
//            printer.println(");");
            printer.println(";");
        }


        printer.unindent();
        printer.println("}");
    }

    private void printWrapper(ASTMethodDeclaration node) {
        SymTypeExpression returnType = node.getSymbol().getReturnType();
        String typePrint = returnType instanceof SymTypeOfNumericWithSIUnit ?
                ((SymTypeOfNumericWithSIUnit) returnType).getNumericType().print() :
                returnType.print();
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

        List<String> newParameters = new LinkedList<>();
        for (ASTSIJavaParameter parameter : node.getSIJavaParametersList()) {
            String newName = parameter.getName() + "_";
            newParameters.add(newName);
            SymTypeExpression parType = parameter.getSymbol().getType();
            String numericType = printNumericType(parType);
            String factor = "";

            if (parType instanceof SymTypeOfNumericWithSIUnit) {
                Unit parUnit = ((SymTypeOfNumericWithSIUnit) parType).getUnit();
                double d = UnitFactory.getConverter(parUnit, UnitFactory.createBaseUnit(parUnit)).convert(1);
                if (d != 1)
                    factor = " * " + d;
            }

            printer.print(numericType + " " + newName + " = (" + numericType + ") " + parameter.getName());
            printer.println(factor + ";");
        }

        if (!node.getSIJavaParametersList().isEmpty())
            printer.println();
        String factor = "";
        if (node.isPresentSIJavaMethodReturn()) {
            printer.print("return (" + typePrint + ") ");
            if (returnType instanceof SymTypeOfNumericWithSIUnit) {
                Unit returnUnit = ((SymTypeOfNumericWithSIUnit) returnType).getUnit();
                double d = UnitFactory.getConverter(UnitFactory.createBaseUnit(returnUnit), returnUnit).convert(1);
                if (d != 1)
                    factor = " * " + d + ")";
            }
        }

        if (!factor.equals(""))
            printer.print("(");
        printer.print(node.getName() + "_(");
        printer.print(String.join(", ", newParameters));
        printer.println(")" + factor + ";");

        printer.unindent();
        printer.println("}");

    }

    @Override
    public void traverse(ASTSIJavaParameter node) {
        printer.print(printNumericType(node.getSymbol().getType()) + " " + node.getName());
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
            return ((SymTypeOfNumericWithSIUnit) symTypeExpression).getNumericType().print();
        else
            return symTypeExpression.print();
    }
}
