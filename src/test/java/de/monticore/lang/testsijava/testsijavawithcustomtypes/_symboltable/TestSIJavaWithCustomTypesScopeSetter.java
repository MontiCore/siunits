/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.lang.types.customprimitivewithsiunittypes._ast.ASTCustomPrimitiveWithSIUnitType;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.check.FlatExpressionScopeSetterAbs;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;

public class TestSIJavaWithCustomTypesScopeSetter extends FlatExpressionScopeSetterAbs implements TestSIJavaWithCustomTypesVisitor {

    private TestSIJavaWithCustomTypesVisitor realThis = this;

    public TestSIJavaWithCustomTypesScopeSetter(ITestSIJavaWithCustomTypesScope scope) {
        super(scope);
    }

    @Override
    protected ITestSIJavaWithCustomTypesScope getScope() {
        return (ITestSIJavaWithCustomTypesScope) scope;
    }

    @Override
    public void setRealThis(TestSIJavaWithCustomTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaWithCustomTypesVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void visit(ASTPrimitiveWithSIUnitType node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTCustomPrimitiveWithSIUnitType node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTSIUnitType node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTMCPrimitiveType node) {
        node.setEnclosingScope(getScope());
    }
}
