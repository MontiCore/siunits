/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._symboltable.ICombineExpressionsWithLiteralsScope;
import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsVisitor;

public class FlatExpressionScopeSetter extends FlatExpressionScopeSetterAbs implements CombineExpressionsWithLiteralsVisitor {

  @Override
  protected ICombineExpressionsWithLiteralsScope getScope() {
    return (ICombineExpressionsWithLiteralsScope) scope;
  }

  public FlatExpressionScopeSetter(ICombineExpressionsWithLiteralsScope scope){
    super(scope);
    realThis = this;
  }

  private CombineExpressionsWithLiteralsVisitor realThis;

  @Override
  public CombineExpressionsWithLiteralsVisitor getRealThis() {
    return realThis;
  }

  @Override
  public void setRealThis(CombineExpressionsWithLiteralsVisitor realThis){
    this.realThis = realThis;
  }
}
