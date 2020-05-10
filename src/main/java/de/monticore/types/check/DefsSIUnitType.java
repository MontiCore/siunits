package de.monticore.types.check;

import de.monticore.types.typesymbols._symboltable.TypeSymbol;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.monticore.types.typesymbols._symboltable.TypeSymbolsScope;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines the basic siunit types without prefixes and exponents
 * and puts all those types into a TypeSymbolsScope
 */
public class DefsSIUnitType extends DefsTypeBasic {

    /**
     * List of all SIUnitBasicTypes without prefix or exponent
     */
    private static Map<String, SymTypeOfSIUnitBasic> sIUnitBasicTypes;

    static {
        setup_SIUnitTypes();
    }

    private static void setup_SIUnitTypes() {
        sIUnitBasicTypes = new HashMap<>();

        List<String> officallyAcceptedUnits = Arrays.asList
                ("min", "h", "day", "ha", "t", "Au", "Np", "B", "dB", "eV", "u");
        List<String> siUnitDimensionlesses = Arrays.asList
                ("deg", "rad", "sr");
        List<String> unitBases = Arrays.asList
                ("m", "kg", "s", "A", "K", "mol", "cd", "Hz", "N", "Pa", "J", "W", "C", "V", "F", "Ohm", "S", "Wb", "T", "H", "lm", "lx", "Bq", "Gy", "Sv", "kat");
        List<String> celsiusFahrenheit = Arrays.asList
                ("°C", "°F");

        TypeSymbolsScope scope = new TypeSymbolsScope();

        for (String currentType : siUnitDimensionlesses)
            createSIUnitBasicTypeAndAddToScope(currentType, scope);
        for (String currentType : officallyAcceptedUnits)
            createSIUnitBasicTypeAndAddToScope(currentType, scope);
        for (String unitBase : unitBases)
            createSIUnitBasicTypeAndAddToScope(unitBase, scope);
        for (String currentType : celsiusFahrenheit)
            createSIUnitBasicTypeAndAddToScope(currentType, scope);
        createSIUnitBasicTypeAndAddToScope("°", scope);
        createSIUnitBasicTypeAndAddToScope("1", scope);
    }

    private static void createSIUnitBasicTypeAndAddToScope(String name, TypeSymbolsScope scope) {
        if (sIUnitBasicTypes.get(name) != null) return;
        TypeSymbol newSymbol = type(name);
        scope.add(newSymbol);
        SymTypeOfSIUnitBasic newSymType = new SymTypeOfSIUnitBasic(new TypeSymbolLoader(name, scope));
        sIUnitBasicTypes.put(name, newSymType);
    }

    public static Map<String, SymTypeOfSIUnitBasic> getSIUnitBasicTypes() {
        if (sIUnitBasicTypes == null)
            setup_SIUnitTypes();
        return sIUnitBasicTypes;
    }
}
