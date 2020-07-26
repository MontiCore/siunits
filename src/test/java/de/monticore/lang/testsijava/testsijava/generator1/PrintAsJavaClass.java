/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator1;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaDelegatorVisitor;
import de.monticore.lang.testsijava.testsijava.generator1.prettyprint.*;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class PrintAsJavaClass extends TestSIJavaDelegatorVisitor {

    public PrintAsJavaClass(IndentPrinter printer) {
        setAssignmentExpressionsVisitor(new MyAssignmentExpressionsPrettyPrinter(printer));
        setCommonExpressionsVisitor(new MyCommonExpressionsPrettyPrinter(printer));
        setExpressionsBasisVisitor(new MyExpressionsBasisPrettyPrinter(printer));
        setTestSIJavaVisitor(new TestSIJavaPrettyPrinter(printer));
        setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
        setSIUnitLiteralsVisitor(new MySIUnitLiteralsPrettyPrinter(printer));
        setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    }

    public static String printAsJavaClass(ASTSIJavaClass siclass) {
        IndentPrinter printer = new IndentPrinter();
        printer.setIndentLength(4);
        PrintAsJavaClass printAsJavaClass = new PrintAsJavaClass(printer);
        siclass.accept(printAsJavaClass);
        return printer.getContent();
    }

}
