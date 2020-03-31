package de.monticore.testsijava.testsijava._symboltable;

import de.monticore.testsijava.testsijava._ast.ASTFieldDeclaration;
import de.monticore.testsijava.testsijava._ast.ASTSIFieldDeclaration;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;

import java.util.Deque;

public class TestSIJavaSymbolTableCreator extends TestSIJavaSymbolTableCreatorTOP {

    public TestSIJavaSymbolTableCreator(ITestSIJavaScope enclosingScope) {
        super(enclosingScope);
        initTypeCheck();
    }

    public TestSIJavaSymbolTableCreator(Deque<? extends de.monticore.testsijava.testsijava._symboltable.ITestSIJavaScope> scopeStack) {
        super(scopeStack);
        initTypeCheck();
    }

    private TypeCheck tc;

    private void initTypeCheck() {
        ISynthesize synthesize = new SynthesizeSymTypeFromTestSIJava(this.scopeStack.getLast());
        ITypesCalculator der = new DeriveSymTypeOfTestSIJava(this.scopeStack.getLast());
        tc = new TypeCheck(synthesize, der);
    }

    @Override
    public void visit(ASTFieldDeclaration node) {
        FieldSymbol symbol = create_FieldDeclaration(node);
        initialize_FieldDeclaration(symbol, node);
        addToScopeAndLinkWithNode(symbol, node);

        // Add type in the symbol table creation process
        initTypeCheck();
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        symbol.setType(symTypeExpression);
    }

    @Override
    public void visit(ASTSIFieldDeclaration node) {
        FieldSymbol symbol = create_SIFieldDeclaration(node);
        initialize_SIFieldDeclaration(symbol, node);
        addToScopeAndLinkWithNode(symbol, node);

        // Add type in the symbol table creation process
        initTypeCheck();
        SymTypeExpression symTypeExpression;
        if (node.isPresentSIUnitType()) {
            ASTSIUnitType astType = node.getSIUnitType();
            symTypeExpression = tc.symTypeFromAST(astType);
        } else {
            symTypeExpression = tc.typeOf(node.getExpression());
        }
        symbol.setType(symTypeExpression);
    }
}
