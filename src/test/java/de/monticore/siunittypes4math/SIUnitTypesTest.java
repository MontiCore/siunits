/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.siunittypes4math;

import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.siunittypes4computing.prettyprint.SIUnitTypes4MathPrettyPrinter;
import de.monticore.siunittypes4math._ast.ASTSIUnitType4Math;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTypesTest {

    TestSIJavaParser parser = new TestSIJavaParser();
    SIUnitTypes4MathPrettyPrinter prettyPrinter = new SIUnitTypes4MathPrettyPrinter(new IndentPrinter());

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        test("km/h", "km/h", false);
        test("km/int", "km/int", true);
        test("int", "int", true);
    }

    private void test(String control, String s, boolean expectedParseError) {
        Optional<ASTSIUnitType4Math> astOpt = Optional.empty();
        prettyPrinter = new SIUnitTypes4MathPrettyPrinter(new IndentPrinter());
        try {
            astOpt = parser.parseSIUnitType4Math(new StringReader(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (expectedParseError) {
            assertFalse("Should not be able to parse " + s, astOpt.isPresent());
        } else {
            assertTrue(astOpt.isPresent());
            astOpt.get().accept(prettyPrinter);
            assertEquals(control, prettyPrinter.getPrinter().getContent());
        }
    }
}
