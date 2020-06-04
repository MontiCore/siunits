/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaDelegatorVisitor;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaVisitor;
import de.monticore.lang.testsijava.testsijava.visitor.TestSIJavaBasicVisitor;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJava extends TestSIJavaDelegatorVisitor
        implements ISynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;
    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;

    public void init() {
        typeCheckResult = new TypeCheckResult();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromSIUnitTypes4Computing();

        symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);

        setMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        setSIUnitTypes4MathVisitor(symTypeFromSIUnitTypes4Math);
        setSIUnitTypes4ComputingVisitor(symTypeFromSIUnitTypes4Computing);
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
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }
}
