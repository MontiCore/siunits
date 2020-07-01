/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava._cocos;

import de.monticore.expressions.assignmentexpressions._ast.ASTAssignmentExpression;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaMethodExpression;
import de.se_rwth.commons.logging.Log;

public class MethodExpressionIsAssignmentExpressionCoCo implements TestSIJavaASTSIJavaMethodExpressionCoCo {
    @Override
    public void check(ASTSIJavaMethodExpression node) {
        if (!(node.getExpression() instanceof ASTAssignmentExpression)) {
            Log.error("0xE567743 Only AssignmentExpressions are allowed as SIJavaMethodExpression");
        }
    }
}
