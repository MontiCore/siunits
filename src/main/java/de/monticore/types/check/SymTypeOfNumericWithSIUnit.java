/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.symboltable.serialization.JsonDeSers;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;

/**
 * SymTypeOfNumericWithSIUnit stores any kind of Numerics combined with SIUnit applied
 * to Arguments, such as <int m>, <double m/s>, <short m/s^2>, <int s/m> ...
 */
public class SymTypeOfNumericWithSIUnit extends SymTypeExpression {

    private SymTypeExpression siunitType;
    private SymTypeExpression numericType;

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfNumericWithSIUnit(TypeSymbolLoader typeSymbolLoader, SymTypeExpression numericType, SymTypeExpression siunitType) {
        this.typeSymbolLoader = typeSymbolLoader;
        this.numericType = numericType;
        this.siunitType = siunitType;
    }

    public SymTypeOfNumericWithSIUnit(ITypeSymbolsScope enclosingScope, SymTypeConstant numericType, SymTypeExpression siunitType) {
        this.numericType = numericType;
        this.siunitType = siunitType;
        this.typeSymbolLoader = new TypeSymbolLoader(print(), enclosingScope);
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        return "(" + numericType.print() + "," + siunitType.print() + ")";
    }

    public String printRealType() {
        String siUnitPrint = siunitType instanceof SymTypeOfSIUnit ?
                ((SymTypeOfSIUnit) siunitType).printDeclaredType() :
                siunitType.print();
        return "(" + numericType.print() + "," + siUnitPrint + ")";
    }

    @Override
    public String toString() {
        return printRealType();
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
        return new SymTypeOfNumericWithSIUnit(
                new TypeSymbolLoader(this.typeSymbolLoader.getName(), this.typeSymbolLoader.getEnclosingScope()),
                this.numericType.deepClone(), this.siunitType.deepClone());
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
