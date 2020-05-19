/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.check.FlatExpressionScopeSetterAbs;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;

public class TestSIJavaScopeSetter extends FlatExpressionScopeSetterAbs implements TestSIJavaVisitor {

    private TestSIJavaVisitor realThis = this;

    public TestSIJavaScopeSetter(ITestSIJavaScope scope) {
        super(scope);
    }

    @Override
    protected ITestSIJavaScope getScope() {
        return (TestSIJavaScope) scope;
    }

    @Override
    public void setRealThis(TestSIJavaVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public TestSIJavaVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void visit(ASTPrimitiveWithSIUnitType node) {
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
