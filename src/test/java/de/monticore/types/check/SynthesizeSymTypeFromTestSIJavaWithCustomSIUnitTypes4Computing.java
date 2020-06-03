package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesDelegatorVisitor;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesVisitor;
import de.monticore.lang.testsijava.testsijavawithcustomtypes.visitor.TestSIJavaWithCustomTypesBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing extends TestSIJavaWithCustomTypesDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;
    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;
    private SynthesizeSymTypeFromCustomSIUnitTypes4Computing symTypeFromCustomSIUnitTypes4Computing;


    public void init() {
        typeCheckResult = new TypeCheckResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromCustomSIUnitTypes4Computing();
        symTypeFromCustomSIUnitTypes4Computing = new SynthesizeSymTypeFromCustomSIUnitTypes4Computing();

        symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
        symTypeFromCustomSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypes4MathVisitor(symTypeFromSIUnitTypes4Math);
        setSIUnitTypes4ComputingVisitor(symTypeFromSIUnitTypes4Computing);
        setCustomSIUnitTypes4ComputingVisitor(symTypeFromCustomSIUnitTypes4Computing);
        setTestSIJavaWithCustomTypesVisitor(new TestSIJavaWithCustomTypesBasicVisitor());

        setTypeCheckResult(new TypeCheckResult());
    }

    public SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing() {
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
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
        this.symTypeFromCustomSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }
}
