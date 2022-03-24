/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijavawithcustomtypes.TestSIJavaWithCustomTypesMill;
import de.monticore.lang.testsijava.testsijavawithcustomtypes._visitor.TestSIJavaWithCustomTypesTraverser;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing
        implements ISynthesize {

    protected TestSIJavaWithCustomTypesTraverser traverser;

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;
    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;
    private SynthesizeSymTypeFromCustomSIUnitTypes4Computing symTypeFromCustomSIUnitTypes4Computing;


    public void init() {
        traverser = TestSIJavaWithCustomTypesMill.traverser();

        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromCustomSIUnitTypes4Computing();
        symTypeFromCustomSIUnitTypes4Computing = new SynthesizeSymTypeFromCustomSIUnitTypes4Computing();

        traverser.add4MCBasicTypes(symTypeFromMCBasicTypes);
        traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);
        traverser.setSIUnitTypes4MathHandler(symTypeFromSIUnitTypes4Math);
        traverser.setSIUnitTypes4ComputingHandler(symTypeFromSIUnitTypes4Computing);
        traverser.setCustomSIUnitTypes4ComputingHandler(symTypeFromCustomSIUnitTypes4Computing);

        setTypeCheckResult(new TypeCheckResult());
    }

    public TestSIJavaWithCustomTypesTraverser getTraverser() {
        return traverser;
    }

    public SynthesizeSymTypeFromTestSIJavaWithCustomSIUnitTypes4Computing() {
        init();
    }

    // ---------------------------------------------------------- Storage typeCheckResult

    /**
     * Storage in the Visitor: typeCheckResult of the last endVisit.
     * This attribute is synthesized upward.
     */
    protected TypeCheckResult typeCheckResult = new TypeCheckResult();

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
        this.symTypeFromCustomSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }

    @Override
    public TypeCheckResult synthesizeType(ASTMCType type) {
        typeCheckResult.reset();
        type.accept(getTraverser());
        return typeCheckResult;
    }

    @Override
    public TypeCheckResult synthesizeType(ASTMCReturnType type) {
        typeCheckResult.reset();
        type.accept(getTraverser());
        return typeCheckResult;
    }

    @Override
    public TypeCheckResult synthesizeType(ASTMCQualifiedName qName) {
        typeCheckResult.reset();
        qName.accept(getTraverser());
        return typeCheckResult;
    }
}
