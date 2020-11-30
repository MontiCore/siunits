package de.monticore.lang.testsijava;

import de.monticore.lang.testsijava.testsijava.generator.Generator;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GeneratorTest {

    private String modelPath = "src/test/resources";
    private String outputPath = "target/generate";
    private String compareModelPath = "src/test/resources/compare";

    private void test(String model) throws IOException, ClassNotFoundException {
        Generator.generate(modelPath, model, outputPath);
        String javaModelName = model.replace(".sijava", ".java");
        File generatedFile = new File(outputPath + "/" + javaModelName);
        File compareFile = new File(compareModelPath + "/" + javaModelName);
        assert (generatedFile.exists());
        assertEquals ( FileUtils.readFileToString(compareFile, "utf-8"),
                FileUtils.readFileToString(generatedFile, "utf-8"));
    }

    @Before
    public void init() {
        Log.init();
    }

    @Test
    public void testMyClass() throws IOException, ClassNotFoundException {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        test(model);
    }

    @Test
    public void testMain() throws IOException, ClassNotFoundException {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        test(model);
    }

    @Test
    public void testMain2() throws IOException, ClassNotFoundException {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main2.sijava";
        test(model);
    }

    @Test
    public void testPrintAndValue() throws IOException, ClassNotFoundException {
        String model = "test/de/monticore/lang/testsijava/testsijava/TestPrintAndValue.sijava";
        test(model);
    }
}