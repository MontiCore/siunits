/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;
import de.monticore.lang.testsijava.testsijava.generator.prettyprint.*;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class PrintAsJavaClass {

    TestSIJavaTraverser traverser;

    public PrintAsJavaClass(IndentPrinter printer) {
        traverser = TestSIJavaMill.traverser();

        MyAssignmentExpressionsPrettyPrinter assignmentExpressionsPrettyPrinter = new MyAssignmentExpressionsPrettyPrinter(printer);
        traverser.addAssignmentExpressionsVisitor(assignmentExpressionsPrettyPrinter);
        traverser.setAssignmentExpressionsHandler(assignmentExpressionsPrettyPrinter);

        MyCommonExpressionsPrettyPrinter commonExpressionsPrettyPrinter = new MyCommonExpressionsPrettyPrinter(printer);
        traverser.addCommonExpressionsVisitor(commonExpressionsPrettyPrinter);
        traverser.setCommonExpressionsHandler(commonExpressionsPrettyPrinter);

        MyExpressionsBasisPrettyPrinter expressionsBasisPrettyPrinter = new MyExpressionsBasisPrettyPrinter(printer);
        traverser.addExpressionsBasisVisitor(expressionsBasisPrettyPrinter);
        traverser.setExpressionsBasisHandler(expressionsBasisPrettyPrinter);

        TestSIJavaPrettyPrinter testSIJavaPrettyPrinter = new TestSIJavaPrettyPrinter(printer);
        traverser.addTestSIJavaVisitor(testSIJavaPrettyPrinter);
        traverser.setTestSIJavaHandler(testSIJavaPrettyPrinter);

        MCBasicTypesPrettyPrinter mcBasicTypesPrettyPrinter = new MCBasicTypesPrettyPrinter(printer);
        traverser.addMCBasicTypesVisitor(mcBasicTypesPrettyPrinter);
        traverser.setMCBasicTypesHandler(mcBasicTypesPrettyPrinter);

        MySIUnitLiteralsPrettyPrinter siUnitLiteralsPrettyPrinter = new MySIUnitLiteralsPrettyPrinter(printer);
        traverser.setSIUnitLiteralsHandler(siUnitLiteralsPrettyPrinter);

        MCCommonLiteralsPrettyPrinter mcCommonLiteralsPrettyPrinter = new MCCommonLiteralsPrettyPrinter(printer);
        traverser.addMCCommonLiteralsVisitor(mcCommonLiteralsPrettyPrinter);
        traverser.setMCCommonLiteralsHandler(mcCommonLiteralsPrettyPrinter);
    }

    public static String printAsJavaClass(ASTSIJavaClass siclass) {
        IndentPrinter printer = new IndentPrinter();
        printer.setIndentLength(4);
        PrintAsJavaClass printAsJavaClass = new PrintAsJavaClass(printer);
        siclass.accept(printAsJavaClass.traverser);
        return printer.getContent();
    }

}
