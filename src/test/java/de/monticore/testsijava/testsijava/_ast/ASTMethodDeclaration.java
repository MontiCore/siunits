package de.monticore.testsijava.testsijava._ast;

public class ASTMethodDeclaration extends ASTMethodDeclarationTOP {

    public String getName() {
        return getMethodSignature().getName();
    }
}