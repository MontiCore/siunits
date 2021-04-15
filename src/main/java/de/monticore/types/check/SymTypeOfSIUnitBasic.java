/* (c) https://github.com/MontiCore/monticore */

//package de.monticore.types.check;
//
//public class SymTypeOfSIUnitBasic extends SymTypeExpression {
//
//    /**
//     * A SymTypeExpression has
//     *    a name (representing a TypeConstructor) and
//     *    a list of Type Expressions
//     * This is always the full qualified name (i.e. including package)
//     */
////    protected String unit;
//}
package de.monticore.types.check;

import de.monticore.siunits.utility.UnitFactory;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symboltable.serialization.JsonDeSers;
import de.monticore.symboltable.serialization.JsonPrinter;

import javax.measure.unit.Unit;
import java.util.ArrayList;
import java.util.Optional;

/**
 * SymTypeOfSIUnit stores any kind of SIUnit applied
 * to Arguments, such as m, s, s^2, kg...
 */
public class SymTypeOfSIUnitBasic extends SymTypeExpression {

    protected Optional<Integer> exponent = Optional.of(Integer.valueOf(1));
    protected Optional<String> prefix = Optional.of("");

    public SymTypeOfSIUnitBasic(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
        this.getTypeInfo().setSuperTypesList(new ArrayList<>());
    }

    public SymTypeOfSIUnitBasic(TypeSymbol typeSymbol, Integer exponent) {
        this.typeSymbol = typeSymbol;
        this.exponent = Optional.ofNullable(exponent);
        this.getTypeInfo().setSuperTypesList(new ArrayList<>());
    }

    public String getName() {
        return typeSymbol.getName();
    }

    public String getExponentString() {
        return (isExponentPresent() && getExponent() != 1) ? "^" + getExponent() : "";
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        return getName() + getExponentString();
    }

    @Override
    public String printFullName() {
        return typeSymbol.getFullName() + getExponentString();
    }

    /**
     * printAsJson: Umwandlung in einen kompakten Json String
     */
    protected String printAsJson() {
        JsonPrinter jp = new JsonPrinter();
        jp.beginObject();
        // Care: the following String needs to be adapted if the package was renamed
        jp.member(JsonDeSers.KIND, "de.monticore.types.check.SymTypeOfSIUnitBasic");
        jp.member("constName", getName());
        jp.member("exponent", getExponent());
        jp.endObject();
        return jp.getContent();
    }

    @Override
    public SymTypeOfSIUnitBasic deepClone() {
        TypeSymbol newTypeSymbolLoader = new TypeSymbol(typeSymbol.getName());
        newTypeSymbolLoader.setEnclosingScope(typeSymbol.getEnclosingScope());
        SymTypeOfSIUnitBasic clone;
        if (!exponent.isPresent())
            clone = new SymTypeOfSIUnitBasic(newTypeSymbolLoader);
        else
            clone = new SymTypeOfSIUnitBasic(newTypeSymbolLoader, exponent.get());
        return clone;
    }

    @Override
    public boolean deepEquals(SymTypeExpression sym) {
        if (!(sym instanceof SymTypeOfSIUnitBasic))
            return false;
        if(this.typeSymbol== null ||sym.typeSymbol==null){
            return false;
        }
        if(!this.typeSymbol.getEnclosingScope().equals(sym.typeSymbol.getEnclosingScope())){
            return false;
        }
        if (!this.getUnit().isCompatible(((SymTypeOfSIUnitBasic) sym).getUnit())) {
            return false;
        }
        return true;
    }

    public Unit getUnit() {
        return UnitFactory.createUnit(print());
    }

    public boolean isExponentPresent() {
        return exponent.isPresent();
    }

    public Integer getExponent() {
        return isExponentPresent()?exponent.get():Integer.valueOf(1);
    }

    public void setExponent(Integer exponent) {
        this.exponent = Optional.ofNullable(exponent);
    }

    public String toString() {
        return print();
    }
}