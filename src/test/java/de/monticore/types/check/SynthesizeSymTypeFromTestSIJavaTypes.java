package de.monticore.types.check;

import de.monticore.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.types.siunittypes._ast.ASTSIUnitType;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJavaTypes implements TestSIJavaVisitor, ISynthesize {

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

    @Override
    public TestSIJavaVisitor getRealThis() {
        return realThis;
    }

    /**
     * Storage in the Visitor: result of the last endVisit.
     * This attribute is synthesized upward.
     */
    public Optional<SymTypeExpression> result;

    public Optional<SymTypeExpression> getResult() {
        return result;
    }

    public void init() {
        result = Optional.empty();
    }

    public void endVisit(ASTSIUnitType siunittype) {
        result = Optional.of(SIUnitSymTypeExpressionFactory.createSIUnit(siunittype.toString()));
    }
}
