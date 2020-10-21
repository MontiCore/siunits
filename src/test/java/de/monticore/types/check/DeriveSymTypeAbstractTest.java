/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public abstract class DeriveSymTypeAbstractTest {

    @BeforeClass
    public static void setup() {
        LogStub.init();         // replace log by a sideffect free variant
        // LogStub.initPlusLog();  // for manual testing purpose only
        Log.enableFailQuick(false);
    }

    @BeforeAll
    public void setupForAll() {
        setupTypeCheck();
    }

    @Before
    public void setupForEach() {
        initScope();
        setupScope();
        flatExpressionScopeSetter = new FlatExpressionScopeSetter(getAsExpressionBasisScope());
    }

    protected TypeCheck tc;

    protected abstract void setupTypeCheck();

    protected abstract void initScope();

    protected abstract void setupScope();

    protected abstract IExpressionsBasisScope getAsExpressionBasisScope();

    protected abstract IOOSymbolsScope getAsOOSymbolsScope();


    protected abstract ASTExpression parseExpression(String expression) throws IOException;

    protected FlatExpressionScopeSetter flatExpressionScopeSetter;

    protected void check(String expression, String expectedType) throws IOException {
        ASTExpression astex = parseExpression(expression);
        astex.accept(flatExpressionScopeSetter);
        assertEquals(expectedType, tc.typeOf(astex).print());
    }

    protected void checkError(String expression, String expectedError) throws IOException {
        ASTExpression astex = parseExpression(expression);
        astex.accept(flatExpressionScopeSetter);
        Log.getFindings().clear();
        try {
            tc.typeOf(astex);
        } catch(RuntimeException e) {
            assertEquals(expectedError, getFirstErrorCode());
        }
    }

    private String getFirstErrorCode() {
        if (Log.getFindings().size() > 0) {
            String firstFinding = Log.getFindings().get(0).getMsg();
            return firstFinding.split(" ")[0];
        }
        return "";
    }

}
