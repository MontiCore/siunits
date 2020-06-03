/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.types.check.FlatExpressionScopeSetterAbs;

public class TestSIJavaFlatExpressionScopeSetter extends FlatExpressionScopeSetterAbs implements TestSIJavaVisitor {

    public TestSIJavaFlatExpressionScopeSetter(ITestSIJavaScope scope) {
        super(scope);
    }

    @Override
    protected ITestSIJavaScope getScope() {
        return (TestSIJavaScope) scope;
    }

    // ************************* Visitor *************************

    private TestSIJavaVisitor realThis = this;

    @Override
    public void setRealThis(TestSIJavaVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaVisitor getRealThis() {
        return realThis;
    }

}
