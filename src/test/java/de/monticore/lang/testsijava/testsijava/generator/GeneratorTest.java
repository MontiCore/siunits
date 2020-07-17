package de.monticore.lang.testsijava.testsijava.generator;

import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class GeneratorTest {

    private String modelPath = "src/test/resources";
    private String outputPath = "target/generated-test-sources/monticore/sourcecode";
    private String compareModelPath = "src/test/resources/compare";

    private void test(String model) throws IOException {
        Generator.generate(modelPath, model, outputPath);
        String javaModelName = model.replace(".sijava", ".java");
        File generatedFile = new File(outputPath + "/" + javaModelName);
        File compareFile = new File(compareModelPath + "/" + javaModelName);
        assert (generatedFile.exists());
        assert (FileUtils.contentEquals(compareFile, generatedFile));
    }

    @Before
    public void init() {
        Log.init();
    }

    @Test
    public void testMyClass() throws IOException {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        test(model);
    }

    @Test
    public void testMain() throws IOException {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        test(model);
    }
}