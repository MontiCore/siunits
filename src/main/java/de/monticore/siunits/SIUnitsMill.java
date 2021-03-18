/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits;

import com.google.common.collect.Maps;
import de.monticore.siunits.utility.SIUnitConstants;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.types.check.SymTypeOfSIUnitBasic;

import java.util.List;
import java.util.Map;

public class SIUnitsMill extends SIUnitsMillTOP {

 /* public static void initMe (SIUnitsMill a)  {
    super(a);
    siUnitsInitializer = a;
  }

  */

  /**
   * List of all SIUnitBasicTypes without prefix or exponent
   */
  private static Map<String, SymTypeOfSIUnitBasic> sIUnitBasicTypes = Maps.newHashMap();

  private static void createSIUnitBasicTypeAndAddToScope(String name) {
    if (sIUnitBasicTypes.get(name) != null) {
      return;
    }
    TypeSymbol newType = BasicSymbolsMill.typeSymbolBuilder()
            .setName(name)
            .setEnclosingScope(BasicSymbolsMill.globalScope())
            .setSpannedScope(BasicSymbolsMill.scope())
            .build();
    BasicSymbolsMill.globalScope().add(newType);
    SymTypeOfSIUnitBasic newSymType = new SymTypeOfSIUnitBasic(newType);
    sIUnitBasicTypes.put(name, newSymType);
  }

  protected static SIUnitsMill siUnitsInitializer;

  public static void initializeSIUnits(){
    if(siUnitsInitializer == null){
      siUnitsInitializer = getMill();
    }
    siUnitsInitializer._initializeSIUnits();
  }

  public void _initializeSIUnits(){
    BasicSymbolsMill.initializePrimitives();
    List<String> baseUnits = SIUnitConstants.getAllUnits();
    baseUnits.forEach(u -> createSIUnitBasicTypeAndAddToScope(u));
    createSIUnitBasicTypeAndAddToScope("°");
    createSIUnitBasicTypeAndAddToScope("1");
  }
}
