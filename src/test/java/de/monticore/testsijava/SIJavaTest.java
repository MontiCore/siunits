package de.monticore.testsijava;

import de.monticore.testsijava._ast.ASTSIJavaClass;
import de.monticore.testsijava._parser.TestSIJavaParser;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class SIJavaTest {
    private ASTSIJavaClass parseModel(String input) {
        TestSIJavaParser parser = new TestSIJavaParser();
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
        String model = "de/monticore/testsijava/MyClass.sijava";
        parseModel(model);
    }

    @Test
    public void parseSIModelTest2() {
        String model = "de/monticore/testsijava/MyClass_WithCoCoError.sijava";
        parseModel(model);
    }
}
