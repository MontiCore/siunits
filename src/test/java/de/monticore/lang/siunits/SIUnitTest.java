package de.monticore.lang.siunits;

import com.google.common.collect.Lists;
import de.monticore.lang.literals.testsiunitliterals._parser.TestSIUnitLiteralsParser;
import de.monticore.lang.siunits._ast.ASTSIUnit;
import de.monticore.lang.siunits.utility.UnitPrettyPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTest {

    TestSIUnitLiteralsParser parser = new TestSIUnitLiteralsParser();

    @BeforeClass
    public static void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private void checkSIUnit(String s, String unitAsString, String standardUnitAsString) throws IOException {
        ASTSIUnit lit = parseSIUnit(s);
        String printFromLit = UnitPrettyPrinter.printUnit(lit);
        String printStandardFromLit = UnitPrettyPrinter.printStandardUnit(lit);
        assertEquals(unitAsString, printFromLit);
        assertEquals(standardUnitAsString, printStandardFromLit);
    }

    private ASTSIUnit parseSIUnit(String input) throws IOException {
        Optional<ASTSIUnit> res = parser.parseSIUnit(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    @Test
    public void testSIUnit() {
        try {
            checkSIUnit("kg", "kg", "kg");
            checkSIUnit("cd", "cd", "cd");
            checkSIUnit("m", "m", "m");
            checkSIUnit("s^2", "s^2", "s^2");
            checkSIUnit("s^2/kg", "s^2/kg", "s^2/kg");
            checkSIUnit("s^2/min", "s^2/min", "s");
            checkSIUnit("s^2*kg/(min*m)", "s^2*kg/(min*m)", "s*kg/m");
            checkSIUnit("deg", "deg", "rad");
            checkSIUnit("s^-1", "1/s", "1/s");
            checkSIUnit("1/s", "1/s", "1/s");
            checkSIUnit("°C", "°C", "K");
            checkSIUnit("°F", "°F", "K");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAll() throws IOException {
        String[] prefixes = {"Y","Z","E","P","T","G","M","k","h","da","d","c","m","n","p","f","a","z","y"};
        String[] unitBases = {"m","g","s","A","K","mol","cd","Hz","N","Pa","J","W","C","V","F","Ohm","S","Wb","T","H","lm","lx","Bq","Gy","Sv","kat"};
        String[] officallyAccepted = {"min","h","day","ha","t","Au","Np","B","dB","eV","u"};
        String[] dimensionless = {"deg","rad","sr"};
        String[] fahrenheitCelcius = {"°C", "°F"};


        List<String> combined = new ArrayList<>();
        combined.addAll(Lists.newArrayList(officallyAccepted));
        combined.addAll(Lists.newArrayList(dimensionless));
        combined.addAll(Lists.newArrayList(fahrenheitCelcius));

        for (String unitBase: unitBases) {
            combined.add(unitBase);
            for (String prefix: prefixes) {
                combined.add(prefix + unitBase);
            }
        }

        List<String> faultyUnits = new ArrayList<>();

        for (String s: combined) {
            if(!isValid(s, s)) {
                faultyUnits.add(s);
            }
        }

        if (faultyUnits.size() > 0) {
            Log.init();
            Log.error("Error with: " + faultyUnits.toString());
        }
    }

    private boolean isValid(String s, String expected) {
        expected = expected.replace("Mg", "t").
                            replace("MBq", "Rd").
                            replace("cGy", "rd").
                            replace("cSv", "rem");
        Optional<ASTSIUnit> res = null;
        try {
            res = parser.parseSIUnit(new StringReader(s));
        } catch (IOException e) {
            return false;
        }

        if (res.isPresent()) {
            String print = UnitPrettyPrinter.printUnit(res.get());
            if (expected.equals(print)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
