/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsTraverser;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesTraverser;

import java.util.Optional;

public class SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator implements ISynthesize {

  protected TypeCheckResult typeCheckResult;

  protected CombineExpressionsWithLiteralsTraverser traverser;

  public SynthesizeSymTypeFromCombineExpressionsWithLiteralsDelegator(){
    init();
  }

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

  public MCBasicTypesTraverser getTraverser() {
    return traverser;
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCType type) {
    typeCheckResult.reset();
    type.accept(getTraverser());
    return typeCheckResult;
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCReturnType type) {
    typeCheckResult.reset();
    type.accept(getTraverser());
    return typeCheckResult;
  }

  @Override
  public TypeCheckResult synthesizeType(ASTMCQualifiedName qName) {
    typeCheckResult.reset();
    qName.accept(getTraverser());
    return typeCheckResult;
  }
}
