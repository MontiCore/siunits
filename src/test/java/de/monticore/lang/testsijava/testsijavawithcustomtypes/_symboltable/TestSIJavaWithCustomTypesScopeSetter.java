/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
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
    public void visit(ASTSIUnitType4Computing node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTCustomSIUnitType4Computing node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTSIUnit node) {
        node.setEnclosingScope(getScope());
    }

    @Override
    public void visit(ASTMCPrimitiveType node) {
        node.setEnclosingScope(getScope());
    }
}
