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

import de.monticore.symboltable.serialization.JsonConstants;
import de.monticore.symboltable.serialization.JsonDeSers;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;

import java.util.ArrayList;
import java.util.Optional;

/**
 * SymTypeOfSIUnit stores any kind of SIUnit applied
 * to Arguments, such as m, s, s^2, kg...
 */
public class SymTypeOfSIUnitBasic extends SymTypeExpression {

    protected Optional<Integer> exponent = Optional.of(Integer.valueOf(1));
    protected Optional<String> prefix = Optional.of("");

    public SymTypeOfSIUnitBasic(TypeSymbolLoader typeSymbolLoader) {
        this.typeSymbolLoader = typeSymbolLoader;
        this.getTypeInfo().setSuperTypeList(new ArrayList<>());
    }

    public SymTypeOfSIUnitBasic(TypeSymbolLoader typeSymbolLoader, Integer exponent) {
        this.typeSymbolLoader = typeSymbolLoader;
        this.exponent = Optional.ofNullable(exponent);
        this.getTypeInfo().setSuperTypeList(new ArrayList<>());
    }

    public String getName() {
        return typeSymbolLoader.getName();
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
        TypeSymbolLoader newTypeSymbolLoader = new TypeSymbolLoader(typeSymbolLoader.getName(), typeSymbolLoader.getEnclosingScope());
        SymTypeOfSIUnitBasic clone;
        if (!exponent.isPresent())
            clone = new SymTypeOfSIUnitBasic(newTypeSymbolLoader);
        else
            clone = new SymTypeOfSIUnitBasic(newTypeSymbolLoader, exponent.get());
        return clone;
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