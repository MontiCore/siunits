package de.monticore.types.check;

import de.monticore.symboltable.serialization.JsonConstants;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.monticore.types.typesymbols._symboltable.TypeSymbolLoader;

public class SymTypeOfPrimitiveWithSIUnit extends SymTypeExpression {

    private SymTypeExpression siunit;
    private SymTypeConstant primitive;

    /**
     * Constructor with all parameters that are stored:
     */
    public SymTypeOfPrimitiveWithSIUnit(TypeSymbolLoader typeSymbolLoader, SymTypeConstant literal, SymTypeExpression siunit) {
        this.typeSymbolLoader = typeSymbolLoader;
        this.primitive = literal;
        this.siunit = siunit;
    }

    public SymTypeOfPrimitiveWithSIUnit(ITypeSymbolsScope enclosingScope, SymTypeConstant literal, SymTypeExpression siunit) {
        this.primitive = literal;
        this.siunit = siunit;
        this.typeSymbolLoader = new TypeSymbolLoader(print(), enclosingScope);
    }

    /**
     * print: Umwandlung in einen kompakten String
     */
    @Override
    public String print() {
        return "(" + primitive.print() + "," + siunit.print() + ")";
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
        jp.member("primitiveType", primitive.print());
        jp.member("siunitType", siunit.print());
        jp.endObject();
        return jp.getContent();
    }

    @Override
    public SymTypeExpression deepClone() {
        return new SymTypeOfPrimitiveWithSIUnit(
                new TypeSymbolLoader(this.typeSymbolLoader.getName(), this.typeSymbolLoader.getEnclosingScope()),
                this.primitive.deepClone(), this.siunit.deepClone());
    }

    public SymTypeExpression getSIUnit() {
        return siunit;
    }

    public void setSiunit(SymTypeOfSIUnit siunit) {
        this.siunit = siunit;
    }

    public SymTypeConstant getPrimitive() {
        return primitive;
    }

    public void setPrimitive(SymTypeConstant primitive) {
        this.primitive = primitive;
    }
}
