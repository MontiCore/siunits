/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.lang.siunits.utility.UnitFactory;
import de.monticore.symboltable.serialization.JsonConstants;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.monticore.types.typesymbols._symboltable.TypeSymbolsScope;

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

    private List<SymTypeOfSIUnitBasic> numerator = new LinkedList<>();
    private List<SymTypeOfSIUnitBasic> denominator = new LinkedList<>();

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfSIUnit(TypeSymbolLoader typeSymbolLoader, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        this.typeSymbolLoader = typeSymbolLoader;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public SymTypeOfSIUnit(TypeSymbolsScope enclosingScope, List<SymTypeOfSIUnitBasic> numerator, List<SymTypeOfSIUnitBasic> denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.typeSymbolLoader = new TypeSymbolLoader(print(), enclosingScope);
    }

    public boolean isDimensonless() {
        if (numerator.size() == 0 && denominator.size() == 0)
            return true;
        if (getUnit() instanceof Dimensionless)
            return true;
        return false;
    }

    // should never happen
    public boolean isOne() {
        if (numerator.size() == 0 && denominator.size() == 0)
            return true;
        if ("1".equals(getUnit().toString()))
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
        if (getNumeratorList().size() == 0)
            numerators.add("1");
        List<String> denominators = getDenominatorList().stream().map(SymTypeOfSIUnitBasic::print).collect(Collectors.toList());
        if (denominators.size() == 0)
            return String.join("*", numerators);
        else if (denominators.size() == 1)
            return String.join("*", numerators) + "/" + String.join("*", denominators);
        else
            return String.join("*", numerators) + "/(" + String.join("*", denominators) + ")";
    }

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
        jp.member(JsonConstants.KIND, "de.monticore.types.check.SymTypeOfSIUnit");
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
        return new SymTypeOfSIUnit(new TypeSymbolLoader(typeSymbolLoader.getName(), typeSymbolLoader.getEnclosingScope()), getNumeratorList(), getDenominatorList());
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
