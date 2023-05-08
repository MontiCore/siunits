/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope;
import de.monticore.siunits.SIUnitsMill;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SynthesizeSymTypeFromSIUnitTypes4ComputingTest {

    private TestSIJavaParser parser = new TestSIJavaParser();
    // This is the TypeChecker under Test:
    private TypeCalculator tc = new TypeCalculator(new SynthesizeSymTypeOfCombinedTypes(), null);

    ITestSIJavaScope scope;

    @Before
    public void setupForEach() {
        Log.init();
        Log.enableFailQuick(false);

        TestSIJavaMill.reset();
        TestSIJavaMill.init();
        SIUnitsMill.initializeSIUnits();

        scope = TestSIJavaMill.scope();
        scope.setEnclosingScope(null);     // No enclosing Scope: Search ending here
        scope.setExportingSymbols(true);
        scope.setAstNode(null);     // hopefully unused
    }

    // ------------------------------------------------------  Tests for Function 1, 1b, 1c
    private ASTSIUnitType4Computing parseSIUnitType4Computing(String input) throws IOException {
        Optional<ASTSIUnitType4Computing> res = parser.parseSIUnitType4Computing(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    private void check(String control, String s) throws IOException {
        ASTSIUnitType4Computing asttype = parseSIUnitType4Computing(s);
        asttype.setEnclosingScope(scope);
        asttype.getMCPrimitiveType().setEnclosingScope(BasicSymbolsMill.globalScope());
        SymTypeExpression type = tc.symTypeFromAST(asttype);
        assertEquals(control, printType(type));
    }

    @Test
    public void symTypeFromAST_Test1() throws IOException {
        check("(double,m)", "m<double>");
        check("(int,km)", "km<int>");
        check("(float,m*s^2/km)", "s^2m/km<float>");
        check("(long,m*s^2/km^3)", "s^2m/km^3<long>");
    }

    protected String printType(SymTypeExpression symType) {
        return symType.print();
    }
}