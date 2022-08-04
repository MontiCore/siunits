/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check.cocos;

import de.monticore.ast.ASTNode;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.symbols.basicsymbols._ast.ASTFunction;
import de.monticore.symbols.basicsymbols._ast.ASTVariable;
import de.monticore.symbols.oosymbols._ast.ASTField;
import de.monticore.symbols.oosymbols._ast.ASTMethod;
import de.monticore.types.check.*;
import de.se_rwth.commons.logging.Log;

/**
 * Provides methods to check for incompatible types in assignment expressions,
 *  Field or Variable declarations, and Method or Function declarations.
 * Take note, that this CoCo requires the SymbolTableCreator to set the following:
 *  1. The enclosing scope for each Expression,
 *   Literal, SignedLiteral, MCType and MCReturnType
 *  2. The {@link SymTypeExpression} for each VariableSymbol, FieldSymbol, FunctionSymbol, and MethodSymbol
 */
public abstract class TypeCheckCoCo {

    protected TypeCalculator tc;

    /**
     * Creates an instance of TypeCheckCoCo
     * @param typeCheck a {@link TypeCheck} object instantiated with the correct
     *                  {@link ISynthesize} and {@link IDerive} objects of
     *                  the current language
     */
    public TypeCheckCoCo(TypeCalculator typeCheck) {
        this.tc = typeCheck;
    }

    /**
     * Checks whether the type of the {@link ASTExpression} can be calculated
     * @param node the {@link ASTExpression} to check
     */
    protected void checkExpression(ASTExpression node) {
        try {
            // should throw an exception if there are incompatible types
            SymTypeExpression symTypeExpression = tc.typeOf(node);
            if (symTypeExpression == null) logError(node, "Incompatible types in AssignmentExpression");
        } catch (Exception e) {
            logError(node, "Incompatible types");
        }
    }

    /**
     * Checks whether a {@link ASTVariable} or {@link ASTField} has a type and whether
     *  an assigned expression has a compatible type
     * @param node the {@link ASTVariable} or {@link ASTField} to check
     * @param assignmentExpression the assigned {@link ASTExpression}, can be null if none is available
     */
    protected void checkFieldOrVariable(ASTVariable node, ASTExpression assignmentExpression) {
        if (assignmentExpression == null)
            checkFieldOrVariable(node, (SymTypeExpression) null);
        else {
            SymTypeExpression returnType = null;
            try {
                returnType = tc.typeOf(assignmentExpression);
                if (returnType == null || returnType.isObscureType())
                    logError(assignmentExpression, "Could not calculate type of assigned expression");
            } catch (Exception e) {
                logError(assignmentExpression, "Could not calculate type of assigned expression");
            }
            if (returnType != null)
                checkFieldOrVariable(node, returnType);
        }
    }

    /**
     * Checks whether a {@link ASTVariable} or {@link ASTField} has a type and whether an assigned type is compatible
     * @param node the {@link ASTVariable} or {@link ASTField} to check
     * @param assignmentType the assigned {@link SymTypeExpression}, can be null if none is available
     */
    protected void checkFieldOrVariable(ASTVariable node, SymTypeExpression assignmentType) {
        if (!node.isPresentSymbol())
            logError(node, "Variable symbol not present");
        else if (node.getSymbol().getType() == null)
            logError(node, "Variable symbol has no type");
        else if (assignmentType != null && (assignmentType.isObscureType() || !TypeCheck.compatible(node.getSymbol().getType(), assignmentType)))
            logError(node, String.format("Variable type %s incompatible to assigned type %s",
                    node.getSymbol().getType().print(), assignmentType.print()));
    }

    /**
     * Checks whether a {@link ASTFunction} or {@link ASTMethod} has a type and whether
     *  a return expression has a compatible type
     * @param node the {@link ASTFunction} or {@link ASTMethod} to check
     * @param returnExpression the return {@link ASTExpression}, can be null if none is available
     */
    protected void checkMethodOrFunction(ASTFunction node, ASTExpression returnExpression) {
        if (returnExpression == null)
            checkMethodOrFunction(node, (SymTypeExpression) null);
        else {
            SymTypeExpression returnType = null;
            try {
                returnType = tc.typeOf(returnExpression);
                if (returnType == null)
                    logError(returnExpression, "Could not calculate type of return expression");
            } catch (Exception e) {
                logError(returnExpression, "Could not calculate type of return expression");
            }
            if (returnType != null)
                checkMethodOrFunction(node, returnType);
        }
    }

    /**
     * Checks whether a {@link ASTFunction} or {@link ASTMethod} has a type and whether a return type is compatible
     * @param node the {@link ASTFunction} or {@link ASTMethod} to check
     * @param returnType the return {@link SymTypeExpression}, can be null if none is available
     */
    protected void checkMethodOrFunction(ASTFunction node, SymTypeExpression returnType) {
        if (!node.isPresentSymbol())
            logError(node, "Function symbol not present");
        else if (node.getSymbol().getType() == null)
            logError(node, "Function symbol has no return type");
        else {
            if (node.getSymbol().getType().isVoidType()) {
                if (returnType != null && !returnType.isVoidType())
                    logError(node, String.format("Return type void incompatible to actual return type %s",
                            returnType.print()));
            } else if (returnType == null)
                logError(node, "No return type given");
            else if (!TypeCheck.compatible(node.getSymbol().getType(), returnType))
                logError(node, String.format("Return type %s incompatible to actual return type %s",
                        node.getSymbol().getType().print(), returnType.print()));
        }
    }

    protected void logError(ASTNode node, String msg) {
        Log.error("0xE3810 " + node.get_SourcePositionStart() + " " + msg);
    }
}
