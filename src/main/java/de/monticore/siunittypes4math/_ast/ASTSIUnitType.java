/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunittypes4math._ast;

import de.monticore.symboltable.ISymbol;

import java.util.Optional;

public class ASTSIUnitType extends ASTSIUnitTypeTOP {

  ISymbol definingSymbol;

  @Override
  public Optional<? extends ISymbol> getDefiningSymbol() {
    return Optional.ofNullable(this.definingSymbol);
  }

  @Override
  public void setDefiningSymbol(ISymbol symbol) {
    this.definingSymbol = symbol;
  }
}
