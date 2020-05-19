package de.monticore.lang.testsijava.testsijavawithcustomtypes._cocos;


import de.monticore.ast.ASTNode;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.*;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._symboltable.TestSIJavaWithCustomTypesSymbolTableCreator;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.types.check.DeriveSymTypeOfTestSIJavaWithCustomPrimitiveWithSIUnitTypes;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.logging.Log;

/**
 * Checks for incompatible types in assignments and other expressions.
 * Take note, that this CoCo requires the TestSIJavaSymbolTableCreator to set the types for every
 *  field declaration when building the symbol table
 *  @see TestSIJavaWithCustomTypesSymbolTableCreator#visit(ASTFieldDeclaration node)
 */
public class TypeCheckCoCo implements TestSIJavaWithCustomTypesASTSIJavaClassCoCo {

    @Override
    public void check(ASTSIJavaClass node) {
        node.accept(new CheckVisitor());
    }


    /**
     * checks the nodes of the SIJavaLanguage
     */
    private class CheckVisitor implements TestSIJavaWithCustomTypesVisitor {
        private TypeCheck tc;
        private DeriveSymTypeOfTestSIJavaWithCustomPrimitiveWithSIUnitTypes der;
        private boolean result;

        private CheckVisitor() {
            der = new DeriveSymTypeOfTestSIJavaWithCustomPrimitiveWithSIUnitTypes(); // custom Derive-Class
            this.tc = new TypeCheck(new SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes(), der); // and custom Synthesize-Class
        }

        @Override
        public void visit(ASTSIJavaClass node) {
            this.result = true;
        }

        public boolean getResult() {
            return result;
        }

        @Override
        public void visit(ASTVariableDeclaration node) {
            checkASTFieldOrVariableDeclaration(node);
        }

        @Override
        public void visit(ASTFieldDeclaration node) {
            checkASTFieldOrVariableDeclaration(node);
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

        private void checkASTFieldOrVariableDeclaration(ASTFieldOrVariableDeclaration node) {
            if (node.isPresentAssignment()) {

                ASTMCType astType = node.getMCType();
                SymTypeExpression varType = tc.symTypeFromAST(astType);

                try {
                    // should throw an exception if there are incompatible types in the assignment expression
                    //  e.g. [m var = 3m + 3s
                    if (!tc.isOfTypeForAssign(varType, node.getAssignment())) {
                        SymTypeExpression assignmentType = tc.typeOf(node.getAssignment());
                        logError_notCompatible(node, varType.print(), assignmentType.print());
                    }
                } catch (Exception e) {
                    logError(node, "Incompatible types in the assignment");
                }
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
