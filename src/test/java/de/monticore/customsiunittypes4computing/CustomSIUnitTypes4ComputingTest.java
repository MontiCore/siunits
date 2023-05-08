/* (c) https://github.com/MontiCore/monticore */

package de.monticore.customsiunittypes4computing;

import de.monticore.customsiunittypes4computing._ast.ASTCustomSIUnitType4Computing;
import de.monticore.customsiunittypes4computing.prettyprint.CustomSIUnitTypes4ComputingPrettyPrinter;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._parser.TestSIJavaWithCustomTypesParser;
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
        TestSIJavaWithCustomTypesMill.reset();
        TestSIJavaWithCustomTypesMill.init();
    }

    @Test
    public void test() {
        test("double in km/h", "double in km/h", false);
        test("long in km/h", "long in  km/h", false);
        test("int in h/km", "int in h/km", false);
        test("void in km", "void in km", true);
        test("km in int", "km in int", true);
    }

    private void test(String control, String s, boolean expectedParseError) {
        Optional<ASTCustomSIUnitType4Computing> astOpt = Optional.empty();
        try {
            astOpt = parser.parseCustomSIUnitType4Computing(new StringReader(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (expectedParseError) {
            assertFalse("Should not be able to parse " + s, astOpt.isPresent());
        } else {
            assertTrue(astOpt.isPresent());
            assertEquals(control, CustomSIUnitTypes4ComputingPrettyPrinter.prettyprint(astOpt.get()));
        }
    }
}
