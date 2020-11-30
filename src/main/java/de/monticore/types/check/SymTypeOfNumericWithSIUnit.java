/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols.OOSymbolsMill;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.symboltable.serialization.JsonDeSers;
import de.monticore.symboltable.serialization.JsonPrinter;

import javax.measure.unit.Unit;
import java.util.LinkedList;
import java.util.List;

/**
 * SymTypeOfNumericWithSIUnit stores any kind of Numerics combined with SIUnit applied
 * to Arguments, such as <int m>, <double m/s>, <short m/s^2>, <int s/m> ...
 */
public class SymTypeOfNumericWithSIUnit extends SymTypeExpression {

    private SymTypeExpression siunitType;
    private SymTypeExpression numericType;

    private static SymTypeOfNumericWithSIUnit superNumericUnitType;

    public static SymTypeOfNumericWithSIUnit getSuperNumericUnitType() {
        if (superNumericUnitType == null) {
            String name = "SuperNumericWithUnit";
            IOOSymbolsScope enclosingScope = OOSymbolsMill.scope();
            OOTypeSymbol newSymbol =  de.monticore.types.check.DefsTypeBasic.type(name);
            enclosingScope.add(newSymbol);
            TypeSymbol loader = (new TypeSymbol(name));
            loader.setEnclosingScope(enclosingScope);

            SymTypeConstant doubeType = SymTypeExpressionFactory.createTypeConstant("double");
            superNumericUnitType = new SymTypeOfNumericWithSIUnit(loader, doubeType, SymTypeOfSIUnit.getSuperUnitType());
        }
        return superNumericUnitType;
    }

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfNumericWithSIUnit(TypeSymbol typeSymbol, SymTypeExpression numericType, SymTypeExpression siunitType) {
        this.typeSymbol = typeSymbol;
        this.numericType = numericType;
        this.siunitType = siunitType;
        setSuperType();
    }

    public SymTypeOfNumericWithSIUnit(IBasicSymbolsScope enclosingScope, SymTypeConstant numericType, SymTypeExpression siunitType) {
        this.numericType = numericType;
        this.siunitType = siunitType;
        this.typeSymbol = new TypeSymbol(print());
        this.typeSymbol.setEnclosingScope(enclosingScope);
        setSuperType();
    }

    private void setSuperType() {
        if (superNumericUnitType != null && this != superNumericUnitType) {
            List<SymTypeExpression> superTypes = new LinkedList<>();
            superTypes.add(getSuperNumericUnitType());
            this.getTypeInfo().setSuperTypesList(superTypes);
        }
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        return "(" + numericType.print() + "," + siunitType.print() + ")";
    }

    @Override
    public String toString() {
        return print();
    }

    /**
     * printAsJson: Umwandlung in einen kompakten Json String
     */
    protected String printAsJson() {
        JsonPrinter jp = new JsonPrinter();
        jp.beginObject();
        // Care: the following String needs to be adapted if the package was renamed
        jp.member(JsonDeSers.KIND, "de.monticore.types.check.SymTypeOfNumericWithSIUnit");
        jp.member("numericType", numericType.printAsJson());
        jp.member("siunitType", siunitType.printAsJson());
        jp.endObject();
        return jp.getContent();
    }

    @Override
    public SymTypeExpression deepClone() {
        TypeSymbol loader = new TypeSymbol(this.typeSymbol.getName());
        loader.setEnclosingScope(this.typeSymbol.getEnclosingScope());
        return new SymTypeOfNumericWithSIUnit(
                loader,
                this.numericType.deepClone(),
                this.siunitType.deepClone());
    }

    @Override
    public boolean deepEquals(SymTypeExpression sym) {
        if (!(sym instanceof SymTypeOfNumericWithSIUnit))
            return false;
        if(this.typeSymbol== null ||sym.typeSymbol==null){
            return false;
        }
//        if(!this.typeSymbol.getEnclosingScope().equals(sym.typeSymbol.getEnclosingScope())){
//            return false;
//        }
        if (!this.getUnit().isCompatible(((SymTypeOfNumericWithSIUnit) sym).getUnit())) {
            return false;
        }
        return true;
    }

    public Unit getUnit() {
        SymTypeExpression siunit = getSIUnit();
        if (!(siunit instanceof SymTypeOfSIUnit))
            return Unit.ONE;
        else {
            return ((SymTypeOfSIUnit) siunit).getUnit();
        }
    }

    public SymTypeExpression getSIUnit() {
        return siunitType;
    }

    public void setSiunitType(SymTypeOfSIUnit siunitType) {
        this.siunitType = siunitType;
    }

    public SymTypeExpression getNumericType() {
        return numericType;
    }

    public void setNumericType(SymTypeExpression numericType) {
        this.numericType = numericType;
    }
}
