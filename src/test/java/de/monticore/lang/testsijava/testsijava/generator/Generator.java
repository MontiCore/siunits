/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator;

import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.se_rwth.commons.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class Generator {

    public static void generate(String modelPath, String fullName, String outputPath) {
        String name = fullName
                .substring(0, fullName.lastIndexOf("."))
                .replace(".","/")
                .replace("\\","/")
                + ".sijava";
        ASTSIJavaClass ast = parseModel(modelPath, name);
        try {
            ast.accept(TestSIJavaMill.testSIJavaSymbolTableCreatorBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("0xE6548322 Cannot build symbol table");
        }
        String print = PrintAsJavaClass.printAsJavaClass(ast);

        String filePath = outputPath;
        String className = "";
        if (fullName.contains("/")) {
            className = fullName.substring(fullName.lastIndexOf("/") + 1, fullName.lastIndexOf("."));
            filePath += "/" + fullName.substring(0, fullName.lastIndexOf("/"));
        }

        File pathFile = new File(filePath);
        if (!pathFile.exists())
            pathFile.mkdirs();

        try {
            FileWriter writer = new FileWriter(filePath + "/" + className + ".java");
            writer.write(print);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ASTSIJavaClass parseModel(String modelPath, String name) {
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTSIJavaClass> res = Optional.empty();
        try {
            res = parser.parseSIJavaClass(modelPath + "/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!res.isPresent()) {
            Log.error("0xE6548321 Cannot find or parse class " + modelPath + "/" + name);
        }
        return res.get();
    }
}
