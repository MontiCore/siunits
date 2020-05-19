package de.monticore.lang.types.primitivewithsiunittypes._cocos;

import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitType;
import de.monticore.lang.types.primitivewithsiunittypes._ast.ASTPrimitiveWithSIUnitTypesNode;
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
        ASTPrimitiveWithSIUnitType ast = parse(s);
        PrimitiveWithSIUnitTypesCoCoChecker checker = new PrimitiveWithSIUnitTypesCoCoChecker();
        checker.addCoCo(new PrimitiveIsNumericType());
        checker.checkAll((ASTPrimitiveWithSIUnitTypesNode) ast);

        if (errorExpected)
            assert (Log.getFindings().size() > 0);
        else
            assert (Log.getFindings().size() == 0);
    }

    private ASTPrimitiveWithSIUnitType parse(String s) throws IOException {
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTPrimitiveWithSIUnitType> ast = parser.parsePrimitiveWithSIUnitType(new StringReader(s));
        assert (ast.isPresent());
        return ast.get();
    }
}
