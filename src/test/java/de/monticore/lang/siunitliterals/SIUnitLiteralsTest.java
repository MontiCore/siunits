package de.monticore.lang.siunitliterals;

import de.monticore.lang.literals.siunitliterals.prettyprint.SIUnitLiteralsPrettyPrinter;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitLiteralsTest {

    @BeforeClass
    public static void init() {
        Log.init();
        Log.enableFailQuick(true);
    }

    private void checkSIUnitLiteral(double number, String unit, String s) throws IOException {
        ASTLiteral lit = parseLiteral(s);
        assertTrue(lit instanceof ASTSIUnitLiteral);
        assertEquals(number, ((ASTSIUnitLiteral) lit).getNumber(), 0.00001);
        assertEquals(unit, ((ASTSIUnitLiteral) lit).getUnit());
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
            checkSIUnitLiteral(30, "kg", "30kg");
            checkSIUnitLiteral(30, "kg", "30 kg");
            checkSIUnitLiteral(30.4, "kg", "30.4 kg");
            checkSIUnitLiteral(30.4, "kg", "30.4kg");
            checkSIUnitLiteral(30.4, "kg^2", "30.4 kg^2");
            checkSIUnitLiteral(30.4, "kg^2", "30.4kg^2");
            checkSIUnitLiteral(30.4, "s^3/m^2*kg", "30.4 s^3/m^2*kg");
            checkSIUnitLiteral(30.4, "s^3/m^2*kg", "30.4s^3/m^2*kg");
            checkSIUnitLiteral(30.4, "deg", "30.4 deg");
            checkSIUnitLiteral(30.4, "min/h", "30.4 min/h");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLongLiteral() {
        try {
            ASTLiteral literal = parseLiteral("30.2F");
            assert (literal instanceof ASTSIUnitLiteral);
            assert (((ASTSIUnitLiteral) literal).getNum() instanceof ASTBasicLongLiteral);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void checkPrettyPrint(String expected, String s) throws IOException {
        ASTSIUnitLiteral lit = (ASTSIUnitLiteral) parseLiteral(s);
        SIUnitLiteralsPrettyPrinter prettyPrinter = new SIUnitLiteralsPrettyPrinter(new IndentPrinter());
        assertEquals(expected, prettyPrinter.prettyprint(lit));
    }

    @Test
    public void testSIUnitLiteralsPrettyPrint() {
        try {
            checkPrettyPrint("3 kg", "3 kg");
            checkPrettyPrint("3 kg", "3kg");
            checkPrettyPrint("3 kg/s", "3kg/s");
            checkPrettyPrint("3 kg/s", "3 kg/s");
            checkPrettyPrint("3 kg/s^2", "3 kg/s^2");
            checkPrettyPrint("3 kg/s^2*m", "3kg/s^2*m");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
