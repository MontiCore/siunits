package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsDelegatorVisitor;

import java.util.Optional;

public class SynthesizeSymTypeOfCombinedTypes extends CombineExpressionsWithLiteralsDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes;

    private SynthesizeSymTypeFromPrimitiveWithSIUnitTypes symTypeFromPrimitiveWithSIUnitTypes;

    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;

    private CombineExpressionsWithLiteralsDelegatorVisitor realThis = this;

    private TypeCheckResult typeCheckResult = new TypeCheckResult();

    @Override
    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getLast());
    }

    @Override
    public void init() {
        symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes();
        symTypeFromPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromPrimitiveWithSIUnitTypes();
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes();

        setMCSimpleGenericTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCCollectionTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCBasicTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setPrimitiveWithSIUnitTypesVisitor(symTypeFromPrimitiveWithSIUnitTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);

        typeCheckResult = new TypeCheckResult();
        symTypeFromMCSimpleGenericTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);
    }

    @Override
    public CombineExpressionsWithLiteralsDelegatorVisitor getRealThis(){
        return realThis;
    }
}
