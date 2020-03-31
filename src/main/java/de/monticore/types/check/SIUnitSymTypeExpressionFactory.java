package de.monticore.types.check;

import de.monticore.lang.siunits.utility.UnitFactory;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.monticore.types.typesymbols._symboltable.TypeSymbol;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.monticore.types.check.SymTypeExpressionHelperWithSIUnitTypes.isSIUnitType;

public class SIUnitSymTypeExpressionFactory extends SymTypeExpressionFactory {
    /**
     * createSIUnitBasic: for SIUnitBasicTypes
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
     * @param nameWithPrefix Of form deg, m, km, ...
     * @param exponent The exponent of the BasicType
     */
    public static SymTypeOfSIUnitBasic createSIUnitBasic(String nameWithPrefix, Integer exponent) {
        SymTypeOfSIUnitBasic symType = null;
        String nameWithOutPrefix = UnitFactory.printFormattedToStandard(nameWithPrefix);
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

    public static SymTypeOfSIUnitBasic createSIUnitBasic(TypeSymbolLoader typeSymbolLoader) {
        return new SymTypeOfSIUnitBasic(typeSymbolLoader);
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     * @param name Of form km^2*s/kg, ...
     */
    public static SymTypeExpression createSIUnit(String name) {
        return createSIUnit(name, DefsSIUnitType.getSIUnitTypeSymbolsScope());
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     * @param name Of form km^2*s/kg, ...
     * @param enclosingScope The node's enclosing scope
     */
    public static SymTypeExpression createSIUnit(String name, ITypeSymbolsScope enclosingScope) {
        String formattedStandard = UnitFactory.printFormattedToStandard(name);
        String[] split = formattedStandard.split("\\/");
        List<String> numeratorStringList = Arrays.asList(split[0].split("\\*"));
        List<String> denominatorStringList = new ArrayList<>();
        if (split.length == 2)
            denominatorStringList = Arrays.asList(split[1].replace("(","").replace(")","")
                    .split("\\*"));
        else if (split.length == 1 && split[0].equals("")) // dimensionless, will return int_type in the next function
            return createSIUnit(new TypeSymbolLoader("1", enclosingScope), new ArrayList<>(), new ArrayList<>());
        return createSIUnit(numeratorStringList, denominatorStringList, new TypeSymbolLoader(formattedStandard, enclosingScope));
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     * @param numerator List of the numerator SIBasicTypes
     * @param denominator List of the denominator SIBasicTypes
     * @param enclosingScope The node's enclosing scope
     */
    public static SymTypeExpression createSIUnit(List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator, ITypeSymbolsScope enclosingScope) {
        String numeratorString = numerator.stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.joining("*"));
        String denominatorString = numerator.stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.joining("*"));
        if (numerator.size() > 1)
            numeratorString = "(" + numeratorString + ")";
        if (denominator.size() > 1)
            denominatorString = "(" + numeratorString + ")";
        String name = numeratorString + ((denominator.size() >= 1) ? "/" + denominatorString : "");
        return createSIUnit(new TypeSymbolLoader(name, enclosingScope), numerator, denominator);
    }

    /**
     * createSIUnit: for SIUnitTypes
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     * @param numerator List of the numerator SIBasicTypes
     * @param denominator List of the denominator SIBasicTypes
     */
    public static SymTypeExpression createSIUnit(TypeSymbolLoader typeSymbolLoader, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        SymTypeExpression result;
        if (numerator.size() == 0 && denominator.size() == 0 || typeSymbolLoader.getName().equals("1"))
            result = createTypeConstant("int");
        else
            result = new SymTypeOfSIUnit(typeSymbolLoader, numerator, denominator);

        // Check if the symType is already in the scope and add it otherwise
        // Needed because there can be created new SIUnitType while computing, e.g. varM*varS
        final String name = result.print();
        if (typeSymbolLoader.getEnclosingScope().getLocalTypeSymbols().stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList()).size() == 0) {
            TypeSymbol newSymbol = DefsSIUnitType.type(result.print());
            typeSymbolLoader.getEnclosingScope().add(newSymbol);
        }

        return result;
    }

    // Method signature clashes with the before, so the typeSymbolLoader is the last argument
    /**
     * createSIUnit: for SIUnitTypes from Strings
     * returns SymTypeExpression because the unit could be dimensionless (e.g. m/m)
     * @param numerator List of the numerator SIBasicTypes as Strings
     * @param denominator List of the denominator SIBasicTypes as Strings
     */
    public static SymTypeExpression createSIUnit(List<String> numerator, List<String> denominator, TypeSymbolLoader typeSymbolLoader) {
        List<SymTypeOfSIUnitBasic> numeratorSymTypes = new ArrayList<>();
        List<SymTypeOfSIUnitBasic> denominatorSymTypes = new ArrayList<>();
        for (String num: numerator)
            numeratorSymTypes.add(createSIUnitBasic(num));
        for (String den: denominator)
            denominatorSymTypes.add(createSIUnitBasic(den));
        return createSIUnit(typeSymbolLoader, numeratorSymTypes, denominatorSymTypes);
    }

    public static SymTypeExpression createPrimitiveWithSIUnitType(SymTypeConstant primitiveType, SymTypeExpression siunitType, TypeSymbolLoader typeSymbolLoader) {
        SymTypeExpression result;
        if (!primitiveType.isNumericType())
            result = siunitType;
        else if (!isSIUnitType(siunitType))
            result = primitiveType;
        else
            result = new SymTypeOfPrimitiveWithSIUnit(typeSymbolLoader, primitiveType, siunitType);

        // Check if the symType is already in the scope and add it otherwise
        // Needed because there can be created new SIUnitType while computing, e.g. varM*varS
        final String name = result.print();
        if (typeSymbolLoader.getEnclosingScope().getLocalTypeSymbols().stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList()).size() == 0) {
            TypeSymbol newSymbol = DefsSIUnitType.type(result.print());
            typeSymbolLoader.getEnclosingScope().add(newSymbol);
        }

        return result;
    }

    public static SymTypeExpression createPrimitiveWithSIUnitType(SymTypeConstant primitiveType, SymTypeExpression siunitType, ITypeSymbolsScope enclosingScope) {
        String name = "(" + primitiveType.print() + "," + siunitType.print() + ")";
        return createPrimitiveWithSIUnitType(primitiveType, siunitType, new TypeSymbolLoader(name, enclosingScope));
    }
}
