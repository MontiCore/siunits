package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.check.*;
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
    public void visit(ASTFieldDeclaration node) {
        super.visit(node);
        // Add the enclosing scope
        node.accept(new TestSIJavaScopeSetter(node.getEnclosingScope()));
    }

    @Override
    public void endVisit(ASTFieldDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        initTypeCheck();
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
    }

    @Override
    public void visit(ASTSIFieldDeclaration node) {
        super.visit(node);
        // Add the enclosing scope
        node.accept(new TestSIJavaScopeSetter(node.getEnclosingScope()));
    }

    @Override
    public void endVisit(ASTSIFieldDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        initTypeCheck();
        SymTypeExpression symTypeExpression;
        if (node.isPresentSIUnitType()) {
            ASTSIUnitType astType = node.getSIUnitType();
            symTypeExpression = tc.symTypeFromAST(astType);
        } else {
            symTypeExpression = tc.typeOf(node.getExpression());
        }
        node.getSymbol().setType(symTypeExpression);
    }

    @Override
    public void visit(ASTMethodDeclaration node) {
        super.visit(node);
        // Add the enclosing scope
        node.getReturnType().accept(new TestSIJavaScopeSetter((ITestSIJavaScope) node.getSymbol().getSpannedScope()));
        for (ASTSIJavaParameter parameter: node.getSIJavaParameterList()) {
            parameter.setEnclosingScope((ITestSIJavaScope) node.getSymbol().getSpannedScope());
        }
        for (ASTSIJavaMethodStatement statement: node.getStatementList()) {
            statement.accept(new TestSIJavaScopeSetter((ITestSIJavaScope) node.getSymbol().getSpannedScope()));
        }
    }

    @Override
    public void endVisit(ASTMethodDeclaration node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        initTypeCheck();
        ASTMCReturnType astType = node.getReturnType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setReturnType(symTypeExpression);
    }

    @Override
    public void visit(ASTSIJavaParameter node) {
        super.visit(node);
        // Add the enclosing scope
        node.accept(new TestSIJavaScopeSetter(node.getEnclosingScope()));
    }

    @Override
    public void endVisit(ASTSIJavaParameter node) {
        super.endVisit(node);
        // Add type in the symbol table creation process
        initTypeCheck();
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
    }
}
