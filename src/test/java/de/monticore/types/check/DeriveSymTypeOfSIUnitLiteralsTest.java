/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.siunits.SIUnitsMill;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.testsiunitliterals.TestSIUnitLiteralsMill;
import de.monticore.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.testsiunitliterals._symboltable.ITestSIUnitLiteralsScope;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class DeriveSymTypeOfSIUnitLiteralsTest {
    /**
     * Focus: Deriving Type of SIUnitLiterals, here:
     *    literals/SIUnitLiterals.mc4
     */

    @BeforeClass
    public static void setup() {
        Log.init();
        Log.enableFailQuick(false);
        TestSIUnitLiteralsMill.reset();
        TestSIUnitLiteralsMill.init();
        SIUnitsMill.initializeSIUnits();   }

    protected ITestSIUnitLiteralsScope scope;

    @Before
    public void setupForEach() {
        scope = TestSIUnitLiteralsMill.scope();
        scope.setEnclosingScope(null);       // No enclosing Scope: Search ending here
        scope.setExportingSymbols(true);
        scope.setAstNode(null);     // hopefully unused
    }

    // This is the core Visitor under Test (but rather empty)
    DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

    // other arguments not used (and therefore deliberately null)

    // This is the TypeChecker under Test:
    TypeCalculator tc = new TypeCalculator(null,derLit);

    TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();

    // ------------------------------------------------------  Tests for Function 2b
    @Test
    public void deriveTFromLiteralM() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 m")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(double,m)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromLiteralKM() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3 km")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(int,km)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromLiteralS() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3s")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(int,s)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromLiteralComplex1() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 km^2s/h^2")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(double,km^2*s/h^2)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromLiteralComplex2() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3 mm^5/kms^2g")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(int,mm^5/(g*km*s^2))", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromDimensionless1() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 sr")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(double,sr)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromDimensionless2() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 m/m")).get();
        lit.setEnclosingScope(scope);
        assertEquals("double", printType(tc.typeOf(lit)));
    }

    protected String printType(SymTypeExpression symType) {
        return symType.print();
    }
}
