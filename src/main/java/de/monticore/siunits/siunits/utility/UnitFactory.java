/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunits.siunits.utility;

import de.monticore.siunits.siunits._ast.ASTSIUnit;
import de.monticore.siunits.siunits._parser.SIUnitsParser;
import de.monticore.siunits.siunits.prettyprint.SIUnitsWithBracketsPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import javax.measure.converter.LogConverter;
import javax.measure.converter.MultiplyConverter;
import javax.measure.converter.RationalConverter;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.unit.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * This class is intended to handle the Units from the jscience package javax.measure.unit.
 * It should be the only class to create Units or to print them. It is mainly used to handle
 * SI units from the grammar de.monticore.siunits.SIUnits.mc4
 */
public class UnitFactory {

    /**
     * Create a {@link javax.measure.unit.Unit} from a String, e.g. from km/(Ohm*siunit^2)
     *
     * @param siunit ast of the SIUnit from the grammar de.monticore.siunits.SIUnits
     * @return a {@link javax.measure.unit.Unit}
     */
    public static Unit createUnit(ASTSIUnit siunit) {
        return getInstance()._createUnit(siunit);
    }

    private Unit _createUnit(ASTSIUnit siunit) {
        String print = SIUnitsWithBracketsPrettyPrinter.prettyprint(siunit);
        String str = print
                .replace("1/", "(m/m)/");
        return Unit.valueOf(str);
    }

    /**
     * Create a {@link javax.measure.unit.Unit} from a String, e.g. from km/(Ohm*siunit^2)
     *
     * @param siunit the String representation of a unit
     * @return a {@link javax.measure.unit.Unit}
     */
    public static Unit createUnit(String siunit) {
        return getInstance()._createUnit(siunit);
    }

    private Unit _createUnit(String siunit) {
        if ("1".equals(siunit))
            return Unit.ONE;
        SIUnitsParser parser = new SIUnitsParser();
        Optional<ASTSIUnit> ast = null;
        try {
            ast = parser.parse(new StringReader(siunit));
        } catch (IOException e) {
            Log.error("0x"); //TODO
        }
        return _createUnit(ast.get());
    }

    public static Unit createBaseUnit(Unit unit) {
        return getInstance()._createBaseUnit(unit);
    }

    private Unit _createBaseUnit(Unit unit) {
        Unit<?> systemUnit = unit.getStandardUnit();
        if (systemUnit instanceof BaseUnit) {
            return systemUnit;
        } else if (systemUnit instanceof AlternateUnit) {
            return _createBaseUnit(((AlternateUnit) systemUnit).getParent());
        } else if (!(systemUnit instanceof ProductUnit)) {
            Log.error("0x System Unit cannot be an instance of " + unit.getClass()); // TODO
            return null;
        } else {
            ProductUnit<?> productUnit = (ProductUnit) systemUnit;
            Unit<?> baseUnits = Unit.ONE;

            for (int i = 0; i < productUnit.getUnitCount(); ++i) {
                Unit<?> un = _createBaseUnit(productUnit.getUnit(i));
                un = un.pow(productUnit.getUnitPow(i));
                un = un.root(productUnit.getUnitRoot(i));
                baseUnits = baseUnits.times(un);
            }

            return baseUnits;
        }
    }

    public static Unit createBaseUnit(ASTSIUnit unit) {
        return getInstance()._createBaseUnit(unit);
    }

    private Unit _createBaseUnit(ASTSIUnit unit) {
        String asString = SIUnitsWithBracketsPrettyPrinter.prettyprint(unit);
        return _createBaseUnit(_createUnit(asString));
    }

    public static Unit createBaseUnit(String unit) {
        return getInstance()._createBaseUnit(unit);
    }

    private Unit _createBaseUnit(String unit) {
        return _createBaseUnit(_createUnit(unit));
    }

    public static Unit createStandardUnit(Unit unit) {
        return getInstance()._createStandardUnit(unit);
    }

    private Unit _createStandardUnit(Unit unit) {
        return unit.getStandardUnit();
    }

    public static Unit createStandardUnit(ASTSIUnit unit) {
        return getInstance()._createStandardUnit(unit);
    }

    private Unit _createStandardUnit(ASTSIUnit unit) {
        return _createStandardUnit(_createUnit(unit));
    }

    public static Unit createStandardUnit(String unit) {
        return getInstance()._createStandardUnit(unit);
    }

    private Unit _createStandardUnit(String unit) {
        return _createStandardUnit(_createUnit(unit));
    }

    public static Unit removePrefixes(Unit unit) {
        return getInstance()._removePrefixes(unit);
    }

