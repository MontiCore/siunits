/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJava implements ISynthesize {

    protected TestSIJavaTraverser traverser;

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;
    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;

    public SynthesizeSymTypeFromTestSIJava() {
        init();
    }

    public void init() {
        traverser = TestSIJavaMill.traverser();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromSIUnitTypes4Computing();

        traverser.addMCBasicTypesVisitor(symTypeFromMCBasicTypes);
        traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);
        traverser.setSIUnitTypes4MathHandler(symTypeFromSIUnitTypes4Math);
        traverser.setSIUnitTypes4ComputingHandler(symTypeFromSIUnitTypes4Computing);

        setTypeCheckResult(new TypeCheckResult());
    }

    @Override
    public TestSIJavaTraverser getTraverser() {
        return traverser;
    }


    // ---------------------------------------------------------- Storage typeCheckResult

    /**
     * Storage in the Visitor: typeCheckResult of the last endVisit.
     * This attribute is synthesized upward.
     */
    protected TypeCheckResult typeCheckResult;

    @Override
    public Optional<SymTypeExpression> getResult() {
        if(typeCheckResult.isPresentCurrentResult()){
            return Optional.of(typeCheckResult.getCurrentResult());
        }else{
            return Optional.empty();
        }
    }

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }
}
