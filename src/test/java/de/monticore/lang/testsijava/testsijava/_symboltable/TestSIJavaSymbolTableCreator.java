package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIFieldDeclaration;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.lang.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;

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
