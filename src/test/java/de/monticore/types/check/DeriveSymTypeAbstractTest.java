/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._symboltable.ICombineExpressionsWithLiteralsScope;
import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._parser.CombineExpressionsWithSIUnitLiteralsParser;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.siunits.SIUnitsMill;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

public abstract class DeriveSymTypeAbstractTest {

    @BeforeClass
    public static void setup() {
        LogStub.init();         // replace log by a sideffect free variant
        // LogStub.initPlusLog();  // for manual testing purpose only
        Log.enableFailQuick(false);
    }

    @Before
    public void setupForEach() {
        LogStub.init();         // replace log by a sideffect free variant
        CombineExpressionsWithSIUnitLiteralsMill.reset();
        CombineExpressionsWithSIUnitLiteralsMill.init();
        CombineExpressionsWithSIUnitLiteralsMill.globalScope();
        SIUnitsMill.initializeSIUnits();
    }

    private TypeCheck tc;

    protected void setTypeCheck(TypeCheck tc) {
        this.tc = tc;
    }

    protected abstract void setupTypeCheck();


    // Parser used for convenience:
    // (may be any other Parser that understands CommonExpressions)
    private CombineExpressionsWithSIUnitLiteralsParser p = new CombineExpressionsWithSIUnitLiteralsParser();

    protected ASTExpression parseExpression(String expression) throws IOException {
        Optional<ASTExpression> astExpression = p.parse_StringExpression(expression);
        assertTrue(astExpression.isPresent());
        return astExpression.get();
    }

    private ExpressionsBasisTraverser flatExpressionScopeSetterTraverser;

    abstract ExpressionsBasisTraverser getTraverser();

    protected void setFlatExpressionScopeSetter(ICombineExpressionsWithLiteralsScope enclosingScope) {
        flatExpressionScopeSetterTraverser = getTraverser();
        FlatExpressionScopeSetter.addToTraverser(flatExpressionScopeSetterTraverser, enclosingScope);
    }

    protected void check(String expression, String expectedType) throws IOException {
        setupTypeCheck();
        ASTExpression astex = parseExpression(expression);
        if (flatExpressionScopeSetterTraverser != null)
            astex.accept(flatExpressionScopeSetterTraverser);

        assertEquals(expectedType, tc.typeOf(astex).print());
    }

    protected void checkError(String expression, String expectedError) throws IOException {
        setupTypeCheck();
        ASTExpression astex = parseExpression(expression);
        if (flatExpressionScopeSetterTraverser != null)
            astex.accept(flatExpressionScopeSetterTraverser);

        Log.getFindings().clear();
        try {
            tc.typeOf(astex);
        } catch (RuntimeException e) {
            assertEquals(expectedError, getFirstErrorCode());
            return;
        }
        fail();
    }

    private String getFirstErrorCode() {
        if (Log.getFindings().size() > 0) {
            String firstFinding = Log.getFindings().get(0).getMsg();
            return firstFinding.split(" ")[0];
        }
        return "";
    }

}
