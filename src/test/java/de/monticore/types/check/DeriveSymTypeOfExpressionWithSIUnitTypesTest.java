/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._symboltable.ICombineExpressionsWithSIUnitLiteralsScope;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;

public class DeriveSymTypeOfExpressionWithSIUnitTypesTest extends DeriveSymTypeAbstractTest {


    @Override
    protected void setupTypeCheck() {
        // This is an auxiliary
        ITypesCalculator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCheck(null, derLit));
    }

    @Before
    public void setupForEach() {
        // No enclosing Scope: Search ending here
        ICombineExpressionsWithSIUnitLiteralsScope scope = CombineExpressionsWithSIUnitLiteralsMill
                .combineExpressionsWithSIUnitLiteralsScopeBuilder()
                .setEnclosingScope(null)       // No enclosing Scope: Search ending here
                .setExportingSymbols(true)
                .setAstNode(null)
                .build();

        add2scope(scope, field("varM", SIUnitSymTypeExpressionFactory.createSIUnit("m", scope)));
        add2scope(scope, field("varKM", SIUnitSymTypeExpressionFactory.createSIUnit("km", scope)));
        add2scope(scope, field("varS", SIUnitSymTypeExpressionFactory.createSIUnit("s", scope)));

        setFlatExpressionScopeSetter(new CombineExpressionWithSIUnitLiteralsFlatScopeSetter(scope));
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
