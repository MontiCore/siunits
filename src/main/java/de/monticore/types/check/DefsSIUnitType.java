/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.siunits.utility.SIUnitConstants;
import de.monticore.types.typesymbols._symboltable.TypeSymbol;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.monticore.types.typesymbols._symboltable.TypeSymbolsScope;

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

//        List<String> baseUnits = Arrays.asList
//                ("A", "lm", "C", "Sv", "F", "H", "J", "K",
//                        "mol", "bit", "lx", "N", "Pa", "Gy", "S",
//                        "rad", "T", "V", "W", "kg", "sr", "kat",
//                        "cd", "Ohm", "Wb", "m", "Bq", "Hz", "s");

//        List<String> baseUnits = Arrays.asList
//                ("s", "m", "kg", "A", "K", "mol", "cd");

        List<String> baseUnits = SIUnitConstants.getAllUnits();

        TypeSymbolsScope scope = new TypeSymbolsScope();
        for (String currentType : baseUnits)
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
