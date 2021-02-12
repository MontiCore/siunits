/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._cocos;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.testsijava.testsijava._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijava._ast.ASTMethodDeclaration;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaMethodExpression;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaScopesGenitor;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor2;
import de.monticore.types.check.DeriveSymTypeOfTestSIJava;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJava;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.check.cocos.TypeCheckCoCo;

/**
 * Checks for incompatible types in method expressions and other expressions.
 * Take note, that this CoCo requires the TestSIJavaSymbolTableCreator to set the type for each
 *  field and method declaration when building the symbol table and also set the enclosing scope for each
 *  expression
 *  @see TestSIJavaScopesGenitor
 */
public class TestSIJavaTypeCheckCoCo extends TypeCheckCoCo
    implements TestSIJavaASTSIJavaClassCoCo, TestSIJavaVisitor2 {

    public static TestSIJavaTypeCheckCoCo getCoCo() {
        TypeCheck typeCheck = new TypeCheck(new SynthesizeSymTypeFromTestSIJava(), new DeriveSymTypeOfTestSIJava());
        return new TestSIJavaTypeCheckCoCo(typeCheck);
    }

    public TestSIJavaTypeCheckCoCo(TypeCheck tc) {
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
