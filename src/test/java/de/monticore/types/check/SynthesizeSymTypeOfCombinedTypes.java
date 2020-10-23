/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals._visitor.CombineExpressionsWithSIUnitLiteralsDelegatorVisitor;

import java.util.Optional;

public class SynthesizeSymTypeOfCombinedTypes extends CombineExpressionsWithSIUnitLiteralsDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes;

    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;

    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;

    private CombineExpressionsWithSIUnitLiteralsDelegatorVisitor realThis = this;

    private TypeCheckResult typeCheckResult = new TypeCheckResult();

    @Override
    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getCurrentResult());
    }

    @Override
    public void init() {
        symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromSIUnitTypes4Computing();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();

        setMCSimpleGenericTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCCollectionTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCBasicTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setSIUnitTypes4ComputingVisitor(symTypeFromSIUnitTypes4Computing);
        setSIUnitTypes4MathVisitor(symTypeFromSIUnitTypes4Math);

        typeCheckResult = new TypeCheckResult();
        symTypeFromMCSimpleGenericTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
    }

    @Override
    public CombineExpressionsWithSIUnitLiteralsDelegatorVisitor getRealThis(){
        return realThis;
    }
}
