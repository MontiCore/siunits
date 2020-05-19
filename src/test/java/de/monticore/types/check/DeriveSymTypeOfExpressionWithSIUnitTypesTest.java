/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;
import static org.junit.Assert.assertEquals;

public class DeriveSymTypeOfExpressionWithSIUnitTypesTest extends DeriveSymTypeOfExpressionTest {

    @Before
    public void setupForEach() {
        super.setupForEach();
        add2scope(scope, field("varM", SIUnitSymTypeExpressionFactory.createSIUnit("m", scope)));
        add2scope(scope, field("varKM", SIUnitSymTypeExpressionFactory.createSIUnit("km", scope)));
        add2scope(scope, field("varS", SIUnitSymTypeExpressionFactory.createSIUnit("s", scope)));
    }

    // ------------------------------------------------------  Tests for Function 2


    @Test
    public void deriveTFromASTNameExpression6() throws IOException {
        String s = "varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("m", tc.typeOf(astex).print());
    }

    @Test
    public void deriveTFromASTNameExpression7() throws IOException {
        String s = "varKM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("m", tc.typeOf(astex).print());
    }

    @Test
    public void deriveTFromSIUnitLiteral() throws IOException {
        ASTExpression astex = p.parse_StringExpression("42.3 km").get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", tc.typeOf(astex).print());
    }
}
