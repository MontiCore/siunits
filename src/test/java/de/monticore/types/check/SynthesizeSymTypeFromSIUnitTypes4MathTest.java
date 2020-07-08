/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaScope;
import de.monticore.siunits.utility.SIUnitConstants;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SynthesizeSymTypeFromSIUnitTypes4MathTest {

    private TestSIJavaParser parser = new TestSIJavaParser();
    // This is the TypeChecker under Test:
    private TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromSIUnitTypes4Math());

    @BeforeClass
    public static void setup() {
        Log.init();
        Log.enableFailQuick(false);
    }

    TestSIJavaScope scope;

    @Before
    public void setupForEach() {
        scope = TestSIJavaMill.testSIJavaScopeBuilder()
                .setEnclosingScope(null)       // No enclosing Scope: Search ending here
                .setExportingSymbols(true)
                .setAstNode(null)
                .setName("Phantasy2").build();     // hopefully unused
    }

    // ------------------------------------------------------  Tests for Function 1, 1b, 1c
    private ASTSIUnitType parseSIUnitType4Math(String input) throws IOException {
        Optional<ASTSIUnitType> res = parser.parseSIUnitType(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    private void check(String s) throws IOException {
        ASTSIUnitType asttype = parseSIUnitType4Math(s);
        asttype.setEnclosingScope(scope);
        SymTypeExpression type = tc.symTypeFromAST(asttype);
        assertEquals("(double," + UnitPrettyPrinter.printUnit(s) + ")", type.print());
    }

    @Test
    public void symTypeFromAST_Test1() throws IOException {
        check("mm*dm");
        check("m");
        check("km");
        check("m*s^2/km");
        check("m*s^2/km^3");
    }

    @Test
    public void testAll() throws IOException {
        for (String s: SIUnitConstants.getAllUnits()) {
            check(s);
        }
    }

    protected String printBaseType(SymTypeExpression symType) {
        return symType.print();
    }
}
