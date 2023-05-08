/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunitliterals;

import de.monticore.literals.mccommonliterals._ast.ASTBasicFloatLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.siunitliterals.utility.SIUnitLiteralDecoder;
import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.testsiunitliterals.TestSIUnitLiteralsMill;
import de.monticore.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitLiteralsTest {

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
        TestSIUnitLiteralsMill.reset();
        TestSIUnitLiteralsMill.init();
    }

    private void checkSIUnitLiteral(String s, double number, String unit, double value,
                                    String baseUnit) throws IOException {
        ASTLiteral lit = parseLiteral(s);
        assertTrue(lit instanceof ASTSIUnitLiteral);

        String unitAsString = SIUnitsPrettyPrinter.prettyprint(((ASTSIUnitLiteral) lit).getSIUnit());

        assertEquals(number, SIUnitLiteralDecoder.doubleOf((ASTSIUnitLiteral) lit), 0.0001);
        assertEquals(value, SIUnitLiteralDecoder.valueOf((ASTSIUnitLiteral) lit), 0.0001);
        assertEquals(unit, unitAsString);
        assertEquals(baseUnit, UnitPrettyPrinter.printBaseUnit(unitAsString));
    }

    private void checkLongLiteral(String s) throws IOException {
        ASTLiteral lit = parseLiteral(s);
        assertTrue(lit instanceof ASTBasicLongLiteral);
    }

    private void checkFloatLiteral(String s) throws IOException {
        ASTLiteral lit = parseLiteral(s);
        assertTrue(lit instanceof ASTBasicFloatLiteral);
    }

    private ASTLiteral parseLiteral(String input) throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    @Test
    public void testSIUnitLiterals() {
        try {
            checkSIUnitLiteral("30.4 s^3/kgm^2", 30.4, "s^3/kgm^2", 30.4, "s^3/(kg*m^2)");
            checkSIUnitLiteral("30kg", 30, "kg", 30, "kg");
            checkSIUnitLiteral("30km", 30, "km", 30000, "m");
            checkSIUnitLiteral("30g", 30, "g", 0.03, "kg");
            checkSIUnitLiteral("30 kg", 30, "kg", 30, "kg");
            checkSIUnitLiteral("30.4 kg", 30.4, "kg", 30.4, "kg");
            checkSIUnitLiteral("30.4kg", 30.4, "kg", 30.4, "kg");
            checkSIUnitLiteral("30 kg/m", 30, "kg/m", 30, "kg/m");
            checkSIUnitLiteral("30.4 kg^2", 30.4, "kg^2", 30.4, "kg^2");
            checkSIUnitLiteral("30 g^2", 30, "g^2", 0.00003, "kg^2");
            checkSIUnitLiteral("30 km^2", 30, "km^2", 30000000, "m^2");
            checkSIUnitLiteral("30.4kg^2", 30.4, "kg^2", 30.4, "kg^2");
            checkSIUnitLiteral("30.4s^3/kgm^2", 30.4, "s^3/kgm^2", 30.4, "s^3/(kg*m^2)");
            checkSIUnitLiteral("1 h/min", 1, "h/min", 60, "1");
            checkSIUnitLiteral("30.4 rad", 30.4, "rad", 30.4, "1");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWithFOrL() {
        try {
            checkSIUnitLiteral("30.2f km", 30.2, "km", 30200, "m");
            checkSIUnitLiteral("30.4F F", 30.4, "F", 30.4, "A^2*s^4/(kg*m^2)");
            checkSIUnitLiteral("30.4F kg", 30.4, "kg", 30.4, "kg");
            checkSIUnitLiteral("30L F", 30, "F", 30, "A^2*s^4/(kg*m^2)");
            checkSIUnitLiteral("30.2 F", 30.2, "F", 30.2, "A^2*s^4/(kg*m^2)");
            checkSIUnitLiteral("30F", 30, "F", 30, "A^2*s^4/(kg*m^2)");
            checkSIUnitLiteral("30 L", 30, "L", 0.03, "m^3");
            checkSIUnitLiteral("30.2L", 30.2, "L", 0.0302, "m^3");
            checkSIUnitLiteral("30 l", 30, "l", 0.03, "m^3");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLongLiteral() {
        try {
            checkLongLiteral("30L");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFloatLiteral() {
        try {
            checkFloatLiteral("30.2F");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInvalid1() throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3.2F"));
        assertTrue(res.isPresent());
        assertTrue(res.get() instanceof ASTBasicFloatLiteral);
        assertFalse(res.get() instanceof ASTSIUnitLiteral);
    }

    @Test
    public void testInvalid2() throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3L"));
        assertTrue(res.isPresent());
        assertTrue(res.get() instanceof ASTBasicLongLiteral);
        assertFalse(res.get() instanceof ASTSIUnitLiteral);
    }

    @Test
    public void testInvalid3() throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3l"));
        assertTrue(res.isPresent());
        assertTrue(res.get() instanceof ASTBasicLongLiteral);
        assertFalse(res.get() instanceof ASTSIUnitLiteral);
    }

    @Test
    public void testInvalid4() throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3 var"));
        assertFalse(res.isPresent());
    }
}
