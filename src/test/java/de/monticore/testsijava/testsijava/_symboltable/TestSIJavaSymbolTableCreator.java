package de.monticore.testsijava.testsijava._symboltable;

import de.monticore.testsijava.testsijava._ast.ASTFieldDeclaration;
import de.monticore.testsijava.testsijava._ast.ASTVariableDeclaration;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJavaTypes;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;

import java.util.Deque;

public class TestSIJavaSymbolTableCreator extends TestSIJavaSymbolTableCreatorTOP {

    public TestSIJavaSymbolTableCreator(ITestSIJavaScope enclosingScope) {
        super(enclosingScope);
    }

    public  TestSIJavaSymbolTableCreator(Deque<? extends de.monticore.testsijava.testsijava._symboltable.ITestSIJavaScope> scopeStack)  {
        super(scopeStack);
    }

    @Override
    public void visit (ASTFieldDeclaration node)  {
        FieldSymbol symbol = create_FieldDeclaration(node);
        initialize_FieldDeclaration(symbol, node);
        addToScopeAndLinkWithNode(symbol, node);

        // Add type in the symbol table creation process
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaTypes(), null);
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        symbol.setType(symTypeExpression);
    }

    @Override
    public void visit (ASTVariableDeclaration node) {
        FieldSymbol symbol = create_VariableDeclaration(node);
        initialize_VariableDeclaration(symbol, node);
        addToScopeAndLinkWithNode(symbol, node);

        // Add type in the symbol table creation process
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaTypes(), null);
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        symbol.setType(symTypeExpression);
    }
}
