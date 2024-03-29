/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._parser.CombineExpressionsWithSIUnitLiteralsParser;
import de.monticore.expressions.combineexpressionswithsiunitliterals._symboltable.ICombineExpressionsWithSIUnitLiteralsScope;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.siunits.SIUnitsMill;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;

public class DeriveSymTypeOfExpressionWithSIUnitTypesTest extends DeriveSymTypeAbstractTest {

    CombineExpressionsWithSIUnitLiteralsParser p = new CombineExpressionsWithSIUnitLiteralsParser();
    @Override
    protected Optional<ASTExpression> parseStringExpression(String expression) throws IOException {
        return p.parse_StringExpression(expression);
    }

    @Override
    protected ExpressionsBasisTraverser getUsedLanguageTraverser() {
        return CombineExpressionsWithSIUnitLiteralsMill.traverser();
    }

    @Override
    protected void setupTypeCheck() {
        // This is an auxiliary
        AbstractDerive derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCalculator(null, derLit));
    }

    @Before
    public void setupForEach() {
        CombineExpressionsWithSIUnitLiteralsMill.reset();
        CombineExpressionsWithSIUnitLiteralsMill.init();
        SIUnitsMill.initializeSIUnits();
        // No enclosing Scope: Search ending here
        ICombineExpressionsWithSIUnitLiteralsScope scope = CombineExpressionsWithSIUnitLiteralsMill.scope();
        scope.setEnclosingScope(null);       // No enclosing Scope: Search ending here
        scope.setExportingSymbols(true);
        scope .setAstNode(null);

        add2scope(scope, field("varM", SIUnitSymTypeExpressionFactory._deprecated_createSIUnit("m", scope)));
        add2scope(scope, field("varKM", SIUnitSymTypeExpressionFactory._deprecated_createSIUnit("km", scope)));
        add2scope(scope, field("varS", SIUnitSymTypeExpressionFactory._deprecated_createSIUnit("s", scope)));

        setFlatExpressionScopeSetter(scope);
    }

    // ------------------------------------------------------  Tests for Function 2


    @Test
    public void deriveTFromASTNameExpression6() throws IOException {
        check("varM", "m");
    }

    @Test
    public void deriveTFromASTNameExpression7() throws IOException {
        check("varKM", "km");
    }

    @Test
    public void deriveTFromSIUnitLiteral() throws IOException {
        check("42.3 km", "(double,km)");
    }
}
