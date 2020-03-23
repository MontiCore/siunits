package de.monticore.types.check;

import de.monticore.types.typesymbols._symboltable.TypeSymbol;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.monticore.types.typesymbols._symboltable.TypeSymbolsScope;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefsSIUnitType extends DefsTypeBasic {

    /**
     * List of potential SIUnitBasicTypes without prefix or exponent
     */
    private static Map<String, SymTypeOfSIUnitBasic> sIUnitBasicTypes;

    private static List<String> officallyAcceptedUnits;
    private static List<String> siUnitDimensionlesses;
    private static List<String> unitBases;
    private static List<String> celsiusFahrenheit;
    private static TypeSymbolsScope sIUnitBasicTypeSymbolsScope;
    private static TypeSymbolsScope sIUnitTypeSymbolsScope;

    static {
        setup_SIUnitTypes();
    }

    private static void setArrays() {
        officallyAcceptedUnits =
                Arrays.asList("min","h","day","ha","t","Au","Np","B","dB","eV","u");
        siUnitDimensionlesses = Arrays
                .asList("deg", "rad", "sr");
        unitBases = Arrays.asList("m","kg","s","A","K","mol","cd","Hz","N","Pa","J","W","C","V","F","Ohm","S","Wb","T","H","lm","lx","Bq","Gy","Sv","kat");
        celsiusFahrenheit = Arrays.asList("°C, °F");
    }

    private static void setup_SIUnitTypes() {
        setArrays();
        sIUnitBasicTypeSymbolsScope = new TypeSymbolsScope();
        sIUnitTypeSymbolsScope = new TypeSymbolsScope();
        sIUnitBasicTypes = new HashMap<>();

        for (String currentType : siUnitDimensionlesses)
            createSIUnitBasicTypeAndAddToScope(currentType);
        for (String currentType : officallyAcceptedUnits)
            createSIUnitBasicTypeAndAddToScope(currentType);
        for (String unitBase : unitBases)
            createSIUnitBasicTypeAndAddToScope(unitBase);
        for (String currentType : celsiusFahrenheit)
            createSIUnitBasicTypeAndAddToScope(currentType);
        createSIUnitBasicTypeAndAddToScope("°");
        createSIUnitBasicTypeAndAddToScope("1");
    }

    private static void createSIUnitBasicTypeAndAddToScope(String name) {
        if (sIUnitBasicTypes.get(name) != null) return;
        TypeSymbol newSymbol = type(name);
        getSIUnitBasicTypeSymbolsScope().add(newSymbol);
        SymTypeOfSIUnitBasic newSymType = new SymTypeOfSIUnitBasic(new TypeSymbolLoader(name, getSIUnitBasicTypeSymbolsScope()));
        sIUnitBasicTypes.put(name, newSymType);
    }

    public static Map<String, SymTypeOfSIUnitBasic> getSIUnitBasicTypes() {
        if (sIUnitBasicTypes == null)
            setup_SIUnitTypes();
        return sIUnitBasicTypes;
    }

    public static TypeSymbolsScope getSIUnitBasicTypeSymbolsScope() {
        return sIUnitBasicTypeSymbolsScope;
    }

    public static TypeSymbolsScope getSIUnitTypeSymbolsScope() {
        return sIUnitTypeSymbolsScope;
    }
}
