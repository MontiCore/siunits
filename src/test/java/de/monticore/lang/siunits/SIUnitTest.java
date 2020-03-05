package de.monticore.lang.siunits;

import de.monticore.lang.literals.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.lang.siunits.siunits._ast.ASTSIUnit;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTest {

    @BeforeClass
    public static void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    private void checkSIUnit(String u, String s) throws IOException {
        ASTSIUnit lit = parseSIUnit(s);
        String print = lit.toString();
        assertEquals(u, print);
    }

    private ASTSIUnit parseSIUnit(String input) throws IOException {
        TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();
        Optional<ASTSIUnit> res = parser.parseSIUnit(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    @Test
    public void testSIUnit() {
        try {
            checkSIUnit("kg", "kg");
            checkSIUnit("cd", "cd");
            checkSIUnit("m", "m");
            checkSIUnit("s^2", "s^2");
            checkSIUnit("s^2/kg", "s^2/kg");
            checkSIUnit("s^2/min", "s^2/min");
            checkSIUnit("s^2*kg/(min*m)", "s^2*kg/(min*m)");
            checkSIUnit("°", "deg");
            checkSIUnit("1/s", "s^-1");
//            checkSIUnit("°", "°");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAll() throws IOException {
        String[] unitBases = {"m","g","s","A","K","mol","cd","Hz","N","Pa","J","W","C","V","F","Ohm","S","Wb","T","H","lm","lx","Bq","Gy","Sv","kat"};
        String[] officallyAccepted = {"min","h","day","ha","t","au","Au","Np","B","db","eV","u"};
        String[] dimensionless = {"deg","rad","sr"};

        for (String s: unitBases) {
            checkSIUnit(s.replace("Ohm","Ω"), s);
        }
        for (String s: officallyAccepted) {
            checkSIUnit(s, s);
        }
        for (String s: dimensionless) {
            checkSIUnit(s, s);
        }
    }
}
