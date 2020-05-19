package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesDelegatorVisitor;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.visitor.TestSIJavaWithCustomTypesBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes extends TestSIJavaWithCustomTypesDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes symTypeFromSIUnitTypes;
    private SynthesizeSymTypeFromPrimitiveWithSIUnitTypes symTypeFromPrimitiveWithSIUnitTypes;
    private SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes symTypeFromCustomPrimitiveWithSIUnitTypes;


    public void init() {
        typeCheckResult = new TypeCheckResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes = new SynthesizeSymTypeFromSIUnitTypes();
        symTypeFromPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes();
        symTypeFromCustomPrimitiveWithSIUnitTypes = new SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes();

        symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromCustomPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypesVisitor(symTypeFromSIUnitTypes);
        setPrimitiveWithSIUnitTypesVisitor(symTypeFromPrimitiveWithSIUnitTypes);
        setCustomPrimitiveWithSIUnitTypesVisitor(symTypeFromCustomPrimitiveWithSIUnitTypes);
        setTestSIJavaWithCustomTypesVisitor(new TestSIJavaWithCustomTypesBasicVisitor());

        setTypeCheckResult(new TypeCheckResult());
    }

    public SynthesizeSymTypeFromTestSIJavaWithCustomPrimitiveWithSIUnitTypes() {
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
    public TypeCheckResult typeCheckResult = new TypeCheckResult();

    public Optional<SymTypeExpression> getResult() {
        return Optional.of(typeCheckResult.getLast());
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromCustomPrimitiveWithSIUnitTypes.setTypeCheckResult(typeCheckResult);
    }
}
