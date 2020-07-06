/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._parser.TestSIJavaWithCustomTypesParser;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class SIJavaWithCustomTypesTest {

    @Before
    public void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private ASTSIJavaClass parseModel(String input) {
        TestSIJavaWithCustomTypesParser parser = new TestSIJavaWithCustomTypesParser();
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
        String model = "test/de/monticore/lang/testsijava/testsijavawithcustomtypes/MyClass.sijava";
        parseModel(model);
    }

    @Test
    public void parseSIModelTest2() {
        String model = "test/de/monticore/lang/testsijava/testsijavawithcustomtypes/MyClass_WithCoCoError.sijava";
        parseModel(model);
    }
}
