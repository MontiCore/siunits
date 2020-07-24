/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Deque;
import java.util.Iterator;

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

        addMethods(node);
    }

    private void addMethods(ASTSIJavaClass node) {
        SymTypeConstant _double = SymTypeExpressionFactory.createTypeConstant("double");
        SymTypeOfNumericWithSIUnit _superNumericUnitType = SymTypeOfNumericWithSIUnit.getSuperNumericUnitType();
        SymTypeVoid _void = SymTypeExpressionFactory.createTypeVoid();


        MethodSymbol printSIUnitMethod = DefsTypeBasic.method("print", _void);
        FieldSymbol field = DefsSIUnitType.field(_superNumericUnitType.getTypeInfo().getName(), _superNumericUnitType);
        printSIUnitMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(printSIUnitMethod);


        MethodSymbol printNumericMethod = DefsTypeBasic.method("print", _void);
        field = DefsSIUnitType.field("double", _double);
        printNumericMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(printNumericMethod);


        MethodSymbol sivalueMethod = DefsTypeBasic.method("value", _double);
        field = DefsSIUnitType.field(_superNumericUnitType.getTypeInfo().getName(), _superNumericUnitType);
        sivalueMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(sivalueMethod);


        MethodSymbol valueMethod = DefsTypeBasic.method("value", _double);
        field = DefsSIUnitType.field("double", _double);
        valueMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(valueMethod);


        MethodSymbol sibasevalueMethod = DefsTypeBasic.method("basevalue", _double);
        field = DefsSIUnitType.field(_superNumericUnitType.getTypeInfo().getName(), _superNumericUnitType);
        sibasevalueMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(sibasevalueMethod);


        MethodSymbol basevalueMethod = DefsTypeBasic.method("basevalue", _double);
        field = DefsSIUnitType.field("double", _double);
        basevalueMethod.getSpannedScope().add(field);
        node.getSpannedScope().add(basevalueMethod);
    }

    @Override
    public void traverse(ASTMethodDeclaration node) {
        if (null != node.getReturnType()) {
            node.getReturnType().accept(getRealThis());
        }
        {
            Iterator<ASTSIJavaParameter> iter_sIJavaParameters = node.getSIJavaParameterList().iterator();
            while (iter_sIJavaParameters.hasNext()) {
                iter_sIJavaParameters.next().accept(getRealThis());
            }
        }

        ITestSIJavaScope scope = createScope(false);
        putOnStack(scope);

        Iterator<ASTSIJavaMethodStatement> iter_sIJavaMethodStatements = node.getSIJavaMethodStatementList().iterator();
        while (iter_sIJavaMethodStatements.hasNext()) {
            iter_sIJavaMethodStatements.next().accept(getRealThis());
        }

        if (node.isPresentSIJavaMethodReturn()) {
            node.getSIJavaMethodReturn().accept(getRealThis());
        }

        if (node.getSpannedScope() != null) {
            node.getSpannedScope().accept(getRealThis());
        }

        scope.accept(getRealThis());

        removeCurrentScope();
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
