package de.monticore.lang.siunitliterals;

import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._parser.SIUnitLiteralsParser;
import de.monticore.literals.mccommonliterals._ast.ASTBasicFloatLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitLiteralsTest {

    @BeforeClass
    public static void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private void checkSIUnitLiteral(String s) throws IOException {
        ASTLiteral lit = parseLiteral(s);
        assertTrue(lit instanceof ASTSIUnitLiteral);
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
        SIUnitLiteralsParser parser = new SIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    @Test
    public void testSIUnitLiterals() {
        try {
            checkSIUnitLiteral("30kg");
            checkSIUnitLiteral("30 kg");
            checkSIUnitLiteral("30.4 kg");
            checkSIUnitLiteral("30.4kg");
            checkSIUnitLiteral("30 kg/m");
            checkSIUnitLiteral("30.4 kg^2");
            checkSIUnitLiteral("30.4kg^2");
            checkSIUnitLiteral("30.4 s^3/m^2*kg");
            checkSIUnitLiteral("30.4 s^3/m^2*kg");
            checkSIUnitLiteral("30.4 deg");
            checkSIUnitLiteral("30.4 min/h");
            checkSIUnitLiteral("3.2 l");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWithFloatOrLongLiteral() {
        try {
            checkSIUnitLiteral("30.2F km");
//            checkSIUnitLiteral("30.2F ~ F");
            checkSIUnitLiteral("30.4F F");
            checkSIUnitLiteral("30.4F kg");
            checkSIUnitLiteral("30L F");
            checkSIUnitLiteral("30 F");
            checkSIUnitLiteral("30 L");
            checkSIUnitLiteral("30 l");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Ignore
    @Test
    public void testWithDelimiter() {
        try {
            checkSIUnitLiteral("30.4F ~ F");
            checkSIUnitLiteral("30.2 ~ F");
            checkSIUnitLiteral("30L ~ L");

            checkSIUnitLiteral("30.4~ F");
            checkSIUnitLiteral("30.4 ~F");
            checkSIUnitLiteral("30.4~F");
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
        SIUnitLiteralsParser parser = new SIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3.2F"));
        assertTrue(res.isPresent());
        assertFalse(res.get() instanceof ASTSIUnitLiteral);
    }

    @Test
    public void testInvalid2() throws IOException {
        SIUnitLiteralsParser parser = new SIUnitLiteralsParser();
        Optional<ASTLiteral> res = parser.parseLiteral(new StringReader("3L"));
        assertTrue(res.isPresent());
        assertFalse(res.get() instanceof ASTSIUnitLiteral);
    }
}
