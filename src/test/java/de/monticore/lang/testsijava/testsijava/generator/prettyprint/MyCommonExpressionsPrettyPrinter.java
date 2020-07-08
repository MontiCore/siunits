/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.commonexpressions._ast.ASTCallExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class MyCommonExpressionsPrettyPrinter extends CommonExpressionsPrettyPrinter {
    public MyCommonExpressionsPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

    @Override
    public void handle(ASTCallExpression node) {
        CommentPrettyPrinter.printPreComments(node, this.getPrinter());
        node.getExpression().accept(getRealThis());
        this.getPrinter().print("_");
        this.handle((ASTArguments) node.getArguments());
        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
