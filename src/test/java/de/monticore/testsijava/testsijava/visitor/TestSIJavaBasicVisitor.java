package de.monticore.testsijava.testsijava.visitor;

import de.monticore.testsijava.testsijava._visitor.TestSIJavaVisitor;

public class TestSIJavaBasicVisitor implements TestSIJavaVisitor {

    private TestSIJavaVisitor realThis;

    @Override
    public void setRealThis(TestSIJavaVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaVisitor getRealThis() {
        return realThis;
    }

    public TestSIJavaBasicVisitor() {
        realThis = this;
    }
}
