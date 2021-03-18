/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.io.paths.ModelPath;
import de.monticore.siunits.SIUnitsMill;
import org.junit.Before;

public class DeriveSymTypeOfCommonExpressionSIExtended extends DeriveSymTypeOfCommonExpressionTest {

    @Override
    @Before
    public void setupForEach() {
        super.setupForEach();
        CombineExpressionsWithSIUnitLiteralsMill.reset();
        CombineExpressionsWithSIUnitLiteralsMill.init();
        SIUnitsMill.initializeSIUnits();
    }

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
