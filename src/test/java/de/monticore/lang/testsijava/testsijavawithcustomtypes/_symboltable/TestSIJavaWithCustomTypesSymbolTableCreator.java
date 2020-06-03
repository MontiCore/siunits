package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTMethodDeclaration;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTSIJavaMethodStatement;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTVariableDeclaration;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Deque;

/**
 * The SymType for every field symbol has to be set when creating the symbol table
 */
public class TestSIJavaWithCustomTypesSymbolTableCreator extends TestSIJavaWithCustomTypesSymbolTableCreatorTOP {

    public TestSIJavaWithCustomTypesSymbolTableCreator(ITestSIJavaWithCustomTypesScope enclosingScope) {
        super(enclosingScope);
    }

    public TestSIJavaWithCustomTypesSymbolTableCreator(Deque<? extends ITestSIJavaWithCustomTypesScope> scopeStack)  {
        super(scopeStack);
    }

    @Override
    public void endVisit (ASTFieldDeclaration node)  {
        super.endVisit(node);
        // Add the enclosing scope
        node.accept(new TestSIJavaWithCustomTypesScopeSetter(node.getEnclosingScope()));

        // Add type in the symbol table creation process
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing(), null);
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
    }

    @Override
    public void endVisit (ASTVariableDeclaration node) {
        super.endVisit(node);
        // Add the enclosing scope
        node.accept(new TestSIJavaWithCustomTypesScopeSetter(node.getEnclosingScope()));

        // Add type in the symbol table creation process
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing(), null);
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        node.getSymbol().setType(symTypeExpression);
    }

    @Override
    public void endVisit(ASTMethodDeclaration node) {
        super.endVisit(node);
        for (ASTSIJavaMethodStatement statement: node.getStatementList()) {
            statement.accept(new TestSIJavaWithCustomTypesScopeSetter(node.getSpannedScope()));
        }
    }
}
