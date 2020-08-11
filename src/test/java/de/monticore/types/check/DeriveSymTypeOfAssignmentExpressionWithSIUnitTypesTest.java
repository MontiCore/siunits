/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeriveSymTypeOfAssignmentExpressionWithSIUnitTypesTest extends DeriveSymTypeOfAssignmentExpressionTest {

    @Before
    @Override
    public void setupForEach() {
        super.setupForEach();
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

    /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

    /**
     * test IncSuffixExpression
     */
    @Test
    public void deriveFromIncSuffixExpression() throws IOException {
        //example with siunit literal
        String s = "4km++";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidIncSuffixExpression() throws IOException {
        //not possible because ++ is not defined for siunits
        String s = "varS++";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0170", getFirstErrorCode());
        }
    }

    /**
     * test DecSuffixExpression
     */
    @Test
    public void deriveFromDecSuffixExpression() throws IOException {
        //example with siunit literal
        String s = "4.2km--";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDecSuffixExpression() throws IOException {
        //not possible because -- is not defined for siunits
        String s = "varS--";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0171", getFirstErrorCode());
        }
    }

    /**
     * test IncPrefixExpression
     */
    @Test
    public void deriveFromIncPrefixExpression() throws IOException {
        //example with siunit literal
        String s = "++4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidIncPrefixExpression() throws IOException {
        //not possible because ++ is not defined for siunits
        String s = "++varS";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0172", getFirstErrorCode());
        }
    }

    /**
     * test DecPrefixExpression
     */
    @Test
    public void deriveFromDecPrefixExpression() throws IOException {
        //example with siunit literal
        String s = "--4.1km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDecPrefixExpression() throws IOException {
        //not possible because -- is not defined for siunits
        String s = "--varS";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0173", getFirstErrorCode());
        }
    }

    /**
     * test MinusPrefixExpression
     */
    @Test
    public void deriveFromMinusPrefixExpression() throws IOException {
        //example with siunit literal
        String s = "-4.3km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidMinusPrefixExpression() throws IOException {
        //not possible because - is not defined for siunits
        String s = "-varS";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0175", getFirstErrorCode());
        }
    }

    /**
     * test PlusPrefixExpression
     */
    @Test
    public void deriveFromPlusPrefixExpression() throws IOException {
        //example with siunit literal
        String s = "+4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,km)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidPlusPrefixExpression() throws IOException {
        //not possible because + is not defined for siunits
        String s = "+varS";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0174", getFirstErrorCode());
        }
    }

    /**
     * test PlusAssignmentExpression
     */
    @Test
    public void deriveFromPlusAssignmentExpression() throws IOException {
        //example with double m += int m
        String s = "varD_M+=4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));

        //example with int m += double m
        s = "varI_M+=4.2km";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));

        //example with double m += int m
        s = "varI_M+=4km";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
    }


    @Test
    public void testInvalidPlusAssignmentExpression1() throws IOException {
        //not possible because s = s + m cannot be calculated
        String s = "varD_S+=4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0176", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidPlusAssignmentExpression2() throws IOException {
        //not possible because += is not defined for siunits
        String s = "varS+=4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0176", getFirstErrorCode());
        }
    }

    /**
     * test MinusAssignmentExpression
     */
    @Test
    public void deriveFromMinusAssignmentExpression() throws IOException {
        //example with siunit literal long m^2/s -= int m^2/s
        String s = "varL_KMe2perH-=4m^2/s";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(long,km^2/h)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidMinusAssignmentExpression1() throws IOException {
        //not possible because s = s - m cannot be calculated
        String s = "varD_S-=4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0177", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidMinusAssignmentExpression2() throws IOException {
        //not possible because -= is not defined for siunits
        String s = "varS-=4km";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0177", getFirstErrorCode());
        }
    }

    /**
     * test MultAssignmentExpression
     */
    @Test
    public void deriveFromMultAssignmentExpression() throws IOException {
        //example with siunit literal int m * double
        String s = "varI_M*=5.3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));

        //example with siunit literal double m * double
        s = "varD_M*=5.3";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));

        //example with siunit literal double m * int
        s = "varD_M*=5";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidMultAssignmentExpression1() throws IOException {
        //not possible because int m = int m * (int m) String returns a casting error
        String s = "varI_M*=\"Hello\"";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0178", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidMultAssignmentExpression2() throws IOException {
        //not possible because m != m * m
        String s = "varI_M*=varI_M";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0178", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidMultAssignmentExpression3() throws IOException {
        //not possible because m != m * m
        String s = "varM*=varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0178", getFirstErrorCode());
        }
    }

    /**
     * test DivideAssignmentExpression
     */
    @Test
    public void deriveFromDivideAssignmentExpression() throws IOException {
        //example with siunit literal int m / int
        String s = "varI_M/=5";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));

        //example with siunit literal int m / double
        s = "varI_M/=5.2";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));

        //example with siunit literal double m / int
        s = "varD_M/=5";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDivideAssignmentExpression1() throws IOException {
        //not possible because int m = int m / (int m) String returns a casting error
        String s = "varI_M/=\"Hello\"";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0179", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidDivideAssignmentExpression2() throws IOException {
        //not possible because m != m / m
        String s = "varI_M/=varI_M";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0179", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidDivideAssignmentExpression3() throws IOException {
        //not possible because m != m / m
        String s = "varM/=varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0179", getFirstErrorCode());
        }
    }

    /**
     * test ModuloAssignmentExpression
     */
    @Test
    public void deriveFromModuloAssignmentExpression() throws IOException {
        //example with siunit literal int m %= double m
        String s = "varI_M%=9.2 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));

        //example with siunit literal double m %= int m
        s = "varD_M%=9 m";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidModuloAssignmentExpression1() throws IOException {
        //not possible because s = s % m cannot be calculated
        String s = "varD_S%=3m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0189", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidModuloAssignmentExpression2() throws IOException {
        //not possible because s = s % int cannot be calculated
        String s = "varD_S%=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0189", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidModuloAssignmentExpression3() throws IOException {
        //not possible because s = s % int cannot be calculated
        String s = "varS%=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0189", getFirstErrorCode());
        }
    }

    @Test
    public void deriveFromAndAssignmentExpression() throws IOException {
        //example with int m &= int
        String s = "varI_M&=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with long m &= int
        s = "varL_KMe2perH&=9";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(long,km^2/h)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidAndAssignmentExpression1() throws IOException {
        //not possible because int m = int m & int m cannot be calculated
        String s = "varI_M&=varI_M";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0183", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidAndAssignmentExpression2() throws IOException {
        //not possible because int m = int m & int m cannot be calculated
        String s = "varM&=2";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0183", getFirstErrorCode());
        }
    }

    @Test
    public void deriveFromOrAssignmentExpression() throws IOException {
        //example with int m &= int
        String s = "varI_M|=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with long m &= int
        s = "varL_KMe2perH|=9";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(long,km^2/h)", printType(tc.typeOf(astex)));
    }


    @Test
    public void testInvalidOrAssignmentExpression1() throws IOException {
        //not possible because int m = int m | int m cannot be calculated
        String s = "varI_M|=3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0184", getFirstErrorCode());
        }
    }


    @Test
    public void testInvalidOrAssignmentExpression2() throws IOException {
        //not possible because int m = int m | int m cannot be calculated
        String s = "varM|=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0184", getFirstErrorCode());
        }
    }

    @Test
    public void deriveFromBinaryXorAssignmentExpression() throws IOException {
        //example with int m &= int
        String s = "varI_M^=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with long m &= int
        s = "varL_KMe2perH^=9";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(long,km^2/h)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidBinaryXorAssignmentExpression1() throws IOException {
        //not possible because int m = int m ^ int m cannot be calculated
        String s = "varI_M^=3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0185", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidBinaryXorAssignmentExpression2() throws IOException {
        //not possible because int m = int m ^ int m cannot be calculated
        String s = "varM^=3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0185", getFirstErrorCode());
        }
    }

    @Test
    public void deriveFromDoubleLeftAssignmentExpression() throws IOException {
        //example with int m - int
        String s = "varI_M<<=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with int m - char
        s = "varI_M<<=\'c\'";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDoubleLeftAssignmentExpression1() throws IOException {
        //not possible because int m = int m << int m cannot be calculated
        String s = "varI_M<<=3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0187", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidDoubleLeftAssignmentExpression2() throws IOException {
        //not possible because int m = int m << int m cannot be calculated
        String s = "varM<<=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0187", getFirstErrorCode());
        }
    }

    /**
     * test DoubleRightAssignmentExpression
     */
    @Test
    public void deriveFromDoubleRightAssignmentExpression() throws IOException {
        //example with int m - int
        String s = "varI_M>>=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with int m - char
        s = "varI_M>>=\'c\'";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidDoubleRightAssignmentExpression1() throws IOException {
        //not possible because int m = int m >> int m cannot be calculated
        String s = "varI_M>>=3m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0186", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidDoubleRightAssignmentExpression2() throws IOException {
        //not possible because int m = int m >> int m cannot be calculated
        String s = "varM>>=varM";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0186", getFirstErrorCode());
        }
    }

    /**
     * test LogicalRightAssignmentExpression
     */
    @Test
    public void deriveFromLogicalRightAssignmentExpression() throws IOException {
        //example with int - int
        String s = "varI_M>>>=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
        //example with char - char
        s = "varI_M>>>=\'3\'";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(int,m)", printType(tc.typeOf(astex)));
    }

    @Test
    public void testInvalidLogicalRightAssignmentExpression1() throws IOException {
        //not possible because int m = int m >>> int m cannot be calculated
        String s = "varI_M>>>=varI_M";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0188", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidLogicalRightAssignmentExpression2() throws IOException {
        //not possible because int m = int m >>> int m cannot be calculated
        String s = "varM>>>=2";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0188", getFirstErrorCode());
        }
    }

    /**
     * test RegularAssignmentExpression
     */
    @Test
    public void deriveFromRegularAssignmentExpression() throws IOException {
        String a = "3 mm^2/ks^3h^2";
        ASTExpression astex2 = p.parseExpression(new StringReader(a)).get();
        astex2.toString();
        //example with km - km
        String s = "varD_KMe2perHmSe4 = 3 mm^2/ks^3h^2";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("(double,km^2/(h*ms^4))", printType(tc.typeOf(astex)));


        //example with m^2/s
        s = "varKMe2perH = varM*varM/varS";
        astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        assertEquals("km^2/h", printType(tc.typeOf(astex)));
    }


    @Test
    public void testInvalidRegularAssignmentExpression1() throws IOException {
        //not possible because m^2/s^5 and m/s^5 are incompatible types
        String s = "varD_KMe2perHmSe4 = 3 mm/(h^2*ks^3)";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0182", getFirstErrorCode());
        }
    }

    @Test
    public void testInvalidRegularAssignmentExpression2() throws IOException {
        //not possible because m^2/s^5 and m/s^5 are incompatible types
        String s = "varM = 3 m";
        ASTExpression astex = p.parse_StringExpression(s).get();
        astex.accept(flatExpressionScopeSetter);
        try {
            tc.typeOf(astex);
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("0xA0182", getFirstErrorCode());
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
