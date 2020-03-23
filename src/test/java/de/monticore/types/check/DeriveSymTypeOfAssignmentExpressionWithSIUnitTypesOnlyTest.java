package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeriveSymTypeOfAssignmentExpressionWithSIUnitTypesOnlyTest extends DeriveSymTypeOfAssignmentExpressionTest {

    @Before
    @Override
    public void setupForEach() {
        super.setupForEach();
        // SIUnits
        add2scope(scope, field("varS", SIUnitSymTypeExpressionFactory.createSIUnit("s", scope)));
        add2scope(scope, field("varM", SIUnitSymTypeExpressionFactory.createSIUnit("m", scope)));
        add2scope(scope, field("varKMe2perH", SIUnitSymTypeExpressionFactory.createSIUnit("km^2/h", scope)));
        add2scope(scope, field("varKMe2perHMSe4", SIUnitSymTypeExpressionFactory.createSIUnit("km^2/(h*ms^4)", scope)));

        derLit.setScope(scope);
    }

    /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

    /**
     * test IncSuffixExpression
     */
    @Test
    public void deriveFromIncSuffixExpression() throws IOException {
        //example with siunit
        String s = "4[km]++";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test DecSuffixExpression
     */
    @Test
    public void deriveFromDecSuffixExpression() throws IOException {
        //example with siunit
        String s = "4.2[km]--";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test IncPrefixExpression
     */
    @Test
    public void deriveFromIncPrefixExpression() throws IOException {
        //example with siunit
        String s = "++4[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test DecPrefixExpression
     */
    @Test
    public void deriveFromDecPrefixExpression() throws IOException {
        //example with siunit
        String s = "--4.1[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test MinusPrefixExpression
     */
    @Test
    public void deriveFromMinusPrefixExpression() throws IOException {
        //example with siunit
        String s = "-4.3[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test PlusPrefixExpression
     */
    @Test
    public void deriveFromPlusPrefixExpression() throws IOException {
        //example with siunit
        String s = "+4[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test PlusAssignmentExpression
     */
    @Test
    public void deriveFromPlusAssignmentExpression() throws IOException {
        //example with siunit
        String s = "varM+=4[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    @Test (expected = RuntimeException.class)
    public void testInvalidPlusAssignmentExpression() throws IOException {
        //not possible because s = s + m cannot be calculated
        String s = "varS+=4[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test MinusAssignmentExpression
     */
    @Test
    public void deriveFromMinusAssignmentExpression() throws IOException {
        //example with siunit
        String s = "varKMe2perH+=4[m^2/s]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m^2/s", tc.typeOf(astex).print());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidMinusAssignmentExpression() throws IOException {
        //not possible because s = s - m cannot be calculated
        String s = "varS-=4[km]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    /**
     * test MultAssignmentExpression
     */
    @Test
    public void deriveFromMultAssignmentExpression() throws IOException {
        //example with siunit m * s
        String s = "varM*=5 [s]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m*s", tc.typeOf(astex).print());

        //example with siunit m * m^-1
        s = "varM*=5 [m^-1]";
        astex = p.parse_StringExpression(s).get();
        assertEquals("int", tc.typeOf(astex).print());

        //example with siunit m * 5
        s = "varM*=5";
        astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    @Test
    public void testInvalidMultAssignmentExpression() throws IOException {
        //not possible because int = int * (int) String returns a casting error
        String s = "varM*=\"Hello\"";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0178"));
        }
    }

    /**
     * test DivideAssignmentExpression
     */
    @Test
    public void deriveFromDivideAssignmentExpression() throws IOException {
        //example with siunit m / s
        String s = "varM/=5 [s]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m/s", tc.typeOf(astex).print());

        //example with siunit m / m
        s = "varM/=5 [m]";
        astex = p.parse_StringExpression(s).get();
        assertEquals("int", tc.typeOf(astex).print());

        //example with siunit m / 5
        s = "varM/=5";
        astex = p.parse_StringExpression(s).get();
        assertEquals("m", tc.typeOf(astex).print());
    }

    @Test
    public void testInvalidDivideAssignmentExpression() throws IOException {
        //not possible because int = int / (int) String returns a casting error
        String s = "varM/=\"Hello\"";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0179"));
        }
    }

    /**
     * test ModuloAssignmentExpression
     */
    @Test
    public void deriveFromModuloAssignmentExpression() throws IOException {
        //example with int - int
        String s = "varint%=9";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("int", tc.typeOf(astex).print());
        //example with int - float
        s = "foo%=9.8f";
        astex = p.parse_StringExpression(s).get();
        assertEquals("int", tc.typeOf(astex).print());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidModuloAssignmentExpression() throws IOException {
        //not possible because s = s % m cannot be calculated
        String s = "varS%=3[m]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        tc.typeOf(astex);
    }

    @Test
    public void testInvalidAndAssignmentExpression() throws IOException {
        //not possible because int = int & (int) String returns a casting error
        String s = "varM&=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0183"));
        }
    }

    @Test
    public void testInvalidOrAssignmentExpression() throws IOException {
        //not possible because int = int | (int) String returns a casting error
        String s = "varM|=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0184"));
        }
    }

    @Test
    public void testInvalidBinaryXorAssignmentExpression() throws IOException {
        //not possible because int = int ^ (int) String returns a casting error
        String s = "varM^=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0185"));
        }
    }

    @Test
    public void testInvalidDoubleLeftAssignmentExpression() throws IOException {
        //not possible because int = int << (int) String returns a casting error
        String s = "varM<<=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0187"));
        }
    }

    @Test
    public void testInvalidDoubleRightAssignmentExpression() throws IOException {
        //not possible because int = int >> (int) String returns a casting error
        String s = "varM>>=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0186"));
        }
    }

    @Test
    public void testInvalidLogicalRightAssignmentExpression() throws IOException {
        //not possible because int = int >>> (int) String returns a casting error
        String s = "varM>>>=3";
        ASTExpression astex = p.parse_StringExpression(s).get();
        try{
            tc.typeOf(astex);
        }catch(RuntimeException e){
            assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0188"));
        }
    }

    /**
     * test RegularAssignmentExpression
     */
    @Test
    public void deriveFromRegularAssignmentExpression() throws IOException {
        //example with km - km
        String s = "varKMe2perHMSe4 = 3 [mm^2/(h^2*ks^3)]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        assertEquals("m^2/s^5", tc.typeOf(astex).print());
    }


    @Test(expected = RuntimeException.class)
    public void testInvalidRegularAssignmentExpression() throws IOException {
        //not possible because [m^2/s^5] and [m/s^5] are incompatible types
        String s = "varKMe2perHMSe4 = 3 [mm/(h^2*ks^3)]";
        ASTExpression astex = p.parse_StringExpression(s).get();
        tc.typeOf(astex);
    }
}
