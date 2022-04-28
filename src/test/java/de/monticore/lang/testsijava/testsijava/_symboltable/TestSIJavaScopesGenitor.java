/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.symboltable.modifiers.AccessModifier;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Iterator;

/**
 * The SymType for every field symbol has to be set when creating the symbol table
 */
public class TestSIJavaScopesGenitor extends TestSIJavaScopesGenitorTOP {

   public TestSIJavaScopesGenitor(){
        super();
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

        VariableSymbol _superNumericTypeVariable = BasicSymbolsMill.variableSymbolBuilder()
                .setName(_superNumericUnitType.getTypeInfo().getName())
                .setFullName(_superNumericUnitType.getTypeInfo().getName())
                .setAccessModifier(AccessModifier.ALL_INCLUSION)
                .setType(_superNumericUnitType)
                .build();
        VariableSymbol _doubleVariable = BasicSymbolsMill.variableSymbolBuilder()
                .setName(_double.getTypeInfo().getName())
                .setFullName(_double.getTypeInfo().getName())
                .setAccessModifier(AccessModifier.ALL_INCLUSION)
                .setType(_double)
                .build();

        MethodSymbol printSIUnitMethod = DefsTypeBasic.method("print", _void);
        printSIUnitMethod.getSpannedScope().add(_superNumericTypeVariable);
        node.getSpannedScope().add(printSIUnitMethod);

        MethodSymbol printNumericMethod = DefsTypeBasic.method("print", _void);
        printNumericMethod.getSpannedScope().add(_doubleVariable);
        node.getSpannedScope().add(printNumericMethod);


        MethodSymbol sivalueMethod = DefsTypeBasic.method("value", _double);
        sivalueMethod.getSpannedScope().add(_superNumericTypeVariable);
        node.getSpannedScope().add(sivalueMethod);


        MethodSymbol valueMethod = DefsTypeBasic.method("value", _double);
        valueMethod.getSpannedScope().add(_doubleVariable);
        node.getSpannedScope().add(valueMethod);


        MethodSymbol sibasevalueMethod = DefsTypeBasic.method("basevalue", _double);
        sibasevalueMethod.getSpannedScope().add(_superNumericTypeVariable);
        node.getSpannedScope().add(sibasevalueMethod);


        MethodSymbol basevalueMethod = DefsTypeBasic.method("basevalue", _double);
        basevalueMethod.getSpannedScope().add(_doubleVariable);
        node.getSpannedScope().add(basevalueMethod);
    }



    // Add subscope for parameters
    @Override
    public void traverse(ASTMethodDeclaration node) {
        if (null != node.getReturnType()) {
            node.getReturnType().accept(getTraverser());
        }
        {
            Iterator<ASTSIJavaParameter> iter_sIJavaParameters = node.getSIJavaParameterList().iterator();
            while (iter_sIJavaParameters.hasNext()) {
                iter_sIJavaParameters.next().accept(getTraverser());
            }
        }

        ITestSIJavaScope scope = createScope(false);
        putOnStack(scope);

        Iterator<ASTSIJavaMethodStatement> iter_sIJavaMethodStatements = node.getSIJavaMethodStatementList().iterator();
        while (iter_sIJavaMethodStatements.hasNext()) {
            iter_sIJavaMethodStatements.next().accept(getTraverser());
        }

        if (node.isPresentSIJavaMethodReturn()) {
            node.getSIJavaMethodReturn().accept(getTraverser());
        }

        if (node.getSpannedScope() != null) {
            node.getSpannedScope().accept(getTraverser());
        }

        scope.accept(getTraverser());

        removeCurrentScope();
    }

    // ************************* set type *************************

    /**
     * The TypeCheck is used to derive the type for a SIFieldDeclaration
     */
    private TypeCalculator tc;

    private void initTypeCheck() {
        ISynthesize synthesize = new SynthesizeSymTypeFromTestSIJava();
        IDerive der = new DeriveSymTypeOfTestSIJava();
        tc = new TypeCalculator(synthesize, der);
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
        node.getSymbol().setType(symTypeExpression);

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

    private void setFlat(ASTExpression expression, ITestSIJavaScope enclosingScope) {
        TestSIJavaTraverser traverser = TestSIJavaMill.traverser();
        FlatExpressionScopeSetter.addToTraverser(traverser, enclosingScope);
        expression.accept(traverser);
    }

    @Override
    public void visit(ASTFieldDeclaration node) {
        super.visit(node);
        // Add the enclosing scope to the assignment
        if (node.isPresentExpression())
            setFlat(node.getExpression(), node.getEnclosingScope());
    }

    public void visit(ASTSIJavaMethodExpression node) {
        super.visit(node);
        // Add the enclosing scope to the expression
        setFlat(node.getExpression(), scopeStack.getLast());
    }

    @Override
    public void visit(ASTSIJavaMethodReturn node) {
        super.visit(node);
        // Add the enclosing scope to the expression
        setFlat(node.getExpression(), scopeStack.getLast());
    }

}
