package de.monticore.testsijava.testsijavasiunittypesonly;

import de.monticore.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.testsijava.testsijavasiunittypesonly._parser.TestSIJavaSIUnitTypesOnlyParser;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class SIJavaSIUnitTypesOnlyTest {

    @Before
    public void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private ASTSIJavaClass parseModel(String input) {
        TestSIJavaSIUnitTypesOnlyParser parser = new TestSIJavaSIUnitTypesOnlyParser();
        Optional<ASTSIJavaClass> res = Optional.empty();
        try {
            res = parser.parseSIJavaClass("src/test/resources/" + input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(res.isPresent());
        return res.get();
    }

    @Test
    public void parseSIModelTest1() {
        String model = "de/monticore/testsijava/testsijavasiunittypesonly/MyClass.sijava";
        parseModel(model);
    }

    @Test
    public void parseSIModelTest2() {
        String model = "de/monticore/testsijava/testsijavasiunittypesonly/MyClass_WithCoCoError.sijava";
        parseModel(model);
    }
}
