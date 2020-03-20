package de.monticore.types;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.testsijava.testsijava._ast.ASTCustomType;
import de.monticore.testsijava.testsijavasiunittypesonly._parser.TestSIJavaSIUnitTypesOnlyParser;
import de.monticore.types.prettyprint.SIUnitTypesPrettyPrinter;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTypesTest {

    TestSIJavaSIUnitTypesOnlyParser parser = new TestSIJavaSIUnitTypesOnlyParser();
    SIUnitTypesPrettyPrinter prettyPrinter = new SIUnitTypesPrettyPrinter(new IndentPrinter());

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
        Optional<ASTSIUnitType> astOpt = Optional.empty();
        prettyPrinter = new SIUnitTypesPrettyPrinter(new IndentPrinter());
        try {
            astOpt = parser.parseSIUnitType(new StringReader(s));
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
