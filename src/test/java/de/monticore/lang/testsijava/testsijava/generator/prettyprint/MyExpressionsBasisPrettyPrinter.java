/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class MyExpressionsBasisPrettyPrinter extends ExpressionsBasisPrettyPrinter {
    public MyExpressionsBasisPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

//    @Override
//    public void handle(ASTNameExpression node) {
//        CommentPrettyPrinter.printPreComments(node, this.getPrinter());
//
//        IOOSymbolsScope enclosingScope = (IOOSymbolsScope) node.getEnclosingScope();
//        Optional<VariableSymbol> variableSymbol = enclosingScope.resolveVariable(node.getName());
//        Optional<FieldSymbol> fieldSymbol = enclosingScope.resolveField(node.getName());
//
//        SymTypeExpression type = null;
//        if (fieldSymbol.isPresent()) {
//            type = fieldSymbol.get().getType();
//        } else if(variableSymbol.isPresent()) {
//            type = variableSymbol.get().getType();
//        } else {
//            Log.error("0xE562149 Cannot find Field with name " + node.getName());
//        }
//
//        if (type instanceof SymTypeOfNumericWithSIUnit) {
//            SymTypeOfNumericWithSIUnit siUnitReturnType = (SymTypeOfNumericWithSIUnit) type;
//            double factor = UnitFactory.getConverter(
//                    UnitFactory.createBaseUnit(
//                            siUnitReturnType.getUnit()),
//                    siUnitReturnType.getUnit()).convert(1);
//            if (factor != 1) {
//                this.getPrinter().print("(" + factor + " * ");
//                this.getPrinter().print(node.getName() + ")");
//            } else
//                this.getPrinter().print(node.getName());
//        } else
//            this.getPrinter().print(node.getName());
//
//        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
//    }
}
