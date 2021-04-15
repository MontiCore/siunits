package de.monticore.lang.testsijava.testsijava.generator;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.siunits.SIUnitsMill;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class PrintAsJavaClassTest {

    @Before
    public void init() {
        Log.init();
        Log.enableFailQuick(false);
        TestSIJavaMill.reset();
        TestSIJavaMill.init();
        SIUnitsMill.initializeSIUnits();
    }

    @Test
    public void test1() {
        String model = "test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        printAsJavaClass(model);
    }

    @Test
    public void test2() {
        String model = "test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        printAsJavaClass(model);
    }

    public void printAsJavaClass(String model) {
        ASTSIJavaClass astsiJavaClass = parseModel(model);
      TestSIJavaMill.scopesGenitorDelegator().createFromAST(astsiJavaClass);
        String print = PrintAsJavaClass.printAsJavaClass(astsiJavaClass);
        System.out.println(print);
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
}