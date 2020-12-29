package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsTraverser;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesTraverser;

import java.util.Optional;

public class SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator implements ISynthesize {

  protected TypeCheckResult typeCheckResult;

  protected CombineExpressionsWithLiteralsTraverser traverser;

  public SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator(){
    init();
  }

  @Override
  public Optional<SymTypeExpression> getResult() {
    if(typeCheckResult.isPresentCurrentResult()){
      return Optional.of(typeCheckResult.getCurrentResult());
    }else{
      return Optional.empty();
    }
  }

  @Override
  public void init() {
    this.traverser = CombineExpressionsWithLiteralsMill.traverser();
    this.typeCheckResult = new TypeCheckResult();

    SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
    symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
    traverser.add4MCBasicTypes(symTypeFromMCBasicTypes);
    traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);

    SynthesizeSymTypeFromMCCollectionTypes symTypeFromMCCollectionTypes = new SynthesizeSymTypeFromMCCollectionTypes();
    symTypeFromMCCollectionTypes.setTypeCheckResult(typeCheckResult);
    traverser.add4MCCollectionTypes(symTypeFromMCCollectionTypes);
    traverser.setMCCollectionTypesHandler(symTypeFromMCCollectionTypes);

    SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes();
    symTypeFromMCSimpleGenericTypes.setTypeCheckResult(typeCheckResult);
    traverser.add4MCSimpleGenericTypes(symTypeFromMCSimpleGenericTypes);
    traverser.setMCSimpleGenericTypesHandler(symTypeFromMCSimpleGenericTypes);
  }

  @Override
  public MCBasicTypesTraverser getTraverser() {
    return traverser;
  }
}
