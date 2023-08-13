/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.utility;

import de.monticore.siunits.SIUnitsMill;
import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits._parser.SIUnitsParser;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UnitFactoryTest {

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
        SIUnitsMill.reset();
        SIUnitsMill.init();
        SIUnitsMill.initializeSIUnits();
    }
    @Test
    public void testAll() throws IOException {
        for (String s: SIUnitConstants.getAllUnits())
            testUnitFactory(s);
    }

    private void testUnitFactory(String s) {
        ASTSIUnit astsiUnit = null;
        try {
            astsiUnit = (new SIUnitsParser()).parseSIUnit(new StringReader(s)).get();
        } catch (IOException e) {
            fail("Could not parse unit: " + s);
        }

        assertEquals(UnitFactory.createUnit(s), UnitFactory.createUnit(astsiUnit));
    }
}