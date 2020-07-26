/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator2.prettyprint;

import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.siunits.utility.UnitFactory;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;
import de.se_rwth.commons.logging.Log;

import javax.measure.unit.Unit;
import java.util.Optional;

public class MyExpressionsBasisPrettyPrinter extends ExpressionsBasisPrettyPrinter {
    public MyExpressionsBasisPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

    @Override
    public void handle(ASTNameExpression node) {
        CommentPrettyPrinter.printPreComments(node, this.getPrinter());

        IOOSymbolsScope enclosingScope = (IOOSymbolsScope) node.getEnclosingScope();
        Optional<VariableSymbol> variableSymbol = enclosingScope.resolveVariable(node.getName());
        Optional<FieldSymbol> fieldSymbol = enclosingScope.resolveField(node.getName());

        Optional<SymTypeExpression> type = Optional.empty();
        if (fieldSymbol.isPresent()) {
            type = Optional.ofNullable(fieldSymbol.get().getType());
        } else if(variableSymbol.isPresent()) {
            type = Optional.ofNullable(variableSymbol.get().getType());
        } else {
            Log.error("0xE562149 Cannot find Field with name " + node.getName());
        }

        if (type.isPresent() && type.get() instanceof SymTypeOfNumericWithSIUnit) {
            Unit unit = ((SymTypeOfNumericWithSIUnit) type.get()).getUnit();
            double factor = UnitFactory.getConverterFrom(unit).convert(1);
            if (factor != 1) {
                this.getPrinter().print("(" + factor + " * ");
                this.getPrinter().print(node.getName() + ")");
            } else
                this.getPrinter().print(node.getName());
        } else
            this.getPrinter().print(node.getName());

        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
