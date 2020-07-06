/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class MyCommonExpressionsPrettyPrinter extends CommonExpressionsPrettyPrinter {
    public MyCommonExpressionsPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

//    @Override
//    public void handle(ASTCallExpression node) {
//        CommentPrettyPrinter.printPreComments(node, this.getPrinter());
//        node.getExpression().accept(new NormalExpressionPrettyPrinter(this.getPrinter()));
//        node.getArguments().accept(getRealThis());
//        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
//    }
}
