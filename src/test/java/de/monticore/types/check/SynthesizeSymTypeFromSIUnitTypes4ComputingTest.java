/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope;
import de.monticore.siunits.siunittypes4math._ast.ASTSIUnitType4Math;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SynthesizeSymTypeFromSIUnitTypes4ComputingTest {

    private TestSIJavaParser parser = new TestSIJavaParser();
    // This is the TypeChecker under Test:
    private TypeCheck tc = new TypeCheck(new SynthesizeSymTypeFromSIUnitTypes4Math(), null);;

    @BeforeClass
    public static void setup() {
        Log.init();
        Log.enableFailQuick(false);
    }

    ITestSIJavaScope scope;

    @Before
    public void setupForEach() {
        scope = TestSIJavaMill.testSIJavaScopeBuilder()
                .setEnclosingScope(null)       // No enclosing Scope: Search ending here
                .setExportingSymbols(true)
                .setAstNode(null)
                .setName("Phantasy2").build();     // hopefully unused
    }

    // ------------------------------------------------------  Tests for Function 1, 1b, 1c
    private ASTSIUnitType4Math parseSIUnitType4Math(String input) throws IOException {
        Optional<ASTSIUnitType4Math> res = parser.parseSIUnitType4Math(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    private void check(String control, String s) throws IOException {
        ASTSIUnitType4Math asttype = parseSIUnitType4Math(s);
        asttype.setEnclosingScope(scope);
        SymTypeExpression type = tc.symTypeFromAST(asttype);
        assertEquals(control, printType(type));
    }

    @Test
    public void symTypeFromAST_Test1() throws IOException {
        check("m", "m");
        check("km", "km");
        check("m*s^2/km", "m*s^2/km");
        check("m*s^2/km^3", "m*s^2/km^3");
    }

    protected String printType(SymTypeExpression symType) {
        if (symType instanceof SymTypeOfNumericWithSIUnit)
            return ((SymTypeOfNumericWithSIUnit) symType).printDeclaredType();
        if (symType instanceof SymTypeOfSIUnit)
            return ((SymTypeOfSIUnit) symType).printDeclaredType();
        return symType.print();
    }
}