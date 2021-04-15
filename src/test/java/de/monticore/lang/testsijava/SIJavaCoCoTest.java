/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._cocos.OnlyAssignmentAndCallExpressionCoCo;
import de.monticore.lang.testsijava.testsijava._cocos.TestSIJavaCoCoChecker;
import de.monticore.lang.testsijava.testsijava._cocos.TestSIJavaTypeCheckCoCo;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.siunits.SIUnitsMill;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class SIJavaCoCoTest {

    @Before
    public void init() {
        Log.init();
        Log.enableFailQuick(false);
        TestSIJavaMill.reset();
        TestSIJavaMill.init();
        SIUnitsMill.initializeSIUnits();
        TestSIJavaMill.globalScope().add(TestSIJavaMill.typeSymbolBuilder()
            .setName("si")
            .setEnclosingScope(TestSIJavaMill.globalScope())
            .setSpannedScope(TestSIJavaMill.scope())
            .build()
        );
    }

    private void typeCheckCoCo(String input, boolean expectedError) {
        Log.getFindings().clear();
        ASTSIJavaClass model = parseModel(input);
        TestSIJavaMill.scopesGenitorDelegator().createFromAST(model);
        TestSIJavaCoCoChecker checker = new TestSIJavaCoCoChecker();
        checker.addCoCo(TestSIJavaTypeCheckCoCo.getCoCo());
        checker.addCoCo(new OnlyAssignmentAndCallExpressionCoCo());

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
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        typeCheckCoCo(model, false);
    }

    @Test
    public void testSIModel_WithError() {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass_WithCoCoError.sijava";
        typeCheckCoCo(model, true);
    }

    @Test
    public void testSIModel_WithError2() {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass_WithCoCoError2.sijava";
        typeCheckCoCo(model, true);
    }

    @Test
    public void testMain() {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        typeCheckCoCo(model, false);
    }

    @Test
    public void testTestPrintAndValue() {
        String model = "test/de/monticore/lang/testsijava/testsijava/TestPrintAndValue.sijava";
        typeCheckCoCo(model, false);
    }
}
