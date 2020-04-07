/*
 * Copyright (c) 2019 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.siunittypes._visitor.SIUnitTypesVisitor;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;

import java.util.Optional;

/**
 * Visitor for Derivation of SymType from SIUnitTypes
 * i.e. for
 * types/SIUnitTypes.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes implements ISynthesize, SIUnitTypesVisitor {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    protected IExpressionsBasisScope scope;

    public SynthesizeSymTypeFromSIUnitTypes(IExpressionsBasisScope scope) {
        this.scope = scope;
    }

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    SIUnitTypesVisitor realThis = this;

    @Override
    public void setRealThis(SIUnitTypesVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitTypesVisitor getRealThis() {
        return realThis;
    }

    // ---------------------------------------------------------- Storage result

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    public LastResult lastResult = new LastResult();

    public Optional<SymTypeExpression> getResult() {
        return Optional.of(lastResult.getLast());
    }

    public void init() {
        lastResult = new LastResult();
    }

    public void setLastResult(LastResult lastResult){
        this.lastResult = lastResult;
    }

    @Override
    public void endVisit(ASTSIUnitType siunittype) {
        lastResult.setLast(SIUnitSymTypeExpressionFactory.createSIUnit(siunittype.toString(), scope));
    }
}
