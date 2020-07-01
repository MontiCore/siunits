/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._cocos.TestSIJavaCoCoChecker;
import de.monticore.lang.testsijava.testsijava._cocos.TypeCheckCoCo;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class SIJavaCoCoTest {

    @BeforeClass
    public static void init() {
        Log.init();
        Log.enableFailQuick(false);
    }

    private void typeCheckCoCo(String input, boolean expectedError) {
        Log.getFindings().clear();
        ASTSIJavaClass model = parseModel(input);
        model.accept(TestSIJavaMill.testSIJavaSymbolTableCreatorBuilder().build());
        TestSIJavaCoCoChecker checker = new TestSIJavaCoCoChecker();
        checker.addCoCo(new TypeCheckCoCo());

        try {
            checker.checkAll(model);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(expectedError, Log.getErrorCount() > 0);
    }

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
    public void testSIModel() {
        String model = "de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        typeCheckCoCo(model, false);
    }

    @Test
    public void testSIModel_WithError() {
        String model = "de/monticore/lang/testsijava/testsijava/MyClass_WithCoCoError.sijava";
        typeCheckCoCo(model, true);
    }

    @Test
    public void testSIModel_WithError2() {
        String model = "de/monticore/lang/testsijava/testsijava/MyClass_WithCoCoError2.sijava";
        typeCheckCoCo(model, true);
    }

    @Test
    public void testMain() {
        String model = "de/monticore/lang/testsijava/testsijava/Main.sijava";
        typeCheckCoCo(model, false);
    }
}
