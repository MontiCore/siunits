package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals._visitor.CombineExpressionsWithLiteralsDelegatorVisitor;
import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;

import java.util.Optional;

public class SynthesizeSymTypeFromMCSimpleGenericOrSIUnitTypes
        extends CombineExpressionsWithLiteralsDelegatorVisitor
        implements ISynthesize
          {

    private SynthesizeSymTypeFromMCSimpleGenericTypes symTypeFromMCSimpleGenericTypes;
    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;
    private IExpressionsBasisScope scope;

    public SynthesizeSymTypeFromMCSimpleGenericOrSIUnitTypes(IExpressionsBasisScope scope) {
        this.scope = scope;
        init();
    }

    CombineExpressionsWithLiteralsDelegatorVisitor realThis = this;

    @Override
    public CombineExpressionsWithLiteralsDelegatorVisitor getRealThis() {
        return realThis;
    }

    public Optional<SymTypeExpression> getResult() {
        if (symTypeFromMCSimpleGenericTypes.getResult().isPresent())
            return symTypeFromMCSimpleGenericTypes.getResult();
        if (symTypeFromSIUnitTypes.getResult().isPresent())
            return symTypeFromSIUnitTypes.getResult();
        return Optional.empty();
    }

    public void init() {
        symTypeFromMCSimpleGenericTypes = new SynthesizeSymTypeFromMCSimpleGenericTypes(scope);
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes();

        setMCBasicTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCCollectionTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setMCSimpleGenericTypesVisitor(symTypeFromMCSimpleGenericTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);

        setScope(scope);
    }

    public void setScope(IExpressionsBasisScope scope) {
        this.scope = scope;
        symTypeFromMCSimpleGenericTypes.setScope(scope);
    }
}
