/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.ExpressionsBasisSymTabMill;
import de.monticore.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SynthesizeSymTypeFromSIUnitTypesTest {

    private TestSIJavaParser parser = new TestSIJavaParser();
    // This is the TypeChecker under Test:
    private TypeCheck tc;

    @BeforeClass
    public static void setup() {
        Log.init();
        Log.enableFailQuick(false);
    }

    @Before
    public void setupForEach() {
        tc = new TypeCheck(new SynthesizeSymTypeFromSIUnitTypes(
                ExpressionsBasisSymTabMill.expressionsBasisScopeBuilder().build()), null);
    }

    // ------------------------------------------------------  Tests for Function 1, 1b, 1c
    private ASTSIUnitType parseSIUnitType(String input) throws IOException {
        Optional<ASTSIUnitType> res = parser.parseSIUnitType(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    private void check(String control, String s) throws IOException {
        ASTSIUnitType asttype = parseSIUnitType(s);
        SymTypeExpression type = tc.symTypeFromAST(asttype);
        assertEquals(control, type.print());
    }

    @Test
    public void symTypeFromAST_Test1() throws IOException {
        check("m", "m");
        check("m", "km");
        check("s^2", "m*s^2/km");
        check("s^2/m^2", "m*s^2/km^3");
    }
}
