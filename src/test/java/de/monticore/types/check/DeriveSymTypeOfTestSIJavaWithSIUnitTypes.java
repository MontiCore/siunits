package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.testsijava.testsijava.visitor.TestSIJavaBasicVisitor;

public class DeriveSymTypeOfTestSIJavaWithSIUnitTypes extends DeriveSymTypeOfTestSIJava {
    public DeriveSymTypeOfTestSIJavaWithSIUnitTypes(IExpressionsBasisScope scope) {
        super(scope);
        setTestSIJavaVisitor(new TestSIJavaBasicVisitor());
    }
}
