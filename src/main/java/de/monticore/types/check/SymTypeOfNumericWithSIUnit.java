/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.symbols.oosymbols.OOSymbolsMill;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbolSurrogate;
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
            OOSymbolsScope enclosingScope = OOSymbolsMill.oOSymbolsScopeBuilder().build();
            OOTypeSymbol newSymbol =  de.monticore.types.check.DefsTypeBasic.type(name);
            enclosingScope.add(newSymbol);
            OOTypeSymbolSurrogate loader = (new OOTypeSymbolSurrogate(name));
            loader.setEnclosingScope(enclosingScope);

            SymTypeConstant doubeType = SymTypeExpressionFactory.createTypeConstant("double");
            superNumericUnitType = new SymTypeOfNumericWithSIUnit(loader, doubeType, SymTypeOfSIUnit.getSuperUnitType());
        }
        return superNumericUnitType;
    }

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfNumericWithSIUnit(OOTypeSymbolSurrogate typeSymbolSurrogate, SymTypeExpression numericType, SymTypeExpression siunitType) {
        this.typeSymbolSurrogate = typeSymbolSurrogate;
        this.numericType = numericType;
        this.siunitType = siunitType;
        setSuperType();
    }

    public SymTypeOfNumericWithSIUnit(IOOSymbolsScope enclosingScope, SymTypeConstant numericType, SymTypeExpression siunitType) {
        this.numericType = numericType;
        this.siunitType = siunitType;
        this.typeSymbolSurrogate = new OOTypeSymbolSurrogate(print());
        this.typeSymbolSurrogate.setEnclosingScope(enclosingScope);
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
        OOTypeSymbolSurrogate loader = new OOTypeSymbolSurrogate(this.typeSymbolSurrogate.getName());
        loader.setEnclosingScope(this.typeSymbolSurrogate.getEnclosingScope());
        return new SymTypeOfNumericWithSIUnit(
                loader,
                this.numericType.deepClone(),
                this.siunitType.deepClone());
    }

    @Override
    public boolean deepEquals(SymTypeExpression sym) {
        if (!(sym instanceof SymTypeOfNumericWithSIUnit))
            return false;
        if(this.typeSymbolSurrogate== null ||sym.typeSymbolSurrogate==null){
            return false;
        }
//        if(!this.typeSymbolSurrogate.getEnclosingScope().equals(sym.typeSymbolSurrogate.getEnclosingScope())){
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
