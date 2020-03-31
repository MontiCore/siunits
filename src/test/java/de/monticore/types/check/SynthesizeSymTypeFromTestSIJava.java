package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._symboltable.IExpressionsBasisScope;
import de.monticore.testsijava.testsijava._visitor.TestSIJavaDelegatorVisitor;
import de.monticore.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.testsijava.testsijava.visitor.TestSIJavaBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJava extends TestSIJavaDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;
    private SynthesizeSymTypeFromPrimitiveWithSIUnitTypes symTypeFromPrimitiveWithSIUnitTypes;

    public void init() {
        lastResult = new LastResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes(scope);
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes(scope);
        symTypeFromPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromPrimitiveWithSIUnitTypes(scope);

        symTypeFromMCBasicTypes.setLastResult(lastResult);
        symTypeFromSIUnitTypes.setLastResult(lastResult);
        symTypeFromPrimitiveWithSIUnitTypes.setLastResult(lastResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);
        setPrimitiveWithSIUnitTypesVisitor(symTypeFromPrimitiveWithSIUnitTypes);
        setTestSIJavaVisitor(new TestSIJavaBasicVisitor());
    }


    /**
     * Using the visitor functionality to calculate the SymType Expression
     */

    protected IExpressionsBasisScope scope;

    public SynthesizeSymTypeFromTestSIJava(IExpressionsBasisScope scope) {
        this.scope = scope;
        init();
    }

    // ----------------------------------------------------------  realThis start
    // setRealThis, getRealThis are necessary to make the visitor compositional
    //
    // (the Vistors are then composed using theRealThis Pattern)
    //
    TestSIJavaVisitor realThis = this;

    @Override
    public void setRealThis(TestSIJavaVisitor realThis) {
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
