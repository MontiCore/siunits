/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.siunits.utility.UnitFactory;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOSymbolsScope;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.symboltable.serialization.JsonDeSers;
import de.monticore.symboltable.serialization.JsonPrinter;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SymTypeOfSIUnit stores any kind of SIUnit applied
 * to Arguments, such as m, m/s, m/s^2, s/m...
 */

public class SymTypeOfSIUnit extends SymTypeExpression {

    private List<SymTypeOfSIUnitBasic> numerator;
    private List<SymTypeOfSIUnitBasic> denominator;

    private static SymTypeOfSIUnit superUnitType;

    public static SymTypeOfSIUnit getSuperUnitType() {
        if (superUnitType == null) {
            String name = "SuperUnit";
            OOSymbolsScope enclosingScope = new OOSymbolsScope();
            OOTypeSymbol newSymbol =  de.monticore.types.check.DefsTypeBasic.type(name);
            enclosingScope.add(newSymbol);
            TypeSymbol loader = (new TypeSymbol(name));
            loader.setEnclosingScope(enclosingScope);
            superUnitType = new SymTypeOfSIUnit(loader, new LinkedList<>(), new LinkedList<>());
        }
        return superUnitType;
    }

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfSIUnit(TypeSymbol typeSymbol, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        this.typeSymbol = typeSymbol;
        this.numerator = numerator;
        this.denominator = denominator;
        setSuperType();
    }

    public SymTypeOfSIUnit(OOSymbolsScope enclosingScope, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.typeSymbol = new TypeSymbol(print());
        this.typeSymbol.setEnclosingScope(enclosingScope);
        setSuperType();
    }

    private void setSuperType() {
        if (superUnitType != null && this != superUnitType) {
            List<SymTypeExpression> superTypes = new LinkedList<>();
            superTypes.add(getSuperUnitType());
            this.getTypeInfo().setSuperTypesList(superTypes);
        }
    }

    public boolean isDimensonless() {
        if (numerator.size() == 0 && denominator.size() == 0)
            return true;
        if (getUnit() instanceof Dimensionless)
            return true;
        return false;
    }

    public Unit getUnit() {
        return UnitFactory.createUnit(print());
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        List<String> numerators = getNumeratorList().stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.toList());
        numerators.sort(String::compareTo);
        if (getNumeratorList().isEmpty())
            numerators.add("1");
        List<String> denominators = getDenominatorList().stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.toList());
        denominators.sort(String::compareTo);
        if (denominators.size() == 0)
            return String.join("*", numerators);
        else if (denominators.size() == 1)
            return String.join("*", numerators) + "/" + String.join("*", denominators);
        else
            return String.join("*", numerators) + "/(" + String.join("*", denominators) + ")";
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
        jp.member(JsonDeSers.KIND, "de.monticore.types.check.SymTypeOfSIUnit");
        jp.beginArray("numerator");
        for (SymTypeOfSIUnitBasic exp : getNumeratorList()) {
            jp.valueJson(exp.printAsJson());
        }
        jp.endArray();
        jp.beginArray("denominator");
        for (SymTypeOfSIUnitBasic exp : getDenominatorList()) {
            jp.valueJson(exp.printAsJson());
        }
        jp.endArray();
        jp.endObject();
        return jp.getContent();
    }

    /**
     * This is a deep clone: it clones the whole structure including Symbols and Type-Info,
     * but not the name of the constructor
     *
     * @return
     */
    @Override
    public SymTypeOfSIUnit deepClone() {
        TypeSymbol loader = new TypeSymbol(typeSymbol.getName());
        loader.setEnclosingScope(this.typeSymbol.getEnclosingScope());
        return new SymTypeOfSIUnit(loader, getNumeratorList(), getDenominatorList());
    }

    @Override
    public boolean deepEquals(SymTypeExpression sym) {
        if (!(sym instanceof SymTypeOfSIUnit))
            return false;
        if(this.typeSymbol== null ||sym.typeSymbol==null){
            return false;
        }
//        if(!this.typeSymbol.getEnclosingScope().equals(sym.typeSymbol.getEnclosingScope())){
//            return false;
//        }
        if (!this.getUnit().isCompatible(((SymTypeOfSIUnit) sym).getUnit())) {
            return false;
        }
        return true;
    }

    public List<SymTypeOfSIUnitBasic> getNumeratorList() {
        return numerator;
    }

    public void setNumeratorList(List<SymTypeOfSIUnitBasic> numerator) {
        this.numerator = numerator;
    }

    public List<SymTypeOfSIUnitBasic> getDenominatorList() {
        return denominator;
    }

    public void setDenominatorList(List<SymTypeOfSIUnitBasic> denominator) {
        this.denominator = denominator;
    }
}
