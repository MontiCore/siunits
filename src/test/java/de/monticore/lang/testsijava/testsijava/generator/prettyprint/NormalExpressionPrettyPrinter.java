/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.prettyprint.AssignmentExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaDelegatorVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunitliterals.prettyprint.SIUnitLiteralsPrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class NormalExpressionPrettyPrinter extends TestSIJavaDelegatorVisitor {

    public NormalExpressionPrettyPrinter(IndentPrinter printer) {
        setAssignmentExpressionsVisitor(new AssignmentExpressionsPrettyPrinter(printer));
        setCommonExpressionsVisitor(new CommonExpressionsPrettyPrinter(printer));
        setExpressionsBasisVisitor(new ExpressionsBasisPrettyPrinter(printer));
        setTestSIJavaVisitor(new TestSIJavaPrettyPrinter(printer));
        setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
        setSIUnitLiteralsVisitor(new SIUnitLiteralsPrettyPrinter(printer));
        setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    }

}
