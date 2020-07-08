/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeriveSymTypeOfCommonExpressionWithSIUnitTypesTest extends DeriveSymTypeOfCommonExpressionTest {

    /**
     * Focus: Deriving Type of SIUnitLiterals, here:
     * lang/literals/SIUnitLiterals.mc4
     */

    /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

    /**
     * test correctness of addition
     */
    @Test
    public void deriveFromPlusExpression() throws IOException {
        // example with siunit
        String s = "4.1m/h + 12m/s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m/h)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidPlusExpression1() throws IOException {
        String s = "4.1km/h + 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0210", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidPlusExpression2() throws IOException {
        String s = "4.1km/h + 12";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0210", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidPlusExpression3() throws IOException {
        String s = "varM + 2 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0210", getFirstErrorCode());
        }
    }

    /**
     * test correctness of subtraction
     */
    @Test
    public void deriveFromMinusExpression() throws IOException {
        // example with siunit
        String s = "4.1km/h - 12m/s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m/s)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidMinusExpression1() throws IOException {
        String s = "4.1km/h - 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0213", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidMinusExpression2() throws IOException {
        String s = "4.1km/h - 12";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0213", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidMinusExpression3() throws IOException {
        String s = "varM - 2 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0213", getFirstErrorCode());
        }
    }

    /**
     * test correctness of multiplication
     */
    @Test
    public void deriveFromMultExpression() throws IOException {
        // example with siunit
        String s = "4.1km * 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km*s)", printType(tc.typeOf(astex)));

        // example with siunit
        s = "4.1km * 12.3";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km)", printType(tc.typeOf(astex)));

        // example with siunit
        s = "4.1km * 12.3km^-1";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("double", printType(tc.typeOf(astex)));

        // example with siunit
        s = "varM * varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("m*s", printType(tc.typeOf(astex)));

        // example with siunit
        s = "varI_M * varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m*s)", printType(tc.typeOf(astex)));

        // example with siunit
        s = "varM * 3.2";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidMultExpression() throws IOException {
        String s = "3 m*true";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0211", getFirstErrorCode());
        }
    }

    /**
     * test correctness of division
     */
    @Test
    public void deriveFromDivideExpression() throws IOException {
        // example with siunit
        String s = "4.1km / 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km/s)", printType(tc.typeOf(astex)));

        // example with siunit
        s = "4.1km / 12.3";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km)", printType(tc.typeOf(astex)));

        s = "4km / 12 m";;
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,km/m)", printType(tc.typeOf(astex)));

        // example with siunit
        s = "varM / varM";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("int", printType(tc.typeOf(astex)));

        // example with siunit
        // TODO fix 3 m/varM
        s = "(3 m)/varM";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("int", printType(tc.typeOf(astex)));

        // example with siunit
        s = "varM / varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("m/s", printType(tc.typeOf(astex)));

        // example with siunit
        s = "3.2 / varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,1/s)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDivideExpression() throws IOException {
        String s = "3 m/true";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0212", getFirstErrorCode());
        }
    }

    /**
     * tests correctness of modulo
     */
    @Test
    public void deriveFromModuloExpression() throws IOException {
        //example with two ints
        String s = "3 m%2m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidModuloExpression() throws IOException {
        String s = "3 m%2";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0214", getFirstErrorCode());
        }
    }

    /**
     * test LessEqualExpression
     */
    @Test
    public void deriveFromLessEqualExpression() throws IOException {
        // example with siunit
        String s = "4.1km <= 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidLessEqualExpression1() throws IOException {
        String s = "4.1km <= 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0215", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidLessEqualExpression2() throws IOException {
        String s = "varS <= varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0215", getFirstErrorCode());
        }
    }

    /**
     * test GreaterEqualExpression
     */
    @Test
    public void deriveFromGreaterEqualExpression() throws IOException {
        // example with siunit
        String s = "4.1km >= 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidGreaterEqualExpression1() throws IOException {
        String s = "4.1km >= 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0216", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidGreaterEqualExpression2() throws IOException {
        String s = "varM >= 3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0216", getFirstErrorCode());
        }
    }

    /**
     * test LessThanExpression
     */
    @Test
    public void deriveFromLessThanExpression() throws IOException {
        // example with siunit
        String s = "4.1km < 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidLessThanExpression1() throws IOException {
        String s = "4.1km < 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0217", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidLessThanExpression2() throws IOException {
        String s = "varS < 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0217", getFirstErrorCode());
        }
    }

    /**
     * test GreaterThanExpression
     */
    @Test
    public void deriveFromGreaterThanExpression() throws IOException {
        // example with siunit
        String s = "4.1km > 12m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidGreaterThanExpression1() throws IOException {
        String s = "4.1km > 12s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0218", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidGreaterThanExpression2() throws IOException {
        String s = "4.1km > varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0218", getFirstErrorCode());
        }
    }

    /**
     * initialize basic scope and a few symbols for testing
     */
    @Override
    @Before
    public void init_basic() {
        super.init_basic();

        // SIUnits
        SymTypeExpression s = SIUnitSymTypeExpressionFactory.createSIUnit("s", scope);
        SymTypeExpression m = SIUnitSymTypeExpressionFactory.createSIUnit("m", scope);
        SymTypeExpression kMe2perH = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/h", scope);
        SymTypeExpression kMe2perHMSe4 = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/(h*ms^4)", scope);

        // Constants
        SymTypeConstant d = SIUnitSymTypeExpressionFactory.createTypeConstant("double");
        SymTypeConstant i = SIUnitSymTypeExpressionFactory.createTypeConstant("int");
        SymTypeConstant l = SIUnitSymTypeExpressionFactory.createTypeConstant("long");

        // SIUnitLiterals
        add2scope(scope, field("varD_S", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, s, scope)));
        add2scope(scope, field("varI_M", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(i, m, scope)));
        add2scope(scope, field("varD_M", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, m, scope)));
        add2scope(scope, field("varL_KMe2perH", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(l, kMe2perH, scope)));
        add2scope(scope, field("varD_KMe2perHmSe4", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, kMe2perHMSe4, scope)));

        add2scope(scope, field("varS", s));
        add2scope(scope, field("varM", m));
        add2scope(scope, field("varKMe2perH", kMe2perH));
        add2scope(scope, field("varKMe2perHMSe4", kMe2perHMSe4));
    }

    /**
     * test EqualsExpression
     */
    @Test
    public void deriveFromEqualsExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //example with two siunit types
        String s = "3 km/s==2 m^2/1 m*s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));

        //example with two siunit types
        s = "varI_M==3 m";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));

        //example with two siunit types
        s = "varM==varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidEqualsExpression1() throws IOException {
        init_basic();

        String s = "varM==3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0219", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidEqualsExpression2() throws IOException {
        init_basic();

        //person1 has the type Person, foo is a boolean
        String s = "varI_M==3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0219", getFirstErrorCode());
        }
    }

    /**
     * test NotEqualsExpression
     */
    @Test
    public void deriveFromNotEqualsExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //example with two siunit types
        String s = "varI_M/varD_S!=5m^2/(m*s)";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));

        //example with two siunit types
        s = "varM!=varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidNotEqualsExpression() throws IOException {
        init_basic();

        String s = "varM!=varI_M";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0220", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidNotEqualsExpression2() throws IOException {
        init_basic();
        //person1 is a Person, foo is a boolean
        String s = "varM!=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0220", getFirstErrorCode());
        }
    }

    /**
     * test BooleanAndOpExpression
     */
    @Test
    public void deriveFromBooleanAndOpExpression() throws IOException {
        String s = "(3 km/h<=4m/s&&5>6)";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidAndOpExpression() throws IOException {
        //only possible with two booleans
        String s = "3 km &&true";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0223", getFirstErrorCode());
        }
    }

    /**
     * test BooleanOrOpExpression
     */
    @Test
    public void deriveFromBooleanOrOpExpression() throws IOException {
        String s = "(3 km/h<=4m/s||5>6)";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("boolean", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidOrOpExpression() throws IOException {
        //only possible with two booleans
        String s = "3 m||true";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0226", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidLogicalNotExpression() throws IOException {
        //only possible with a boolean as inner expression
        String s = "!4 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0228", getFirstErrorCode());
        }
    }

    /**
     * test BracketExpression
     */
    @Test
    public void deriveFromBracketExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //test with siunits
        String s = "(varS*3 s/(2*varM*varM-4 km^2))";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,s^2/km^2)", printType(tc.typeOf(astex)));
    }

    /**
     * test ConditionalExpression
     */
    @Test
    public void deriveFromConditionalExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //test with siunits
        String s = "3<9?varM:varM*varM/varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("m", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidConditionalExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //true and 7 are not of the same type
        String s = "3<4?varS:varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0234", getFirstErrorCode());
        }
    }

    private String getFirstErrorCode() {
        if (Log.getFindings().size() > 0) {
            String firstFinding = Log.getFindings().get(0).getMsg();
            return firstFinding.split(" ")[0];
        }
        return "";
    }

    protected String printType(SymTypeExpression symType) {
        return symType.print();
    }
}
