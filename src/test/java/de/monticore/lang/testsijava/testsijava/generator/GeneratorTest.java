package de.monticore.lang.testsijava.testsijava.generator;

import org.junit.Test;

public class GeneratorTest {

    private String modelPath = "src/test/resources";
    private String outputPath = "target/generated-test-sources/monticore/sourcecode";

    private void test(String model) {
        Generator.generate(modelPath, model, outputPath);
    }

    @Test
    public void test1() {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        test(model);
    }

    @Test
    public void test2() {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        test(model);
    }
}