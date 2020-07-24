/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbolSurrogate;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.monticore.types.check.TypeCheck.*;

/**
 * SIUnitSymTypeExpressionFactory contains static functions that create
 * the SymTypeOfSIUnit or SymTypeOfNumericWithSIUnit
 * This factory therefore should be the only source to create those SymTypeExpressions.
 * No other source is needed.
 */
public class SIUnitSymTypeExpressionFactory extends SymTypeExpressionFactory {
    /**
     * createSIUnitBasic: for SIUnitBasicTypes
     *
     * @param name Of form deg, m, km^2, ...
     */
    public static SymTypeOfSIUnitBasic createSIUnitBasic(String name) {
        if (!name.contains("^"))
            return createSIUnitBasic(name, null);
        else {
            String nameWithPrefix = name.split("\\^")[0];
            Integer exponent = Integer.parseInt(name.split("\\^")[1]);
            return createSIUnitBasic(nameWithPrefix, exponent);
        }
    }

    /**
     * createSIUnitBasic: for SIUnitBasicTypes
     *
     * @param nameWithPrefix Of form deg, m, km, ...
     * @param exponent       The exponent of the BasicType
     */
    public static SymTypeOfSIUnitBasic createSIUnitBasic(String nameWithPrefix, Integer exponent) {
        SymTypeOfSIUnitBasic symType = null;

        if (nameWithPrefix.equals("kL")){
            int a = 3;
        }

//        String nameWithOutPrefix = UnitPrettyPrinter.printBaseUnit(nameWithPrefix);
        String nameWithOutPrefix = nameWithPrefix;

        if (nameWithOutPrefix.contains("^")) {
            String nameWithOutPrefixTemp = nameWithOutPrefix.split("\\^")[0];
            Integer exponentTemp = Integer.parseInt(nameWithOutPrefixTemp.split("\\^")[1]);
            exponent = exponentTemp * (exponent == null ? 1 : exponent);
            nameWithOutPrefix = nameWithOutPrefixTemp;
        }

        if (DefsSIUnitType.getSIUnitBasicTypes().containsKey(nameWithOutPrefix)) // Then there is no prefix
            symType = DefsSIUnitType.getSIUnitBasicTypes().get(nameWithOutPrefix);
        else
            Log.error("0x893F63 Internal Error: No siunit type " + nameWithOutPrefix + " stored as constant.");

        if (exponent != null && exponent.intValue() > 0) {
            symType = symType.deepClone();
            symType.setExponent(exponent);
        }
        return symType;
    }

    public static SymTypeOfSIUnitBasic createSIUnitBasic(OOTypeSymbolSurrogate typeSymbolSurrogate) {
        return new SymTypeOfSIUnitBasic(typeSymbolSurrogate);
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     *
     * @param name           Of form km^2*s/kg, ...
     * @param enclosingScope The node's enclosing scope
     */
    public static SymTypeExpression createSIUnit(String name, IOOSymbolsScope enclosingScope) {
        String formattedStandard = UnitPrettyPrinter.printUnit(name);
        String[] split = formattedStandard.split("\\/");
        List<String> numeratorStringList = Arrays.asList(split[0].split("\\*"));
        List<String> denominatorStringList = new ArrayList<>();
        if (split.length == 2)
            denominatorStringList = Arrays.asList(split[1].replace("(", "").replace(")", "")
                    .split("\\*"));
        else if (split.length == 1 && split[0].equals("1")) {// dimensionless, will return int_type in the next function
            OOTypeSymbolSurrogate loader = new OOTypeSymbolSurrogate("1");
            loader.setEnclosingScope(enclosingScope);
            return createSIUnit(loader, new ArrayList<>(), new ArrayList<>());
        }
        OOTypeSymbolSurrogate loader = new OOTypeSymbolSurrogate(formattedStandard);
        loader.setEnclosingScope(enclosingScope);
        return createSIUnit(numeratorStringList, denominatorStringList, loader);
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     *
     * @param numerator      List of the numerator SIBasicTypes
     * @param denominator    List of the denominator SIBasicTypes
     * @param enclosingScope The node's enclosing scope
     */
    public static SymTypeExpression createSIUnit(List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator, IOOSymbolsScope enclosingScope) {
        String numeratorString = numerator.stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.joining("*"));
        String denominatorString = numerator.stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.joining("*"));
        if (numerator.size() > 1)
            numeratorString = "(" + numeratorString + ")";
        if (denominator.size() > 1)
            denominatorString = "(" + numeratorString + ")";
        String name = numeratorString + ((denominator.size() >= 1) ? "/" + denominatorString : "");
        OOTypeSymbolSurrogate loader = new OOTypeSymbolSurrogate(name);
        loader.setEnclosingScope(enclosingScope);
        return createSIUnit(loader, numerator, denominator);
    }

