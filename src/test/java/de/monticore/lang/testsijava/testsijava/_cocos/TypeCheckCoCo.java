/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava.testsijava._cocos;

import de.monticore.ast.ASTNode;
import de.monticore.lang.testsijava.testsijava._ast.ASTFieldDeclaration;
import de.monticore.lang.testsijava.testsijava._ast.ASTMethodDeclaration;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaMethodExpression;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaSymbolTableCreator;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.types.check.DeriveSymTypeOfTestSIJava;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJava;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.logging.Log;

/**
 * Checks for incompatible types in assignments and other expressions.
 * Take note, that this CoCo requires the TestSIJavaSymbolTableCreator to set the types for every
 *  field declaration when building the symbol table
 *  @see TestSIJavaSymbolTableCreator#visit(ASTFieldDeclaration node)
 */
public class TypeCheckCoCo implements TestSIJavaASTSIJavaClassCoCo {
    @Override
    public void check(ASTSIJavaClass node) {
        node.accept(new CheckVisitor());
    }

    /**
     * checks the nodes of the SIJavaLanguage
     */
    private class CheckVisitor implements TestSIJavaVisitor {
        private TypeCheck tc;
        private DeriveSymTypeOfTestSIJava der;
        private boolean result;

        private CheckVisitor() {
            der = new DeriveSymTypeOfTestSIJava(); // custom Derive-Class
            this.tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJava(), der); // and custom Synthesize-Class
        }

        @Override
        public void visit(ASTSIJavaClass node) {
            this.result = true;
        }

        public boolean getResult() {
            return result;
        }

        @Override
        public void visit(ASTFieldDeclaration node) {
            if (node.isPresentExpression()) {
                ASTMCType astType = node.getMCType();
                SymTypeExpression varType = tc.symTypeFromAST(astType);

                try {
                    // should throw an exception if there are incompatible types in the assignment expression
                    //  e.g. [m var = 3m + 3s
                    if (!tc.isOfTypeForAssign(tc.symTypeFromAST(astType), node.getExpression())) {
                        SymTypeExpression assignmentType = tc.typeOf(node.getExpression());
                        logError_notCompatible(node, varType.print(), assignmentType.print());
                    }
                } catch (Exception e) {
                    logError(node, "Incompatible types in the assignment");
                }
            }
        }

        @Override
        public void visit(ASTSIJavaMethodExpression node) {
            try {
                // should throw an exception if there are incompatible types
                SymTypeExpression assignmentType2 = tc.typeOf(node.getExpression());
                if (assignmentType2 == null) logError(node, "Incompatible types");
            } catch (Exception e) {
                logError(node, "Incompatible types");
            }
        }

        @Override
        public void visit(ASTMethodDeclaration node) {
            if (node.getSymbol().getReturnType().isVoidType()) {
                if (node.isPresentSIJavaMethodReturn())
                    logError(node.getSIJavaMethodReturn(), "Return type void incompatible to actual return");
            } else if (node.isPresentSIJavaMethodReturn()) {
                try {
                    // should throw an exception if there are incompatible types
                    SymTypeExpression returnType = tc.typeOf(node.getSIJavaMethodReturn().getExpression());
                    if (returnType == null) logError(node.getSIJavaMethodReturn(), "Incompatible types");
                    if (!TypeCheck.compatible(node.getSymbol().getReturnType(), returnType))
                        logError(node, "Return type void incompatible to actual return");
                } catch (Exception e) {
                    logError(node.getSIJavaMethodReturn(), "Incompatible types");
                }
            } else {
                logError(node, "No return present");
            }
        }

        private void logError(ASTNode node, String msg) {
            result = false;
            Log.error(node.get_SourcePositionStart() + " " + msg);
        }

        private void logError_notCompatible(ASTNode node, String type1, String type2) {
            logError(node,"Type " + type1 + " and type " + type2 + " are not compatible");
        }
    }
}
