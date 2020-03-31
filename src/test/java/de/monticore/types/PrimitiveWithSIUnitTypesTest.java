package de.monticore.types;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.types.prettyprint.PrimitiveWithSIUnitTypesPrettyPrinter;
import de.monticore.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class PrimitiveWithSIUnitTypesTest {

    TestSIJavaParser parser = new TestSIJavaParser();
    PrimitiveWithSIUnitTypesPrettyPrinter prettyPrinter;

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        test("<double km/h>", "<double  km/h>", false);
        test("<long km/h>", "<long  km/h>", false);
        test("<int h/km>", "<int h/km>", false);
        test("<void km>", "<void km>", true);
        test("<km int>", "<km int>", true);
    }

    private void test(String control, String s, boolean expectedParseError) {
        Optional<ASTPrimitiveWithSIUnitType> astOpt = Optional.empty();
        prettyPrinter = new PrimitiveWithSIUnitTypesPrettyPrinter(new IndentPrinter());
        try {
            astOpt = parser.parsePrimitiveWithSIUnitType(new StringReader(s));
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
