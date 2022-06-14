/* (c) https://github.com/MontiCore/monticore */
package de.monticore.customsiunittypes4computing._ast;

import de.monticore.symboltable.ISymbol;

import java.util.Optional;

public class ASTCustomSIUnitType4Computing extends ASTCustomSIUnitType4ComputingTOP {

  protected ISymbol definingSymbol;


  @Override
  public Optional<? extends ISymbol> getDefiningSymbol() {
    return Optional.ofNullable(this.definingSymbol);
  }

  @Override
  public void setDefiningSymbol(ISymbol symbol) {
    this.definingSymbol = symbol;
  }
}
