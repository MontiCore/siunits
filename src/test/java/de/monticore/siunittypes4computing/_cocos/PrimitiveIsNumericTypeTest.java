/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.siunittypes4computing._cocos;

import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.siunits.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunits.siunittypes4computing._ast.ASTSIUnitTypes4ComputingNode;
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
                "m<byte>",
                "m<short>",
                "m<int>",
                "m<long>",
                "m<char>",
                "m<float>",
                "m<double>"
        };
        for (String s : toTest)
            test(s, false);
    }

    @Test
    public void testNegative() throws IOException {
        test("m<boolean>", true);
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
