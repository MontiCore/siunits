/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.siunits.utility;

import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits._parser.SIUnitsParser;
import org.junit.Test;

import javax.measure.unit.Unit;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UnitPrettyPrinterTest {

    @Test
    public void test() {
        assertEquals("C*m/(kg*s^2)", UnitPrettyPrinter.printUnit("m/(s*s/(C/kg))"));
    }

    @Test
    public void testAll() throws IOException {
        for (String s: SIUnitConstants.getAllUnits())
            testUnitPrettyPrinter(s);
    }

    private void testUnitPrettyPrinter(String s) {
        ASTSIUnit astsiUnit = null;
        try {
            astsiUnit = (new SIUnitsParser()).parseSIUnit(new StringReader(s)).get();
        } catch (IOException e) {
            fail("Could not parse unit: " + s);
        }
        Unit unit = UnitFactory.createUnit(astsiUnit);

        assertEquals(UnitPrettyPrinter.printUnit(s), UnitPrettyPrinter.printUnit(astsiUnit));
        assertEquals(UnitPrettyPrinter.printUnit(s), UnitPrettyPrinter.printUnit(unit));
        assertEquals(UnitPrettyPrinter.printUnit(unit), UnitPrettyPrinter.printUnit(astsiUnit));

        assertEquals(UnitPrettyPrinter.printStandardUnit(s), UnitPrettyPrinter.printStandardUnit(astsiUnit));
        assertEquals(UnitPrettyPrinter.printStandardUnit(s), UnitPrettyPrinter.printStandardUnit(unit));
        assertEquals(UnitPrettyPrinter.printStandardUnit(unit), UnitPrettyPrinter.printStandardUnit(astsiUnit));
    }
}