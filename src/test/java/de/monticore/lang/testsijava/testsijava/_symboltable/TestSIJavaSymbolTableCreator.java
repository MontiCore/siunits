/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.typesymbols._symboltable.MethodSymbol;

import java.util.Deque;

/**
 * The SymType for every field symbol has to be set when creating the symbol table
 */
public class TestSIJavaSymbolTableCreator extends TestSIJavaSymbolTableCreatorTOP {

    public TestSIJavaSymbolTableCreator(ITestSIJavaScope enclosingScope) {
        super(enclosingScope);
        initTypeCheck();
    }

    public TestSIJavaSymbolTableCreator(Deque<? extends de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope> scopeStack) {
        super(scopeStack);
        initTypeCheck();
    }

    @Override
    public void visit(ASTSIJavaClass node) {
        super.visit(node);

        MethodSymbol printMethod = new MethodSymbol("print");
        printMethod.setReturnType(SymTypeExpressionFactory.createTypeVoid());
        node.getSpannedScope().add(printMethod);

        MethodSymbol value = new MethodSymbol("value");
    }

    // ************************* set type *************************

    /**
     * The TypeCheck is used to derive the type for a SIFieldDeclaration
     */
    private TypeCheck tc;

    private void initTypeCheck() {
        ISynthesize synthesize = new SynthesizeSymTypeFromTestSIJava();
        ITypesCalculator der = new DeriveSymTypeOfTestSIJava();
        tc = new TypeCheck(synthesize, der);
    }

    @Override
    public void endVisit(ASTFieldDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
//        node.getSymbol().setIsVariable(true);
    }

    @Override
    public void endVisit(ASTMethodDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        ASTMCReturnType astType = node.getReturnType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setReturnType(symTypeExpression);

        // Add parameters
    }

    @Override
    public void endVisit(ASTSIJavaParameter node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
//        node.getSymbol().setIsParameter(true);
    }

    // ************************* Set enclosing scope ***********************

    @Override
    public void visit(ASTFieldDeclaration node) {
        super.visit(node);
        // Add the enclosing scope to the assignment
        if (node.isPresentExpression())
            node.getExpression().accept(new TestSIJavaFlatExpressionScopeSetter(node.getEnclosingScope()));
    }

    public void visit(ASTSIJavaMethodExpression node) {
        super.visit(node);
        // Add the enclosing scope to the expression
        node.getExpression().accept(new TestSIJavaFlatExpressionScopeSetter(scopeStack.getLast()));
    }

    @Override
    public void visit(ASTSIJavaMethodReturn node) {
        super.visit(node);
        // Add the enclosing scope to the expression
        node.getExpression().accept(new TestSIJavaFlatExpressionScopeSetter(scopeStack.getLast()));
    }

    @Override
    public void visit(ASTSIUnitType4Computing node) {
        super.visit(node);
        node.setEnclosingScope(scopeStack.getLast());
    }

    @Override
    public void visit(ASTSIUnitType node) {
        super.visit(node);
        node.setEnclosingScope(scopeStack.getLast());
    }

    @Override
    public void visit(ASTMCPrimitiveType node) {
        super.visit(node);
        node.setEnclosingScope(scopeStack.getLast());
    }

    @Override
    public void visit(ASTMCQualifiedType node) {
        super.visit(node);
        node.setEnclosingScope(scopeStack.getLast());
    }
}
