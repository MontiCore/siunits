/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.siunittypes4computing;

import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.siunits.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunits.siunittypes4math.prettyprint.SIUnitTypes4ComputingPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.*;

public class SIUnitTypes4ComputingTest {

    TestSIJavaParser parser = new TestSIJavaParser();
    SIUnitTypes4ComputingPrettyPrinter prettyPrinter;

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        test("km/h<double>", "km/h<double>", false);
        test("km/h<long>", "km/h<long>", false);
        test("h/km<int>", "h/km<int>", false);
        test("km<void>", "km<void>", true);
        test("int<km>", "int<km>", true);
    }

    private void test(String control, String s, boolean expectedParseError) {
        Optional<ASTSIUnitType4Computing> astOpt = Optional.empty();
        prettyPrinter = new SIUnitTypes4ComputingPrettyPrinter(new IndentPrinter());
        try {
            astOpt = parser.parseSIUnitType4Computing(new StringReader(s));
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
