package de.monticore.lang.testsijava.testsijava.generator;

import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {

    private String modelPath = "src/test/resources";
    private String outputPath = "target/generated-test-sources/monticore/sourcecode";

    private void test(String model) {
        Generator.generate(modelPath, model, outputPath);
    }

    @Before
    public void init() {
        Log.init();
    }

    @Test
    public void testMyClass() {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        test(model);
    }

    @Test
    public void testMain() {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        test(model);
    }
}