/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.types.check.FlatExpressionScopeSetterAbs;

public class TestSIJavaWithCustomTypesFlatExpressionScopeSetter extends FlatExpressionScopeSetterAbs
        implements TestSIJavaWithCustomTypesVisitor {

    public TestSIJavaWithCustomTypesFlatExpressionScopeSetter(ITestSIJavaWithCustomTypesScope scope) {
        super(scope);
    }

    // ************************* Visitor *************************

    private TestSIJavaWithCustomTypesVisitor realThis = this;

    @Override
    public void setRealThis(TestSIJavaWithCustomTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaWithCustomTypesVisitor getRealThis() {
        return realThis;
    }

}
