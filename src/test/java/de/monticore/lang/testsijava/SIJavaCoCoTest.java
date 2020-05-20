package de.monticore.lang.testsijava;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._cocos.TestSIJavaCoCoChecker;
import de.monticore.lang.testsijava.testsijava._cocos.TypeCheckCoCo;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.ITestSIJavaScope;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaGlobalScope;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaLanguage;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaSymbolTableCreator;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class SIJavaCoCoTest {

    @BeforeClass
    public static void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    private void typeCheckCoCo(String input, boolean expectedError) {
        Log.getFindings().clear();
        ASTSIJavaClass model = parseModel(input);
        ITestSIJavaScope globalScope = buildScope(model);
        TestSIJavaCoCoChecker checker = new TestSIJavaCoCoChecker();
        checker.addCoCo(new TypeCheckCoCo());

        try {
            checker.checkAll(model);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(expectedError, Log.getErrorCount() > 0);
    }

    private ITestSIJavaScope buildScope(ASTSIJavaClass model) {
        TestSIJavaLanguage lang = new TestSIJavaLanguage("TestSIJavaLanguage", "sijava") {};
        String path = "src/test/resources/";
        ITestSIJavaScope globalScope = new TestSIJavaGlobalScope(new ModelPath(Paths.get(path)), lang);

        TestSIJavaSymbolTableCreator TestSIJavaSymbolTableCreator
                = TestSIJavaMill.testSIJavaSymbolTableCreatorBuilder().addToScopeStack(globalScope).build();
        TestSIJavaSymbolTableCreator.createFromAST(model);

        return globalScope;
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
}
