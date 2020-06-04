/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.types.siunittypes4computing._cocos;

import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.types.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.lang.types.siunittypes4computing._ast.ASTSIUnitTypes4ComputingNode;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

public class PrimitiveIsNumericTypeTest {

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
    }

    @Test
    public void testPositive() throws IOException {
        String[] toTest = {
                "byte in m",
                "short in m",
                "int in m",
                "long in m",
                "char in m",
                "float in m",
                "double in m"
        };
        for (String s : toTest)
            test(s, false);
    }

    @Test
    public void testNegative() throws IOException {
        test("boolean in m", true);
    }

    private void test(String s, boolean errorExpected) throws IOException {
        ASTSIUnitType4Computing ast = parse(s);
        SIUnitTypes4ComputingCoCoChecker checker = new SIUnitTypes4ComputingCoCoChecker();
        checker.addCoCo(new PrimitiveIsNumericType());
        checker.checkAll((ASTSIUnitTypes4ComputingNode) ast);

        if (errorExpected)
            assert (Log.getFindings().size() > 0);
        else
            assert (Log.getFindings().size() == 0);
    }

    private ASTSIUnitType4Computing parse(String s) throws IOException {
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTSIUnitType4Computing> ast = parser.parseSIUnitType4Computing(new StringReader(s));
        assert (ast.isPresent());
        return ast.get();
    }
}
