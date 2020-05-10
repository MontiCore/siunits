package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.ExpressionsBasisSymTabMill;
import de.monticore.expressions.prettyprint.CombineExpressionsWithSIUnitLiteralsPrettyPrinter;
import de.monticore.lang.literals.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
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

    // This is the core Visitor under Test (but rather empty)
    DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator(ExpressionsBasisSymTabMill.expressionsBasisScopeBuilder().build(), new CombineExpressionsWithSIUnitLiteralsPrettyPrinter(new IndentPrinter()));

    // other arguments not used (and therefore deliberately null)

    // This is the TypeChecker under Test:
    TypeCheck tc = new TypeCheck(null,derLit);

    TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();

    // ------------------------------------------------------  Tests for Function 2b
    @Test
    public void deriveTFromLiteralM() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 m")).get();
        assertEquals("(double,m)", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromLiteralKM() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3 km")).get();
        assertEquals("(int,m)", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromLiteralS() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3s")).get();
        assertEquals("(int,s)", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromLiteralComplex1() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 km^2*s/h^2")).get();
        assertEquals("(double,m^2/s)", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromLiteralComplex2() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3 (mm^2*m^3)/(km*s^2*kg)")).get();
        assertEquals("(int,m^4/(s^2*kg))", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromDimensionless1() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 sr")).get();
        assertEquals("(double,sr)", tc.typeOf(lit).print());
    }

    @Test
    public void deriveTFromDimensionless2() throws IOException {
        ASTLiteral lit = parser.parse(new StringReader("3.2 m/m")).get();
        assertEquals("double", tc.typeOf(lit).print());
    }
}
