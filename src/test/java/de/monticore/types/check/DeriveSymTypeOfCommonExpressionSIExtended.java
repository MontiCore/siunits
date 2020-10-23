/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

public class DeriveSymTypeOfCommonExpressionSIExtended extends DeriveSymTypeOfCommonExpressionTest {

    /**
     * Focus: Deriving Type of SIUnitLiterals, here:
     * lang/literals/SIUnitLiterals.mc4
     */

    @Override
    public void setupTypeCheck() {
        // This is an auxiliary
        ITypesCalculator derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCheck(null, derLit));
    }
}
