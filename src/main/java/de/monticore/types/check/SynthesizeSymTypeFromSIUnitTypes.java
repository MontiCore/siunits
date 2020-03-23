/*
 * Copyright (c) 2019 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;
import de.monticore.types.siunittypes._visitor.SIUnitTypesVisitor;

import java.util.Optional;

/**
 * Visitor for Derivation of SymType from SIUnits
 * i.e. for
 * types/SIUnit.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes implements ISynthesize, SIUnitTypesVisitor {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

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
    public Optional<SymTypeExpression> result;

    public Optional<SymTypeExpression> getResult() {
        return result;
    }

    public void init() {
        result = Optional.empty();
    }

    public void endVisit(ASTSIUnitType siunittype) {
        result = Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(siunittype.toString()));
    }
}