    private Unit _removePrefixes(Unit unit) {
        if (unit instanceof AlternateUnit) {
            return unit;
        } else if (unit instanceof BaseUnit) {
            return unit;
        } else if (unit instanceof ProductUnit) {
            ProductUnit productUnit = (ProductUnit) unit;
            Unit baseUnits = Unit.ONE;
            for (int i = 0; i < productUnit.getUnitCount(); ++i) {
                Unit<?> un = _removePrefixes(productUnit.getUnit(i));
                un = un.pow(productUnit.getUnitPow(i));
                un = un.root(productUnit.getUnitRoot(i));
                baseUnits = baseUnits.times(un);
            }
            return baseUnits;
        } else if (unit instanceof TransformedUnit) {
            return _removePrefixes(((TransformedUnit) unit).getParentUnit());
        }
        return null;
    }

    private static UnitFactory instance = null;

    private static UnitFactory getInstance() {
        if (instance == null) {
            instance = new UnitFactory();
            instance.init();
        }

        List<String> baseUnits = new LinkedList<>();
        for (Unit unit : SI.getInstance().getUnits()) {
            if (unit instanceof BaseUnit) {
                baseUnits.add(((BaseUnit) unit).getSymbol());
            } else if (unit instanceof AlternateUnit) {
                baseUnits.add(((AlternateUnit) unit).getSymbol());
            }
        }

        return instance;
    }

    /**
     * Initiation of the UnitFactory, adding labels and prefixes
     */
    private void init() {
        //TODO: Label standard Units

        Unit<Length> AU = SI.METRE.times(1495978707).times(100);
        Unit<Dimensionless> NEPER = Unit.ONE.transform((new LogConverter(2.718281828459045D)));
        Unit<Dimensionless> BEL = Unit.ONE.transform((new LogConverter(10.0D)));
        Unit DEG = NonSI.REVOLUTION.transform(new UniqueConverter(new RationalConverter(1,360), 3));
        Unit OHM = SI.OHM.transform(new UniqueConverter(new Identity(), 1));
        Unit DAY = NonSI.DAY;
        Unit DALTON = SI.KILOGRAM.transform(new MultiplyConverter(1.66053906660E-27D));
        // CustomConverter has a different hash code and therefore LITER != Liter and both have to be possible
        // And more important later kL != m^3
        Unit LITER = SI.CUBIC_METRE.transform(new UniqueConverter(new MultiplyConverter(0.001), 3));
        Unit Liter = SI.CUBIC_METRE.transform(new UniqueConverter(new MultiplyConverter(0.001), 5));
        Unit Rd = SI.BECQUEREL.transform(new MultiplyConverter(1000000L));
        Unit RAD = SI.GRAY.divide(100L);
        Unit REM = SI.SIEVERT.divide(100L);



        UnitFormat.getInstance().label(AU, "au");
        UnitFormat.getInstance().label(NEPER, "Np");
        UnitFormat.getInstance().label(BEL, "B");
        UnitFormat.getInstance().label(DEG, "deg");
        UnitFormat.getInstance().label(DAY, "d");
        UnitFormat.getInstance().label(DALTON, "Da");
        UnitFormat.getInstance().label(OHM, "Ohm");
        UnitFormat.getInstance().label(Liter, "l");
        UnitFormat.getInstance().label(LITER, "L");
        UnitFormat.getInstance().label(Rd, "MBq");
        UnitFormat.getInstance().label(RAD, "cGy");
        UnitFormat.getInstance().label(REM, "cSv");


        initPrefixes();
        addPrefixesToUnit(Liter, "l");
        addPrefixesToUnit(LITER, "L");
        addPrefixesToUnit(OHM, "Ohm");
        addPrefix("u", new UniqueConverter(prefixes.get("micro"), 1));
        addPrefix("µ", new UniqueConverter(prefixes.get("micro"), 2));
    }

    private void addPrefixesToUnit(Unit unit, String label) {
        for (String prefix : prefixes.keySet()) {
            Unit prefixUnit = unit.transform(prefixes.get(prefix));
            String prefixName = prefix + label;
            UnitFormat.getInstance().label(prefixUnit, prefixName);
        }
        unitsWithPrefixes.put(label, unit);
    }

    private void addPrefix(String pre, UnitConverter converter) {
        prefixes.put(pre, converter);
        for (String unitName : unitsWithPrefixes.keySet()) {
            Unit unit = unitsWithPrefixes.get(unitName);
            Unit prefixUnit = unit.transform(converter);
            if ("kg".equals(unitName)) {
                unitName = "g";
                prefixUnit = prefixUnit.transform(new RationalConverter(1L, 1000L));
            }
            String prefixName = pre + unitName;
            UnitFormat.getInstance().label(prefixUnit, prefixName);
        }
    }

