/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._visitor.CombineExpressionsWithSIUnitLiteralsTraverser;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesTraverser;

import java.util.Optional;

public class SynthesizeSymTypeOfCombinedTypes implements ISynthesize {

    CombineExpressionsWithSIUnitLiteralsTraverser traverser;

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;

    private SynthesizeSymTypeFromMCCollectionTypes symTypeFromMCCollectionTypes;

    private SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes;

    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;

    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;

    private TypeCheckResult typeCheckResult = new TypeCheckResult();


    @Override
    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getCurrentResult());
    }

    @Override
    public void init() {
        traverser = CombineExpressionsWithSIUnitLiteralsMill.traverser();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        traverser.add4MCBasicTypes(symTypeFromMCBasicTypes);
        traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);

        symTypeFromMCCollectionTypes = new SynthesizeSymTypeFromMCCollectionTypes();
        traverser.add4MCCollectionTypes(symTypeFromMCCollectionTypes);
        traverser.setMCCollectionTypesHandler(symTypeFromMCCollectionTypes);

        symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes();
        traverser.add4MCSimpleGenericTypes(symTypeFromMCSimpleGenericTypes);
        traverser.setMCSimpleGenericTypesHandler(symTypeFromMCSimpleGenericTypes);

        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromSIUnitTypes4Computing();
        traverser.setSIUnitTypes4ComputingHandler(symTypeFromSIUnitTypes4Computing);

        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        traverser.setSIUnitTypes4MathHandler(symTypeFromSIUnitTypes4Math);

        setTypeCheckResult(new TypeCheckResult());
    }

    @Override
    public MCBasicTypesTraverser getTraverser() {
        return traverser;
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult) {
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromMCCollectionTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromMCSimpleGenericTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }
}
