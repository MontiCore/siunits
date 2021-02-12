/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable;

import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.*;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesTraverser;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.symboltable.modifiers.AccessModifier;
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
public class TestSIJavaWithCustomTypesScopesGenitor extends TestSIJavaWithCustomTypesScopesGenitorTOP {

    public TestSIJavaWithCustomTypesScopesGenitor(ITestSIJavaWithCustomTypesScope enclosingScope) {
        super(enclosingScope);
        initTypeCheck();
    }

    public TestSIJavaWithCustomTypesScopesGenitor(Deque<? extends ITestSIJavaWithCustomTypesScope> scopeStack)  {
        super(scopeStack);
        initTypeCheck();
    }

    public TestSIJavaWithCustomTypesScopesGenitor(){
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

        ITestSIJavaWithCustomTypesScope scope = createScope(false);
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
    private TypeCheck tc;

    private void initTypeCheck() {
        ISynthesize synthesize = new SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing();
        ITypesCalculator der = new DeriveSymTypeOfTestSIJavaWithCustomSIUnitTypes4Computing();
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

    private void setFlat(ASTExpression expression, ITestSIJavaWithCustomTypesScope enclosingScope) {
        TestSIJavaWithCustomTypesTraverser traverser = TestSIJavaWithCustomTypesMill.traverser();
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