    private Map<String, UnitConverter> prefixes = new HashMap();
    private Map<String, Unit> unitsWithPrefixes = new HashMap<>();

    private void initPrefixes() {
        String[] prefixNamesPos = new String[]{"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da"};
        String[] prefixNamesNeg = new String[]{"d", "c", "m", "micro", "n", "p", "f", "a", "z", "y"};
        double[] convertersPos = {1.0E24D, 1.0E21D, 1.0E18D, 1.0E15D, 1.0E12D, 1.0E9D, 1.0E6D, 1.0E3D, 1.0E2D, 1.0E1D};
        double[] convertersNeg = {1.0E1D, 1.0E2D, 1.0E3D, 1.0E6D, 1.0E9D, 1.0E12D, 1.0E15D, 1.0E18D, 1.0E21D, 1.0E24D};
        for (int i = 0; i < prefixNamesPos.length; i++) {
            UnitConverter converter;
            if (convertersPos[i] == 1.0E21D)
                converter = new Compound(new RationalConverter((long) 1.0E18D, 1L), new RationalConverter((long) 1.0E3D, 1L));
            else if (convertersPos[i] == 1.0E24D)
                converter = new Compound(new RationalConverter((long) 1.0E18D, 1L), new RationalConverter((long) 1.0E6D, 1L));
            else
                converter = new UniqueConverter(new RationalConverter((long) convertersPos[i], 1L), 1);
            prefixes.put(prefixNamesPos[i], converter);
        }
        for (int i = 0; i < prefixNamesNeg.length; i++) {
            UnitConverter converter;
            if (convertersNeg[i] == 1.0E21D)
                converter = new Compound(new RationalConverter(1L, (long) 1.0E18D), new RationalConverter(1L, (long) 1.0E3D));
            else if (convertersNeg[i] == 1.0E24D)
                converter = new Compound(new RationalConverter(1L, (long) 1.0E18D), new RationalConverter(1L, (long) 1.0E6D));
            else
                converter = new UniqueConverter(new RationalConverter(1L, (long) convertersNeg[i]),1);
            prefixes.put(prefixNamesNeg[i], converter);
        }

        Unit[] unitsWithPrefixes = {SI.AMPERE, SI.BECQUEREL, SI.CANDELA, SI.COULOMB, SI.FARAD, SI.GRAY, SI.HENRY, SI.HERTZ, SI.JOULE, SI.KATAL, SI.KELVIN, SI.KILOGRAM, SI.LUMEN, SI.LUX, SI.METRE, SI.MOLE, SI.NEWTON, SI.OHM, SI.PASCAL, SI.RADIAN, SI.SECOND, SI.SIEMENS, SI.SIEVERT, SI.STERADIAN, SI.TESLA, SI.VOLT, SI.WATT, SI.WEBER};
        for (Unit unit : unitsWithPrefixes)
            this.unitsWithPrefixes.put(unit.toString(), unit);
    }

    private class UniqueConverter extends UnitConverter {
        private final UnitConverter converter;
        private final int id;

        private UniqueConverter(UnitConverter converter, int id) {
            this.converter = converter;
            this.id = id;
        }

        public UnitConverter inverse() {
            return new UniqueConverter(this.converter.inverse(), id);
        }

        public double convert(double x) {
            return this.converter.convert(x);
        }

        public boolean isLinear() {
            return converter.isLinear();
        }

        public int hashCode() {
            return converter.hashCode() * 10000 + id * 10 + 3;
        }
    }

    private class Compound extends UnitConverter {
        private final UnitConverter _first;
        private final UnitConverter _second;

        private Compound(UnitConverter first, UnitConverter second) {
            this._first = first;
            this._second = second;
        }

        public UnitConverter inverse() {
            return new Compound(this._second.inverse(), this._first.inverse());
        }

        public double convert(double x) {
            return this._second.convert(this._first.convert(x));
        }

        public boolean isLinear() {
            return this._first.isLinear() && this._second.isLinear();
        }

        public int hashCode() {
            return _first.hashCode() * 100 + _second.hashCode() * 10 + 5;
        }
    }

    private class Identity extends UnitConverter {
        private Identity() {
        }

        public UnitConverter inverse() {
            return this;
        }

        public double convert(double x) {
            return x;
        }

        public boolean isLinear() {
            return true;
        }

        public UnitConverter concatenate(UnitConverter converter) {
            return converter;
        }
    }
}
