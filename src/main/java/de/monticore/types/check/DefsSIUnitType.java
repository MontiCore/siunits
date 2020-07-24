/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.utility.SIUnitConstants;
import de.monticore.symbols.oosymbols._symboltable.OOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbolSurrogate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines the basic siunit types without prefixes and exponents
 * and puts all those types into a TypeSymbolsScope
 */
public class DefsSIUnitType extends DefsTypeBasic {

    private static SymTypeOfSIUnit superUnit;

    /**
     * List of all SIUnitBasicTypes without prefix or exponent
     */
    private static Map<String, SymTypeOfSIUnitBasic> sIUnitBasicTypes;

    static {
        setup_SIUnitTypes();
    }

    private static void setup_SIUnitTypes() {
        sIUnitBasicTypes = new HashMap<>();

        List<String> baseUnits = SIUnitConstants.getAllUnits();

        OOSymbolsScope scope = new OOSymbolsScope();
        for (String currentType : baseUnits)
            createSIUnitBasicTypeAndAddToScope(currentType, scope);
        createSIUnitBasicTypeAndAddToScope("°", scope);
        createSIUnitBasicTypeAndAddToScope("1", scope);
    }

    private static void createSIUnitBasicTypeAndAddToScope(String name, OOSymbolsScope scope) {
        if (sIUnitBasicTypes.get(name) != null) return;
        OOTypeSymbol newSymbol = type(name);
        scope.add(newSymbol);
        OOTypeSymbolSurrogate loader = (new OOTypeSymbolSurrogate(name));
        loader.setEnclosingScope(scope);
        SymTypeOfSIUnitBasic newSymType = new SymTypeOfSIUnitBasic(loader);
        sIUnitBasicTypes.put(name, newSymType);
    }

    public static Map<String, SymTypeOfSIUnitBasic> getSIUnitBasicTypes() {
        if (sIUnitBasicTypes == null)
            setup_SIUnitTypes();
        return sIUnitBasicTypes;
    }

    public static SymTypeOfSIUnit getSuperUnit() {
        return superUnit;
    }
}
