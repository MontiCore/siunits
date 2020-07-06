/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;

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

        for (ASTSIJavaClassStatement statement : node.getSIJavaClassStatementList()) {
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
        String typePrint = node.getSymbol().getType().print();
        if (node.getSymbol().getType() instanceof SymTypeOfNumericWithSIUnit) {
            SymTypeOfNumericWithSIUnit type = (SymTypeOfNumericWithSIUnit) node.getSymbol().getType();
            typePrint = type.getNumericType().print();
        }
        printer.print(typePrint);

        printer.print(" " + node.getName());

        if (node.isPresentExpression()) {
            printer.print(" = (" + typePrint + ") (");
            node.getExpression().accept(getRealThis());
            printer.print(")");
        }
    }

    @Override
    public void traverse(ASTMethodDeclaration node) {
        SymTypeExpression returnType = node.getSymbol().getReturnType();
        String typePrint = returnType instanceof SymTypeOfNumericWithSIUnit ?
                ((SymTypeOfNumericWithSIUnit) returnType).getNumericType().print() :
                returnType.print();
        printer.print("public " + typePrint + " " + node.getName() + "(");

        boolean first = true;
        for (ASTSIJavaParameter parameter : node.getSIJavaParameterList()) {
            if (!first) {
                printer.print(", ");
            } else {
                first = false;
            }
            parameter.accept(getRealThis());
        }
        printer.println(") {");
        printer.indent();

        for (ASTSIJavaMethodStatement statement : node.getSIJavaMethodStatementList()) {
            statement.accept(getRealThis());
            printer.println(";");
        }

        if (node.isPresentSIJavaMethodReturn()) {
            printer.println();
            printer.print("return ");

            typePrint = returnType.print();
            if (returnType instanceof SymTypeOfNumericWithSIUnit)
                typePrint = ((SymTypeOfNumericWithSIUnit) returnType).getNumericType().print();

            printer.print("(" + typePrint + ") (");

//            if (returnType instanceof SymTypeOfNumericWithSIUnit) {
//                SymTypeOfNumericWithSIUnit siUnitReturnType = (SymTypeOfNumericWithSIUnit) returnType;
//                double factor = UnitFactory.getConverter(
//                        UnitFactory.createBaseUnit(
//                                siUnitReturnType.getUnit()),
//                        siUnitReturnType.getUnit()).convert(1);
//
//                if (factor != 1) {
//                    printer.print("(" + factor + " * ");
//                    node.getSIJavaMethodReturn().accept(getRealThis());
//                    printer.print(")");
//                } else {
//                    node.getSIJavaMethodReturn().accept(getRealThis());
//                }
//            } else
            node.getSIJavaMethodReturn().accept(getRealThis());
            printer.println(");");
        }


        printer.unindent();
        printer.println("}");
    }

    @Override
    public void traverse(ASTSIJavaParameter node) {
        SymTypeExpression type = node.getSymbol().getType();
        if (type instanceof SymTypeOfNumericWithSIUnit) {
            printer.print(((SymTypeOfNumericWithSIUnit) type).getNumericType().print());
        } else {
            printer.print(type.print());
        }
        printer.print(" " + node.getName());
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
}
