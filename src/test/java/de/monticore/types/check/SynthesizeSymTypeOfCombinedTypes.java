package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsDelegatorVisitor;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;

import java.util.Optional;

public class SynthesizeSymTypeOfCombinedTypes extends CombineExpressionsWithLiteralsDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes;

    private SynthesizeSymTypeFromPrimitiveWithSIUnitTypes symTypeFromPrimitiveWithSIUnitTypes;

    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;

    private CombineExpressionsWithLiteralsDelegatorVisitor realThis;

    private LastResult lastResult = new LastResult();

    IExpressionsBasisScope scope;

    public SynthesizeSymTypeOfCombinedTypes(IExpressionsBasisScope scope) {
        realThis = this;
        this.scope = scope;
    }

    @Override
    public Optional<SymTypeExpression> getResult() {
        return Optional.of(lastResult.getLast());
    }

    @Override
    public void init() {
        symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes(scope);
        symTypeFromPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromPrimitiveWithSIUnitTypes(scope);
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes(scope);

        setMCSimpleGenericTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCCollectionTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCBasicTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setPrimitiveWithSIUnitTypesVisitor(symTypeFromPrimitiveWithSIUnitTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);

        lastResult = new LastResult();
        symTypeFromMCSimpleGenericTypes.setLastResult(lastResult);
        symTypeFromPrimitiveWithSIUnitTypes.setLastResult(lastResult);
    }

    public void setScope(IExpressionsBasisScope scope) {
        this.scope = scope;
        init();
    }

    @Override
    public CombineExpressionsWithLiteralsDelegatorVisitor getRealThis(){
        return realThis;
    }
}
