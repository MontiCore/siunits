package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaDelegatorVisitor;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.lang.testsijava.testsijava.visitor.TestSIJavaBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJava extends TestSIJavaDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;
    private SynthesizeSymTypeFromPrimitiveWithSIUnitTypes symTypeFromPrimitiveWithSIUnitTypes;

    public void init() {
        typeCheckResult = new TypeCheckResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes();
        symTypeFromPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromPrimitiveWithSIUnitTypes();

        symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);
        setPrimitiveWithSIUnitTypesVisitor(symTypeFromPrimitiveWithSIUnitTypes);
        setTestSIJavaVisitor(new TestSIJavaBasicVisitor());

        setTypeCheckResult(new TypeCheckResult());
    }

    public SynthesizeSymTypeFromTestSIJava() {
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
    public TypeCheckResult typeCheckResult;

    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getLast());
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);
    }
}
