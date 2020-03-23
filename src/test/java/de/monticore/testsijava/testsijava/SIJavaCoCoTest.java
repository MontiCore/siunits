package de.monticore.testsijava.testsijava;

import de.monticore.io.paths.ModelPath;
import de.monticore.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.testsijava.testsijava._cocos.TestSIJavaCoCoChecker;
import de.monticore.testsijava.testsijava._cocos.TypeCheckCoCo;
import de.monticore.testsijava.testsijava._symboltable.TestSIJavaGlobalScope;
import de.monticore.testsijava.testsijava._symboltable.TestSIJavaSymbolTableCreator;
import de.monticore.testsijava.testsijavasiunittypesonly._cocos.TestSIJavaSIUnitTypesOnlyCoCoChecker;
import de.monticore.testsijava.testsijavasiunittypesonly._parser.TestSIJavaSIUnitTypesOnlyParser;
import de.monticore.testsijava.testsijavasiunittypesonly._symboltable.TestSIJavaSIUnitTypesOnlyGlobalScope;
import de.monticore.testsijava.testsijavasiunittypesonly._symboltable.TestSIJavaSIUnitTypesOnlyLanguage;
import de.monticore.testsijava.testsijavasiunittypesonly._symboltable.TestSIJavaSIUnitTypesOnlySymTabMill;
import de.monticore.testsijava.testsijavasiunittypesonly._symboltable.TestSIJavaSIUnitTypesOnlySymbolTableCreator;
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
        TestSIJavaSIUnitTypesOnlyLanguage lang = new TestSIJavaSIUnitTypesOnlyLanguage("TestSIJavaLanguage", "sijava") {};
        String path = "src/test/resources/";
        TestSIJavaSIUnitTypesOnlyGlobalScope globalScope = new TestSIJavaSIUnitTypesOnlyGlobalScope(new ModelPath(Paths.get(path)), lang);
        TestSIJavaSIUnitTypesOnlySymbolTableCreator testSIJavaSymbolTableCreator = TestSIJavaSIUnitTypesOnlySymTabMill.testSIJavaSIUnitTypesOnlySymbolTableCreatorBuilder().addToScopeStack(globalScope).build();
        ASTSIJavaClass model = parseModel(input);
//        testSIJavaSymbolTableCreator.createFromAST(model);
        TestSIJavaSIUnitTypesOnlyCoCoChecker checker = new TestSIJavaSIUnitTypesOnlyCoCoChecker();
        checker.addCoCo(new TypeCheckCoCo(globalScope));

        try {
            checker.checkAll(model);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(expectedError, Log.getErrorCount() > 0);
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
    public void testSIModel() {
        String model = "de/monticore/testsijava/MyClass.sijava";
        typeCheckCoCo(model, false);
    }

    @Test
    public void testSIModel_WithError() {
        String model = "de/monticore/testsijava/MyClass_WithCoCoError.sijava";
        typeCheckCoCo(model, true);
    }
}
