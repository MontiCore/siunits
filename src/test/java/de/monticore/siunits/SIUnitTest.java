/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits;

import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits._parser.SIUnitsParser;
import de.monticore.siunits.utility.SIUnitConstants;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTest {

    SIUnitsParser parser = new SIUnitsParser();

    @BeforeClass
    public static void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private void checkSIUnit(String s, String unitAsString, String standardUnitAsString) throws IOException {
        ASTSIUnit lit = parseSIUnit(s);
        String printFromLit = UnitPrettyPrinter.printUnit(lit);
        String printStandardFromLit = UnitPrettyPrinter.printBaseUnit(lit);
        assertEquals(unitAsString, printFromLit);
        assertEquals(standardUnitAsString, printStandardFromLit);
    }

    private ASTSIUnit parseSIUnit(String input) throws IOException {
        Optional<ASTSIUnit> res = parser.parseSIUnit(new StringReader(input));
        assertTrue(res.isPresent());
        return res.get();
    }

    private void checkInvalid(String input) throws IOException {
        Optional<ASTSIUnit> res = parser.parseSIUnit(new StringReader(input));
        assertFalse(res.isPresent());
    }

    @Ignore
    @Test
    public void testNewCompoundUnits() {
        try {
            checkSIUnit("(kV^2A/VA*ks)^3","kV^6*ks^3/V^3","kg^3*m^6/(A^3*s^6)");
            parseSIUnit("s^-1");
            parseSIUnit("(s)");
            parseSIUnit("(s)^-1");
            parseSIUnit("kVA");
            parseSIUnit("kV^2A^3");
            parseSIUnit("kVAh");
            parseSIUnit("kVAh/°C");
            checkInvalid("khA");
            checkInvalid("VkA");

            checkSIUnit("s^-1","1/s","1/s");
            checkSIUnit("(s)","s","s");
            checkSIUnit("(s)^-1","1/s","1/s");
            checkSIUnit("kVA","A*kV","kg*m^2/s^3");
            checkSIUnit("kV^2A^3","kV^2*A^3","kV^2*A^3");
            checkSIUnit("kVAh","kV*A*h","kV*A*h");
            checkSIUnit("kVAh/°C","kV*A*h/°C","kV*A*h/°C");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSIUnit() {
        try {
            checkSIUnit("°C", "°C", "K");
            checkSIUnit("kg", "kg", "kg");
            checkSIUnit("cd", "cd", "cd");
            checkSIUnit("m", "m", "m");
            checkSIUnit("s^2", "s^2", "s^2");
            checkSIUnit("s^2/kg", "s^2/kg", "s^2/kg");
            checkSIUnit("s^2/min", "s^2/min", "s");
            checkSIUnit("s^2*kg/(min*m)", "kg*s^2/(m*min)", "kg*s/m");
            checkSIUnit("deg", "deg", "1");
            checkSIUnit("s^-1", "1/s", "1/s");
            checkSIUnit("1/s", "1/s", "1/s");
            checkSIUnit("°F", "°F", "K");
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOhmAndMu() throws IOException {
        checkSIUnit("Ω", "Ω", "kg*m^2/(A^2*s^3)");
        checkSIUnit("kΩ", "kΩ", "kg*m^2/(A^2*s^3)");
        checkSIUnit("µg", "µg", "kg");
        checkSIUnit("µΩ", "µΩ", "kg*m^2/(A^2*s^3)");

        checkInvalid("k Ω");
        checkInvalid("µ g");
        checkInvalid("µ Ω");
    }

    @Test
    public void testAll() throws IOException {
        List<String> faultyUnits = new ArrayList<>();

        for (String s: SIUnitConstants.getAllUnits()) {
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
        expected = expected.replace("Mg", "t");
        Optional<ASTSIUnit> res = null;
        try {
            res = parser.parseSIUnit(new StringReader(s));
        } catch (IOException e) {
            return false;
        }

        if (res.isPresent()) {
            String print = "";
            try {
                print = UnitPrettyPrinter.printUnit(res.get());
            } catch (Exception e) {
                return false;
            }
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
