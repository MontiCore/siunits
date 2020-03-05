package de.monticore.lang.siunitliterals;

import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.lang.literals.prettyprint.SIUnitLiteralsPrettyPrinter;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteral;
import de.monticore.lang.literals.siunitliterals._ast.ASTSIUnitLiteralsNode;
import de.monticore.lang.literals.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SIUnitLiteralsTest {

    @BeforeClass
    public static void init() {
        LogStub.init();
        Log.enableFailQuick(true);
    }

    /*
     nimm auch hier Analog zu der Änderung in der Grammatik die parseSignedLiteral() Methode zum parsen
     */
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
            checkSIUnitLiteral(30, "kg", "30[kg]");
            checkSIUnitLiteral(30, "kg", "30 [kg]");
            checkSIUnitLiteral(30.4, "kg", "30.4 [kg]");
            checkSIUnitLiteral(30.4, "kg", "30.4[kg]");
            checkSIUnitLiteral(30.4, "kg^2", "30.4 [kg^2]");
            checkSIUnitLiteral(30.4, "kg^2", "30.4[kg^2]");
            checkSIUnitLiteral(30.4, "s^3/m^2*kg", "30.4 [s^3/m^2*kg]");
            checkSIUnitLiteral(30.4, "s^3/m^2*kg", "30.4[s^3/m^2*kg]");
            checkSIUnitLiteral(30.4, "deg", "30.4 [deg]");
            checkSIUnitLiteral(30.4, "min/h", "30.4 [min/h]");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void checkPrettyPrint(String expected, String s) throws IOException {
        ASTSIUnitLiteral lit = (ASTSIUnitLiteral) parseLiteral(s);
        SIUnitLiteralsPrettyPrinter prettyPrinter = new SIUnitLiteralsPrettyPrinter(new IndentPrinter());
        assertEquals(expected, prettyPrinter.prettyprint((ASTSIUnitLiteralsNode) lit));
    }

    @Test
    public void testSIUnitLiteralsPrettyPrint() {
        try {
            checkPrettyPrint("3 kg", "3 [kg]");
            checkPrettyPrint("3 kg", "3[kg]");
            checkPrettyPrint("3 kg/s", "3[kg/s]");
            checkPrettyPrint("3 kg/s", "3 [kg/s]");
            checkPrettyPrint("3 kg/s^2", "3 [kg/s^2]");
            checkPrettyPrint("3 kg/s^2*m", "3[kg/s^2*m]");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
