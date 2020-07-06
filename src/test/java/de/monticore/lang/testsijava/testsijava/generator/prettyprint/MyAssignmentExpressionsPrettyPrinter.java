/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator.prettyprint;

import de.monticore.expressions.assignmentexpressions._ast.ASTAssignmentExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.AssignmentExpressionsPrettyPrinter;
import de.monticore.prettyprint.CommentPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.basictypesymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeOfNumericWithSIUnit;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.ITypeSymbolsScope;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class MyAssignmentExpressionsPrettyPrinter extends AssignmentExpressionsPrettyPrinter {
    public MyAssignmentExpressionsPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

    @Override
    public void handle(ASTAssignmentExpression node) {
        CommentPrettyPrinter.printPreComments(node, this.getPrinter());

        Optional<SymTypeExpression> type = Optional.empty();
        if (!(node.getLeft() instanceof ASTNameExpression)) {
            Log.error("0xE725687 Left side of AssignmentExpression is not a NameExpression");
        } else {
            String nameToResolve = ((ASTNameExpression) node.getLeft()).getName();
            ITypeSymbolsScope enclosingScope = (ITypeSymbolsScope) node.getEnclosingScope();
            Optional<FieldSymbol> fieldSymbol = enclosingScope.resolveField(nameToResolve);
            Optional<VariableSymbol> variable = enclosingScope.resolveVariable(nameToResolve);
            if (fieldSymbol.isPresent()) {
                type = Optional.of(fieldSymbol.get().getType());
            } else if (variable.isPresent()) {
                type = Optional.of(variable.get().getType());
            }
            this.getPrinter().print(((ASTNameExpression) node.getLeft()).getName());
        }

        this.getPrinter().print(" ");
        switch(node.getOperator()) {
            case 1:
                this.getPrinter().print("&=");
                break;
            case 2:
                this.getPrinter().print("=");
                break;
            case 3:
                this.getPrinter().print(">>=");
                break;
            case 4:
                this.getPrinter().print(">>>=");
                break;
            case 5:
                this.getPrinter().print("<<=");
                break;
            case 6:
                this.getPrinter().print("-=");
                break;
            case 7:
                this.getPrinter().print("%=");
                break;
            case 8:
                this.getPrinter().print("|=");
                break;
            case 9:
                this.getPrinter().print("+=");
                break;
            case 10:
                this.getPrinter().print("^=");
                break;
            case 11:
                this.getPrinter().print("/=");
                break;
            case 12:
                this.getPrinter().print("*=");
                break;
            default:
                Log.error("0xA0114 Missing implementation for RegularAssignmentExpression");
        }
        this.getPrinter().print(" ");
        if (type.isPresent()) {
            this.getPrinter().print("(");
            if (type.get() instanceof SymTypeOfNumericWithSIUnit)
                this.getPrinter().print(((SymTypeOfNumericWithSIUnit) type.get()).getNumericType().print());
            else
                this.getPrinter().print(type.get().print());
            this.getPrinter().print(") (");
        }

        node.getRight().accept(this.getRealThis());
        if (type.isPresent()) {
            this.getPrinter().print(")");
        }
        CommentPrettyPrinter.printPostComments(node, this.getPrinter());
    }
}
