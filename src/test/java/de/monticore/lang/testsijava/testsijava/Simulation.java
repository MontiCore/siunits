/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava;

import de.monticore.expressions.assignmentexpressions._ast.ASTAssignmentExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.lang.testsijava.testsijava._ast.*;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.SIJavaClassSymbol;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._ast.ASTVariableDeclaration;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.MethodSymbol;
import de.se_rwth.commons.logging.Log;

import javax.measure.unit.Unit;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Simulation {

    Unit lastUnit = Unit.ONE;
    double lastValue = 0;

    Map<FieldSymbol, Double> fieldValues = new HashMap<>();

    public void simulate(String modelPath, String modelName, String methodName) {
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTSIJavaClass> astsiJavaClass = null;
        try {
            astsiJavaClass = parser.parseSIJavaClass(modelPath + "/" + modelName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ASTSIJavaClass siclass = astsiJavaClass.orElse(null);
        siclass.accept(TestSIJavaMill.testSIJavaSymbolTableCreatorBuilder().build());

        for (FieldSymbol fieldSymbol : siclass.getSymbol().getSpannedScope().getFieldSymbols().values()) {
            ASTFieldDeclaration fieldDeclaration = (ASTFieldDeclaration) fieldSymbol.getAstNode();
            if (!fieldDeclaration.isPresentExpression())
                fieldValues.put(fieldSymbol, 0.0);
            else {
                double val = Evalulator.evaluate(fieldDeclaration.getExpression());
                fieldValues.put(fieldSymbol, val);
            }
        }

        simulate(siclass.getSymbol(), methodName);
    }

    private void simulate(SIJavaClassSymbol siclass, String methodName) {
        Optional<MethodSymbol> methodSymbolOpt = siclass.getSpannedScope().resolveMethod(methodName);
        if (!methodSymbolOpt.isPresent())
            Log.error("0xE549012 No such method: " + methodName);
        MethodSymbol methodSymbol = methodSymbolOpt.get();

        for (ASTSIJavaMethodStatement statement : ((ASTMethodDeclaration) methodSymbol.getAstNode()).getSIJavaMethodStatementList()) {
            FieldSymbol fieldSymbol;
            ASTExpression expression;
            if (statement instanceof ASTVariableDeclaration) {
                fieldSymbol = ((ASTVariableDeclaration) statement).getSymbol();
                expression = ((ASTVariableDeclaration) statement).getAssignment();
            } else {
                ASTAssignmentExpression assignmentExpression = (ASTAssignmentExpression) ((ASTSIJavaMethodExpression) statement).getExpression();
                if (!(assignmentExpression.getLeft() instanceof ASTNameExpression)) {
                    Log.error ("0xE6809644 there is no variable name on left side of the assignment expression");
                }
                Optional<FieldSymbol> fieldSymbolOpt =
                        methodSymbol.getSpannedScope().resolveField(
                        ((ASTNameExpression) assignmentExpression.getLeft()).getName());
                if (!fieldSymbolOpt.isPresent()) {
                    Log.error("0xE6809645 cannot find variable");
                }
                fieldSymbol = fieldSymbolOpt.get();
                expression = ((ASTAssignmentExpression) statement).getRight();
            }
            if (expression == null)
                fieldValues.put(fieldSymbol, 0.0);
            else {
                double val = Evalulator.evaluate(expression);
                fieldValues.put(fieldSymbol, val);
            }
        }
    }
}
