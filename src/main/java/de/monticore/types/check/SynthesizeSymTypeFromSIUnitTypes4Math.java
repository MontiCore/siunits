/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits.prettyprint.SIUnitsPrettyPrinter;
import de.monticore.siunittypes4math._ast.ASTSIUnitType4Math;
import de.monticore.siunittypes4math._visitor.SIUnitTypes4MathVisitor;
import de.monticore.types.mcbasictypes._symboltable.IMCBasicTypesScope;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Visitor for Derivation of SymType from SIUnitTypes
 * i.e. for
 * types/SIUnitTypes.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes4Math implements ISynthesize, SIUnitTypes4MathVisitor {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    SIUnitTypes4MathVisitor realThis = this;

    @Override
    public void setRealThis(SIUnitTypes4MathVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitTypes4MathVisitor getRealThis() {
        return realThis;
    }

    // ---------------------------------------------------------- Storage result

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    public TypeCheckResult typeCheckResult = new TypeCheckResult();

    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getLast());
    }

    public void init() {
        typeCheckResult = new TypeCheckResult();
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
    }

    @Override
    public void endVisit(ASTSIUnitType4Math siunittype) {
        typeCheckResult.setLast(SIUnitSymTypeExpressionFactory.createSIUnit(
                SIUnitsPrettyPrinter.prettyprint(siunittype.getSIUnit()), getScope(siunittype.getEnclosingScope())));
    }

    public ITypeSymbolsScope getScope (IMCBasicTypesScope mcBasicTypesScope){
        // is accepted only here, decided on 07.04.2020
        if(!(mcBasicTypesScope instanceof ITypeSymbolsScope)){
            Log.error("0xAE107 the enclosing scope of the type does not implement the interface ITypeSymbolsScope");
        }
        // is accepted only here, decided on 07.04.2020
        return (ITypeSymbolsScope) mcBasicTypesScope;
    }
}
