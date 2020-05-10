package de.monticore.lang.siunits.utility;

import com.google.common.collect.Lists;
import de.monticore.lang.siunits._ast.ASTSIUnit;
import de.monticore.lang.siunits._parser.SIUnitsParser;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UnitFactoryTest {
    @Test
    public void testAll() throws IOException {
        String[] prefixes = {"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "d", "c", "m", "n", "p", "f", "a", "z", "y"};
        String[] unitBases = {"m", "g", "s", "A", "K", "mol", "cd", "Hz", "N", "Pa", "J", "W", "C", "V", "F", "Ohm", "S", "Wb", "T", "H", "lm", "lx", "Bq", "Gy", "Sv", "kat"};
        String[] officallyAccepted = {"min", "h", "day", "ha", "t", "Au", "Np", "B", "dB", "eV", "u"};
        String[] dimensionless = {"deg", "rad", "sr"};
        String[] fahrenheitCelcius = {"°C", "°F"};


        List<String> combined = new ArrayList<>();
        combined.addAll(Lists.newArrayList(officallyAccepted));
        combined.addAll(Lists.newArrayList(dimensionless));
        combined.addAll(Lists.newArrayList(fahrenheitCelcius));

        for (String unitBase : unitBases) {
            combined.add(unitBase);
            for (String prefix : prefixes) {
                combined.add(prefix + unitBase);
            }
        }

        for (String s: combined)
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