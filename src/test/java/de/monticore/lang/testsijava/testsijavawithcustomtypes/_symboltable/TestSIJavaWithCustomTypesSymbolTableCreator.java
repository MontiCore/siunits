package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTVariableDeclaration;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;

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
    public void visit (ASTFieldDeclaration node)  {
        FieldSymbol symbol = create_FieldDeclaration(node);
        initialize_FieldDeclaration(symbol, node);
        addToScopeAndLinkWithNode(symbol, node);

        // Add type in the symbol table creation process
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes(this.scopeStack.getFirst()), null);
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
        TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes(this.scopeStack.getFirst()), null);
        ASTMCType astType = node.getMCType();
        SymTypeExpression symTypeExpression = tc.symTypeFromAST(astType);
        symbol.setType(symTypeExpression);
    }
}
