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
        traverser.add4AssignmentExpressions(assignmentExpressionsPrettyPrinter);
        traverser.setAssignmentExpressionsHandler(assignmentExpressionsPrettyPrinter);

        MyCommonExpressionsPrettyPrinter commonExpressionsPrettyPrinter = new MyCommonExpressionsPrettyPrinter(printer);
        traverser.add4CommonExpressions(commonExpressionsPrettyPrinter);
        traverser.setCommonExpressionsHandler(commonExpressionsPrettyPrinter);

        MyExpressionsBasisPrettyPrinter expressionsBasisPrettyPrinter = new MyExpressionsBasisPrettyPrinter(printer);
        traverser.add4ExpressionsBasis(expressionsBasisPrettyPrinter);
        traverser.setExpressionsBasisHandler(expressionsBasisPrettyPrinter);

        TestSIJavaPrettyPrinter testSIJavaPrettyPrinter = new TestSIJavaPrettyPrinter(printer);
        traverser.add4TestSIJava(testSIJavaPrettyPrinter);
        traverser.setTestSIJavaHandler(testSIJavaPrettyPrinter);

        MCBasicTypesPrettyPrinter mcBasicTypesPrettyPrinter = new MCBasicTypesPrettyPrinter(printer);
        traverser.add4MCBasicTypes(mcBasicTypesPrettyPrinter);
        traverser.setMCBasicTypesHandler(mcBasicTypesPrettyPrinter);

        MySIUnitLiteralsPrettyPrinter siUnitLiteralsPrettyPrinter = new MySIUnitLiteralsPrettyPrinter(printer);
        traverser.setSIUnitLiteralsHandler(siUnitLiteralsPrettyPrinter);

        MCCommonLiteralsPrettyPrinter mcCommonLiteralsPrettyPrinter = new MCCommonLiteralsPrettyPrinter(printer);
        traverser.add4MCCommonLiterals(mcCommonLiteralsPrettyPrinter);
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
