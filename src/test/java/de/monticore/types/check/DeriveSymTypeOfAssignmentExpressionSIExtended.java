/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;


public class DeriveSymTypeOfAssignmentExpressionSIExtended extends DeriveSymTypeOfAssignmentExpressionTest {

    @Override
    public void setupTypeCheck() {
        // This is an auxiliary
        ITypesCalculator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCheck(null, derLit));
    }
}
