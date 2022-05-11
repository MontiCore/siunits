/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava.TestSIJavaTool;
import de.monticore.lang.testsijava.testsijava._visitor.TestSIJavaTraverser;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Optional;

public class SynthesizeSymTypeFromTestSIJava extends AbstractSynthesize {

    private SynthesizeSymTypeFromMCBasicTypes symTypeFromMCBasicTypes;
    private SynthesizeSymTypeFromSIUnitTypes4Math symTypeFromSIUnitTypes4Math;
    private SynthesizeSymTypeFromSIUnitTypes4Computing symTypeFromSIUnitTypes4Computing;

    public SynthesizeSymTypeFromTestSIJava() {
        this(TestSIJavaMill.traverser());
    }

    public SynthesizeSymTypeFromTestSIJava(TestSIJavaTraverser traverser){
        super(traverser);
        init(traverser);
    }

    public void init(TestSIJavaTraverser traverser) {
        symTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
        symTypeFromSIUnitTypes4Math = new SynthesizeSymTypeFromSIUnitTypes4Math();
        symTypeFromSIUnitTypes4Computing = new SynthesizeSymTypeFromSIUnitTypes4Computing();

        traverser.add4MCBasicTypes(symTypeFromMCBasicTypes);
        traverser.setMCBasicTypesHandler(symTypeFromMCBasicTypes);
        traverser.setSIUnitTypes4MathHandler(symTypeFromSIUnitTypes4Math);
        traverser.setSIUnitTypes4ComputingHandler(symTypeFromSIUnitTypes4Computing);

        setTypeCheckResult(new TypeCheckResult());
    }

    /**
     * Storage in the Visitor: typeCheckResult of the last endVisit.
     * This attribute is synthesized upward.
     */

    public void setTypeCheckResult(TypeCheckResult typeCheckResult){
        this.typeCheckResult = typeCheckResult;
        this.symTypeFromMCBasicTypes.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Math.setTypeCheckResult(typeCheckResult);
        this.symTypeFromSIUnitTypes4Computing.setTypeCheckResult(typeCheckResult);
    }
}
