/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

public class DeriveSymTypeOfExpressionSIExtended extends DeriveSymTypeOfExpressionTest {

    @Override
    public void setupTypeCheck() {
        // This is an auxiliary
        AbstractDerive derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCalculator(null, derLit));
    }
}
