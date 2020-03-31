package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesDelegatorVisitor;
import de.monticore.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.testsijava.testsijavawithcustomtypes.visitor.TestSIJavaWithCustomTypesBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJavaWithCustomTypes extends TestSIJavaWithCustomTypesDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;
    private SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes symTypeFromCustomPrimitiveWithSIUnitTypes;


    public void init() {
        lastResult = new LastResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes(scope);
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes(scope);
        symTypeFromCustomPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes(scope);

        symTypeFromMCBasicTypes.setLastResult(lastResult);
        symTypeFromSIUnitTypes.setLastResult(lastResult);
        symTypeFromCustomPrimitiveWithSIUnitTypes.setLastResult(lastResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);
        setCustomPrimitiveWithSIUnitTypesVisitor(symTypeFromCustomPrimitiveWithSIUnitTypes);
        setTestSIJavaWithCustomTypesVisitor(new TestSIJavaWithCustomTypesBasicVisitor());
    }


    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    protected IExpressionsBasisScope scope;

    public SynthesizeSymTypeFromTestSIJavaWithCustomTypes(IExpressionsBasisScope scope) {
        this.scope = scope;
        init();
    }

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    TestSIJavaWithCustomTypesVisitor realThis = this;

    @Override
    public void setRealThis(TestSIJavaWithCustomTypesVisitor realThis) {
        this.realThis = realThis;
    }

    // ---------------------------------------------------------- Storage result

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    public LastResult lastResult = new LastResult();

    public Optional<SymTypeExpression> getResult() {
        return Optional.of(lastResult.getLast());
    }

    public void setLastResult(LastResult lastResult){
        this.lastResult = lastResult;
    }
}