    // Method signature clashes with the before, so the typeSymbolSurrogate is the last argument

    /**
     * createSIUnit: for SIUnitTypes from Strings
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     *
     * @param numerator   List of the numerator SIBasicTypes as Strings
     * @param denominator List of the denominator SIBasicTypes as Strings
     */
    public static SymTypeExpression createSIUnit(List<String> numerator, List<String> denominator, OOTypeSymbolSurrogate typeSymbolSurrogate) {
        List<SymTypeOfSIUnitBasic> numeratorSymTypes = new ArrayList<>();
        List<SymTypeOfSIUnitBasic> denominatorSymTypes = new ArrayList<>();
        for (String num : numerator)
            numeratorSymTypes.add(createSIUnitBasic(num));
        for (String den : denominator)
            denominatorSymTypes.add(createSIUnitBasic(den));
        return createSIUnit(typeSymbolSurrogate, numeratorSymTypes, denominatorSymTypes);
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     *
     * @param numerator   List of the numerator SIBasicTypes
     * @param denominator List of the denominator SIBasicTypes
     */
    public static SymTypeExpression createSIUnit(OOTypeSymbolSurrogate typeSymbolSurrogate, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        // Check if the symType is already in the scope and add it otherwise
        // Needed because there can be created new SIUnitType while computing, e.g. varM*varS
        final String name = typeSymbolSurrogate.getName();
        if (typeSymbolSurrogate.getEnclosingScope().getLocalOOTypeSymbols().stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList()).size() == 0) {
            OOTypeSymbol newSymbol = DefsSIUnitType.type(name);
            typeSymbolSurrogate.getEnclosingScope().add(newSymbol);
        }

        SymTypeExpression result;
        if (numerator.size() == 0 && denominator.size() == 0 || typeSymbolSurrogate.getName().equals("1"))
            result = createTypeConstant("int");
        else
            result = new SymTypeOfSIUnit(typeSymbolSurrogate, numerator, denominator);

        return result;
    }

    /**
     * creates a numeric with siunit type
     */
    public static SymTypeExpression createNumericWithSIUnitType(SymTypeExpression numericType, SymTypeExpression siunitType, OOTypeSymbolSurrogate typeSymbolSurrogate) {
        // Check if the symType is already in the scope and add it otherwise
        // Needed because there can be created new SIUnitType while computing, e.g. varM*varS
        final String name = typeSymbolSurrogate.getName();
        if (typeSymbolSurrogate.getEnclosingScope().getLocalOOTypeSymbols().stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList()).size() == 0) {
            OOTypeSymbol newSymbol = DefsSIUnitType.type(name);
            typeSymbolSurrogate.getEnclosingScope().add(newSymbol);
        }

        SymTypeExpression result;
        if (!isNumericType(numericType))
            Log.error("0xA0498 SymTypeExpression must be numeric type");
        if (!(siunitType instanceof SymTypeOfSIUnit))
            result = numericType;
        else
            result = new SymTypeOfNumericWithSIUnit(typeSymbolSurrogate, numericType, siunitType);

        return result;
    }

    /**
     * creates a numeric with siunit type
     */
    public static SymTypeExpression createNumericWithSIUnitType(SymTypeExpression numericType, SymTypeExpression siunitType, IOOSymbolsScope enclosingScope) {
        String siUnitPrint = siunitType.print();
        String name = "(" + numericType.print() + "," + siUnitPrint + ")";
        OOTypeSymbolSurrogate loader = new OOTypeSymbolSurrogate(name);
        loader.setEnclosingScope(enclosingScope);
        return createNumericWithSIUnitType(numericType, siunitType, loader);
    }

    /**
     * test if the expression is of numeric type (double, float, long, int, char, short, byte)
     */
    private static boolean isNumericType(SymTypeExpression type) {
        return (isDouble(type) || isFloat(type) ||
                isLong(type) || isInt(type) ||
                isChar(type) || isShort(type) ||
                isByte(type));
    }
}
