package de.monticore.lang.siunits.utility;

import de.monticore.lang.siunits._ast.ASTSIUnit;
import de.monticore.lang.siunits._parser.SIUnitsParser;
import de.monticore.lang.siunits.prettyprint.SIUnitWithBracketsPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import javax.measure.converter.LogConverter;
import javax.measure.converter.MultiplyConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

/**
 * This class is intended to handle the Units from the jscience package javax.measure.unit.
 * It should be the only class to create Units or to print them. It is mainly used to handle
 * SI units from the grammar de.monticore.lang.SIUnits.mc4
 */
public class UnitFactory {

    /**
     * Create a {@link javax.measure.unit.Unit} from a String, e.g. from km/(Ohm*siunit^2)
     * @param siunit ast of the SIUnit from the grammar de.monticore.lang.SIUnits
     * @return a {@link javax.measure.unit.Unit}
     */
    public static Unit createUnit(ASTSIUnit siunit) {
        return getInstance()._createUnit(siunit);
    }

    private Unit _createUnit(ASTSIUnit siunit) {
        String print = SIUnitWithBracketsPrettyPrinter.prettyprint(siunit);
        String str = print
                .replace("1/", "(m/m)/");
        return Unit.valueOf(str);
    }


    /**
     * Create a {@link javax.measure.unit.Unit} from a String, e.g. from km/(Ohm*siunit^2)
     * @param siunit the String representation of a unit
     * @return a {@link javax.measure.unit.Unit}
     */
    public static Unit createUnit(String siunit) {
        return getInstance()._createUnit(siunit);
    }

    private Unit _createUnit(String siunit) {
        if ("1".equals(siunit))
            return Dimensionless.UNIT;
        SIUnitsParser parser = new SIUnitsParser();
        Optional<ASTSIUnit> ast = null;
        try {
             ast = parser.parse(new StringReader(siunit));
        } catch (IOException e) {
            Log.error("0x"); //TODO
        }
        return _createUnit(ast.get());
    }

    private static UnitFactory instance = null;

    private static UnitFactory getInstance() {
        if (instance == null) {
            instance = new UnitFactory();
            init();
        }
        return instance;
    }

    /**
     * Initiation of the UnitFactory, adding labels and prefixes
     */
    private static void init() {
        Unit<Length> AU = SI.METRE.times(1495978707).times(100);
        Unit<Dimensionless> NEPER = Unit.ONE.transform((new LogConverter(2.718281828459045D)));
        Unit<Dimensionless> BEL = Unit.ONE.transform((new LogConverter(10.0D)));
        Unit DEG = Unit.valueOf("°");
        Unit OHM = SI.OHM;
        Unit LITER = NonSI.LITER;
        UnitFormat.getInstance().label(AU, "Au");
        UnitFormat.getInstance().label(NEPER, "Np");
        UnitFormat.getInstance().label(BEL, "B");
        UnitFormat.getInstance().label(DEG, "deg");
        UnitFormat.getInstance().label(OHM, "Ohm");
        UnitFormat.getInstance().alias(LITER, "l");

        // Add prefixes for Ohm
        String[] prefixes = new String[]{"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "d", "c", "m", "micro", "n", "p", "f", "a", "z", "y"};
        double[] converters = {1.0E24D, 1.0E21D, 1.0E18D, 1.0E15D, 1.0E12D, 1.0E9D, 1.0E6D, 1.0E3D, 1.0E2D, 1.0E1D, 1.0E-1D, 1.0E-2D, 1.0E-3D, 1.0E-6D, 1.0E-9D, 1.0E-12D, 1.0E-15D, 1.0E-18D, 1.0E-21D, 1.0E-24D};
        for (int i = 0; i < prefixes.length; i++) {
            Unit prefixUnit = OHM.transform(new MultiplyConverter(converters[i]));
            UnitFormat.getInstance().label(prefixUnit, prefixes[i] + "Ohm");
        }
    }
}
