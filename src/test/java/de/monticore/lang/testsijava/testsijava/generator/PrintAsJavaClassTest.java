/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava.generator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import de.monticore.lang.testsijava.testsijava.TestSIJavaMill;
import de.monticore.lang.testsijava.testsijava._ast.ASTSIJavaClass;
import de.monticore.lang.testsijava.testsijava._parser.TestSIJavaParser;
import de.monticore.lang.testsijava.testsijava._symboltable.TestSIJavaScopesGenitorDelegator;
import de.monticore.siunits.SIUnitsMill;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrintAsJavaClassTest {

    @Before
    public void init() {
        LogStub.init();
        Log.enableFailQuick(false);
        TestSIJavaMill.reset();
        TestSIJavaMill.init();
        SIUnitsMill.initializeSIUnits();
    }

    @Test
    public void test1() throws IOException {
        String model = "src/test/resources/test/de/monticore/lang/testsijava/testsijava/MyClass.sijava";
        
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTSIJavaClass> result = parser.parseSIJavaClass(model);
        assertFalse(parser.hasErrors());
        assertTrue(result.isPresent());
        ASTSIJavaClass ast = result.get();
    
        TestSIJavaScopesGenitorDelegator genitor = TestSIJavaMill.scopesGenitorDelegator();
        genitor.createFromAST(ast);
        
        String output = PrintAsJavaClass.printAsJavaClass(ast);
    
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser parsers = new JavaParser(configuration);
        ParseResult parseResult = parsers.parse(output);
        Assert.assertTrue(parseResult.isSuccessful());
    }

    @Test
    public void test2() throws IOException {
        String model = "src/test/resources/test/de/monticore/lang/testsijava/testsijava/Main.sijava";
        
        TestSIJavaParser parser = new TestSIJavaParser();
        Optional<ASTSIJavaClass> result = parser.parseSIJavaClass(model);
        assertFalse(parser.hasErrors());
        assertTrue(result.isPresent());
        ASTSIJavaClass ast = result.get();
    
        TestSIJavaScopesGenitorDelegator genitor = TestSIJavaMill.scopesGenitorDelegator();
        genitor.createFromAST(ast);
    
        String output = PrintAsJavaClass.printAsJavaClass(ast);
    
        ParserConfiguration configuration = new ParserConfiguration();
        JavaParser parsers = new JavaParser(configuration);
        ParseResult parseResult = parsers.parse(output);
        Assert.assertTrue(parseResult.isSuccessful());
    }
}
