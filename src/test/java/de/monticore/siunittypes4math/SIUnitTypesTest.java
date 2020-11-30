/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4math;

import de.monticore.siunittypes4math._ast.ASTSIUnitType;
import de.monticore.siunittypes4math.prettyprint.SIUnitTypes4MathPrettyPrinter;
import de.monticore.testsiunittypes._parser.TestSIUnitTypesParser;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTypesTest {

    TestSIUnitTypesParser parser = new TestSIUnitTypesParser();

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void testSIUnitTypes() {
        testSIUnitType("km/h", "km/h", false);
        testSIUnitType("km/int", "km/int", true);
        testSIUnitType("int", "int", true);
    }

    @Test
    public void testMCTypes() {
        testMCType("s");
        testMCType("km/h");
        testMCType("int");
        testMCType("double");
        testMCType("D");
        testMCType("S1");
    }

    private void testSIUnitType(String control, String s, boolean expectedParseError) {
        Optional<ASTSIUnitType> astOpt = Optional.empty();
        try {
            astOpt = parser.parseSIUnitType(new StringReader(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (expectedParseError) {
            assertFalse("Should not be able to parse " + s, astOpt.isPresent());
        } else {
            assertTrue(astOpt.isPresent());
            assertEquals(control, SIUnitTypes4MathPrettyPrinter.prettyprint(astOpt.get()));
        }
    }

    private void testMCType(String s) {
        Optional<ASTMCType> astOpt = Optional.empty();
        try {
            astOpt = parser.parseMCType(new StringReader(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(astOpt.isPresent());
    }
}
