/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.testsijava;

import de.monticore.io.paths.MCPath;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._cocos.TestSIJavaWithCustomTypesCoCoChecker;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._cocos.TestSIJavaWithCustomTypesTypeCheckCoCo;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._parser.TestSIJavaWithCustomTypesParser;
import de.monticore.siunits.SIUnitsMill;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class SIJavaWithCustomTypesCoCoTest {

  @Before
  public void init() {
    LogStub.init();
    Log.enableFailQuick(false);
    TestSIJavaWithCustomTypesMill.reset();
    TestSIJavaWithCustomTypesMill.init();
    String path = "src/test/resources/";
    TestSIJavaWithCustomTypesMill.globalScope().setSymbolPath(new MCPath(Paths.get(path)));
    SIUnitsMill.initializeSIUnits();
  }

  private void typeCheckCoCo(String input, boolean expectedError) {
    Log.getFindings().clear();
    ASTSIJavaClass model = parseModel(input);
    TestSIJavaWithCustomTypesMill.scopesGenitorDelegator().createFromAST(model);
    TestSIJavaWithCustomTypesCoCoChecker checker = new TestSIJavaWithCustomTypesCoCoChecker();
    checker.addCoCo(TestSIJavaWithCustomTypesTypeCheckCoCo.getCoCo());

    try {
      checker.checkAll(model);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(expectedError, Log.getErrorCount() > 0);
  }

  private ASTSIJavaClass parseModel(String input) {
    TestSIJavaWithCustomTypesParser parser = new TestSIJavaWithCustomTypesParser();
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
    String model = "test/de/monticore/lang/testsijava/testsijavawithcustomtypes/MyClass.sijava";
    typeCheckCoCo(model, false);
  }

  @Test
  public void testSIModel_WithError() {
    String model = "test/de/monticore/lang/testsijava/testsijavawithcustomtypes/MyClass_WithCoCoError.sijava";
    typeCheckCoCo(model, true);
  }
}
