/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijavawithcustomtypes._cocos;


import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTMethodDeclaration;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTSIJavaMethodExpression;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable.TestSIJavaWithCustomTypesSymbolTableCreator;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesTraverser;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor2;
import de.monticore.types.check.DeriveSymTypeOfTestSIJavaWithCustomSIUnitTypes4Computing;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.check.cocos.TypeCheckCoCo;

/**
 * Checks for incompatible types in assignments and other expressions.
 * Take note, that this CoCo requires the TestSIJavaSymbolTableCreator to set the types for every
 *  field declaration when building the symbol table
 *  @see TestSIJavaWithCustomTypesSymbolTableCreator#visit(ASTFieldDeclaration node)
 */
public class TestSIJavaWithCustomTypesTypeCheckCoCo extends TypeCheckCoCo
        implements TestSIJavaWithCustomTypesASTSIJavaClassCoCo, TestSIJavaWithCustomTypesVisitor2 {

    public static TestSIJavaWithCustomTypesTypeCheckCoCo getCoCo() {
        TypeCheck typeCheck = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing(),
                new DeriveSymTypeOfTestSIJavaWithCustomSIUnitTypes4Computing());
        return new TestSIJavaWithCustomTypesTypeCheckCoCo(typeCheck);
    }

    public TestSIJavaWithCustomTypesTypeCheckCoCo(TypeCheck tc) {
        super(tc);
    }

    @Override
    public void check(ASTSIJavaClass node) {
    }

    @Override
    public void visit(ASTFieldDeclaration node) {
        checkFieldOrVariable(node, node.getExpression());
    }

    @Override
    public void visit(ASTSIJavaMethodExpression node) {
        checkExpression(node.getExpression());
    }

    @Override
    public void visit(ASTMethodDeclaration node) {
        ASTExpression returnExpression = node.isPresentSIJavaMethodReturn() ?
                node.getSIJavaMethodReturn().getExpression() : null;
        checkMethodOrFunction(node, returnExpression);
    }
}
