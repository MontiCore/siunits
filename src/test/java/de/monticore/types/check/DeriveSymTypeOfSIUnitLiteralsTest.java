/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.testsiunitliterals.TestSIUnitLiteralsMill;
import de.monticore.siunits.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.siunits.testsiunitliterals._symboltable.ITestSIUnitLiteralsScope;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
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
    }

    protected ITestSIUnitLiteralsScope scope;

    @Before
    public void setupForEach() {
        scope = TestSIUnitLiteralsMill.testSIUnitLiteralsScopeBuilder()
                .setEnclosingScope(null)       // No enclosing Scope: Search ending here
                .setExportingSymbols(true)
                .setAstNode(null)
                .setName("Phantasy2").build();     // hopefully unused
    }

    // This is the core Visitor under Test (but rather empty)
    DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

    // other arguments not used (and therefore deliberately null)

    // This is the TypeChecker under Test:
    TypeCheck tc = new TypeCheck(null,derLit);

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
        ASTLiteral lit = parser.parse(new StringReader("3.2 km^2*s/h^2")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(double,km^2*s/h^2)", printType(tc.typeOf(lit)));
    }

    @Test
    public void deriveTFromLiteralComplex2() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3 (mm^2*mm^3)/(km*s^2*kg)")).get();
        lit.setEnclosingScope(scope);
        assertEquals("(int,mm^5/(kg*km*s^2))", printType(tc.typeOf(lit)));
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
        if (symType instanceof SymTypeOfNumericWithSIUnit)
            return ((SymTypeOfNumericWithSIUnit) symType).printDeclaredType();
        if (symType instanceof SymTypeOfSIUnit)
            return ((SymTypeOfSIUnit) symType).printDeclaredType();
        return symType.print();
    }
}
