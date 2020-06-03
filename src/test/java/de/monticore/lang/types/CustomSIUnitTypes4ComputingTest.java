package de.monticore.lang.types;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._parser.TestSIJavaWithCustomTypesParser;
import de.monticore.lang.types.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.lang.types.prettyprint.CustomSIUnitTypes4ComputingPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomSIUnitTypes4ComputingTest {

    TestSIJavaWithCustomTypesParser parser = new TestSIJavaWithCustomTypesParser();
    CustomSIUnitTypes4ComputingPrettyPrinter prettyPrinter;

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        test("double <km/h>", "double  <km/h>", false);
        test("long <km/h>", "long   <km/h>", false);
        test("int <h/km>", "int <h/km>", false);
        test("void <km>", "void <km>", true);
        test("km <int>", "km <int>", true);
    }

    private void test(String control, String s, boolean expectedParseError) {
        Optional<ASTCustomSIUnitType4Computing> astOpt = Optional.empty();
        prettyPrinter = new CustomSIUnitTypes4ComputingPrettyPrinter(new IndentPrinter());
        try {
            astOpt = parser.parseCustomSIUnitType4Computing(new StringReader(s));
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
