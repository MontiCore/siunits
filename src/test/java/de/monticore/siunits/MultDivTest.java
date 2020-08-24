/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits;

import de.monticore.expressions.combineexpressionswithliterals._parser.CombineExpressionsWithLiteralsParser;
import de.monticore.expressions.commonexpressions._ast.ASTDivideExpression;
import de.monticore.expressions.commonexpressions._ast.ASTMultExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.se_rwth.commons.logging.Log;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

public class MultDivTest {

    @Test
    public void testDiv() throws IOException {
        Log.init();

        CombineExpressionsWithLiteralsParser parser = new CombineExpressionsWithLiteralsParser();

        String s = "3 mAh^2V/varM";
        Optional<ASTExpression> astExpression = parser.parse_StringExpression(s);
        assert(astExpression.isPresent());
        assert(astExpression.get() instanceof ASTDivideExpression);
        ASTDivideExpression expression = (ASTDivideExpression) astExpression.get();
        assert(expression.getLeft() instanceof ASTLiteralExpression);
        assert(((ASTLiteralExpression) expression.getLeft()).getLiteral() instanceof ASTSIUnitLiteral);
        assert(expression.getRight() instanceof ASTNameExpression);
    }

    @Test
    public void testMult() throws IOException {
        Log.init();

        CombineExpressionsWithLiteralsParser parser = new CombineExpressionsWithLiteralsParser();

        String s = "3 m*varM";
        Optional<ASTExpression> astExpression = parser.parse_StringExpression(s);
        assert(astExpression.isPresent());
        assert(astExpression.get() instanceof ASTMultExpression);
        ASTMultExpression expression = (ASTMultExpression) astExpression.get();
        assert(expression.getLeft() instanceof ASTLiteralExpression);
        assert(((ASTLiteralExpression) expression.getLeft()).getLiteral() instanceof ASTSIUnitLiteral);
        assert(expression.getRight() instanceof ASTNameExpression);
    }
}
