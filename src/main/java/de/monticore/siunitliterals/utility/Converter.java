/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunitliterals.utility;

import de.monticore.siunits.utility.UnitFactory;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;

public class Converter {

    public static double convert(double value, Unit source, Unit target) {
        return getConverter(source, target).convert(value);
    }

    public static double convertTo(double value, Unit target) {
        return getConverterTo(target).convert(value);
    }

    public static double convertFrom(double value, Unit source) {
        return getConverterFrom(source).convert(value);
    }

    public static UnitConverter getConverter(Unit source, Unit target) {
        return source.getConverterTo(target);
    }

    public static UnitConverter getConverterTo(Unit target) {
        return UnitFactory.createBaseUnit(target).getConverterTo(target);
    }

    public static UnitConverter getConverterFrom(Unit source) {
        return source.getConverterTo(UnitFactory.createBaseUnit(source));
    }
}
