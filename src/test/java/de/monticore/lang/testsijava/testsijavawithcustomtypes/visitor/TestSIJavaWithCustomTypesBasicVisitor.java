package de.monticore.lang.testsijava.testsijavawithcustomtypes.visitor;


import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;

public class TestSIJavaWithCustomTypesBasicVisitor implements TestSIJavaWithCustomTypesVisitor {
    private TestSIJavaWithCustomTypesVisitor realThis;

    @Override
    public void setRealThis(TestSIJavaWithCustomTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaWithCustomTypesVisitor getRealThis() {
        return realThis;
    }

    public TestSIJavaWithCustomTypesBasicVisitor() {
        realThis = this;
    }
}
