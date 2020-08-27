/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.siunits._ast.ASTSIUnit;
import de.monticore.siunits._symboltable.ISIUnitsScope;
import de.monticore.siunits.utility.UnitPrettyPrinter;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4Computing;
import de.monticore.siunittypes4computing._ast.ASTSIUnitType4ComputingInt;
import de.monticore.siunittypes4computing._symboltable.ISIUnitTypes4ComputingScope;
import de.monticore.siunittypes4computing._visitor.SIUnitTypes4ComputingVisitor;
import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.se_rwth.commons.logging.Log;

import static de.monticore.types.check.TypeCheck.*;

/**
 * Visitor for Derivation of SymType from SIUnitTypes4Computing
 * i.e. for
 * types/SIUnitTypes4Computing.mc4
 */
public class SynthesizeSymTypeFromSIUnitTypes4Computing extends SynthesizeSymTypeFromMCBasicTypes
        implements SIUnitTypes4ComputingVisitor {
    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    SIUnitTypes4ComputingVisitor realThis = this;

    @Override
    public void setRealThis(SIUnitTypes4ComputingVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SIUnitTypes4ComputingVisitor getRealThis() {
        return realThis;
    }


    @Override
    public void visit(ASTSIUnit node) {
        typeCheckResult.setCurrentResult(SIUnitSymTypeExpressionFactory.createSIUnit(UnitPrettyPrinter.printUnit(node), getScope(node.getEnclosingScope())));
    }

    @Override
    public void traverse(ASTSIUnitType4Computing node) {
        traverseSIUnitType4Computing(node);
    }

    protected void traverseSIUnitType4Computing(ASTSIUnitType4ComputingInt node) {
        SymTypeExpression numericType = null;
        SymTypeExpression siunitType = null;

        node.getMCPrimitiveType().accept(getRealThis());
        if (!typeCheckResult.isPresentCurrentResult() || !isNumericType(typeCheckResult.getCurrentResult())) {
            Log.error("0xA0495");
        }
        numericType = typeCheckResult.getCurrentResult();
        typeCheckResult.reset();

        siunitType = SIUnitSymTypeExpressionFactory.createSIUnit(
                UnitPrettyPrinter.printUnit(node.getSIUnit()), getScope(node.getEnclosingScope()));
        typeCheckResult.setCurrentResult(SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(numericType, siunitType, getScope(node.getEnclosingScope())));
    }

    /**
     * test if the expression is of numeric type (double, float, long, int, char, short, byte)
     */
    private boolean isNumericType(SymTypeExpression type) {
        return (isDouble(type) || isFloat(type) ||
                isLong(type) || isInt(type) ||
                isChar(type) || isShort(type) ||
                isByte(type));
    }

    private IBasicSymbolsScope getScope(ISIUnitsScope enclosingScope) {
        // is accepted only here, decided on 07.04.2020
        if(!(enclosingScope instanceof IOOSymbolsScope)){
            Log.error("0xAE112 the enclosing scope of the type does not implement the interface IOOSymbolsScope");
        }
        // is accepted only here, decided on 07.04.2020
        return (IOOSymbolsScope) enclosingScope;
    }

    private IOOSymbolsScope getScope(ISIUnitTypes4ComputingScope enclosingScope) {
        // is accepted only here, decided on 07.04.2020
        if(!(enclosingScope instanceof IOOSymbolsScope)){
            Log.error("0xAE106 the enclosing scope of the type does not implement the interface IOOSymbolsScope");
        }
        // is accepted only here, decided on 07.04.2020
        return (IOOSymbolsScope) enclosingScope;
    }
}
