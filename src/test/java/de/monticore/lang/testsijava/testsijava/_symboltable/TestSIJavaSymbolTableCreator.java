/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.siunits.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunits.siunittypes4math._ast.ASTSIUnitType4Math;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

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
    }

    @Override
    public void endVisit(ASTSIFieldDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        SymTypeExpression symTypeExpression;
        if (node.isPresentSIUnitType4Math()) {
            ASTSIUnitType4Math astType = node.getSIUnitType4Math();
            symTypeExpression = tc.symTypeFromAST(astType);
        } else {
            symTypeExpression = tc.typeOf(node.getExpression());
        }
        node.getSymbol().setType(symTypeExpression);
    }

    @Override
    public void endVisit(ASTMethodDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        ASTMCReturnType astType = node.getReturnType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setReturnType(symTypeExpression);
    }

    @Override
    public void endVisit(ASTSIJavaParameter node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);

        node.getSymbol().setIsParameter(true);
    }

    // ************************* Set enclosing scope ***********************

    @Override
    public void visit(ASTFieldDeclaration node) {
        super.visit(node);
        // Add the enclosing scope to the assignment
        if (node.isPresentAssignment())
            node.getAssignment().accept(new TestSIJavaFlatExpressionScopeSetter(node.getEnclosingScope()));
    }

    @Override
    public void visit(ASTSIFieldDeclaration node) {
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
    public void visit(ASTSIUnitType4Computing node) {
        super.visit(node);
        node.setEnclosingScope(scopeStack.getLast());
    }

    @Override
    public void visit(ASTSIUnitType4Math node) {
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
