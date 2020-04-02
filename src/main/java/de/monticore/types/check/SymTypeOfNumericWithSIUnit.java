package de.monticore.types.check;

import de.monticore.symboltable.serialization.JsonConstants;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;
import de.se_rwth.commons.logging.Log;

public class SymTypeOfNumericWithSIUnit extends SymTypeExpression {

    private SymTypeExpression siunitType;
    private SymTypeConstant numericType;

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfNumericWithSIUnit(TypeSymbolLoader typeSymbolLoader, SymTypeConstant numericType, SymTypeExpression siunitType) {
        init(typeSymbolLoader, numericType, siunitType);
    }

    public SymTypeOfNumericWithSIUnit(ITypeSymbolsScope enclosingScope, SymTypeConstant numericType, SymTypeExpression siunitType) {
        init(new TypeSymbolLoader(print(), enclosingScope), numericType, siunitType);
    }

    private void init(TypeSymbolLoader typeSymbolLoader, SymTypeConstant numericType, SymTypeExpression siunitType) {
        if (!numericType.isNumericType()) {
            Log.error("0x SymTypeConstant must be numeric type");
        }
        this.typeSymbolLoader = typeSymbolLoader;
        this.numericType = numericType;
        this.siunitType = siunitType;
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        return "(" + numericType.print() + "," + siunitType.print() + ")";
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
        jp.member(JsonConstants.KIND, "de.monticore.types.check.SymTypeOfSIUnitWithLiteral");
        jp.member("numericType", numericType.print());
        jp.member("siunitType", siunitType.print());
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

    public SymTypeConstant getNumericType() {
        return numericType;
    }

    public void setNumericType(SymTypeConstant numericType) {
        this.numericType = numericType;
    }
}
