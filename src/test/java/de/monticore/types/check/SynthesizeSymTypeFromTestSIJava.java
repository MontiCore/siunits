/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

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

        traverser.add4MCBasicTypes(symTypeFromMCBasicTypes);
        traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);
        traverser.setSIUnitTypes4MathHandler(symTypeFromSIUnitTypes4Math);
        traverser.setSIUnitTypes4ComputingHandler(symTypeFromSIUnitTypes4Computing);

        setTypeCheckResult(new TypeCheckResult());
    }

    public TestSIJavaTraverser getTraverser() {
        return traverser;
    }


    // ---------------------------------------------------------- Storage typeCheckResult

    /**
     * Storage in the Visitor: typeCheckResult of the last endVisit.
     * This attribute is synthesized upward.
     */
    protected TypeCheckResult typeCheckResult;

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
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
