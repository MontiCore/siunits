/* (c) https://github.com/MontiCore/monticore */
package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._symboltable.CombineExpressionsWithLiteralsGlobalScope;
import de.monticore.expressions.combineexpressionswithliterals._symboltable.ICombineExpressionsWithLiteralsGlobalScope;
import de.monticore.literals.mccommonliterals.MCCommonLiteralsMill;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DeriveSymTypeOfMCCommonLiteralsTest {

  /**
   * Focus: Deriving Type of Literals, here:
   *    literals/MCLiteralsBasis.mc4
   */

  @Before
  public void init() {
    LogStub.init();
    Log.enableFailQuick(false);
    CombineExpressionsWithLiteralsMill.reset();
    CombineExpressionsWithLiteralsMill.init();
    BasicSymbolsMill.initializePrimitives();
  }

  // This is the core Visitor under Test (but rather empty)
  FullDeriveFromCombineExpressionsWithLiterals derLit = new FullDeriveFromCombineExpressionsWithLiterals();

  // other arguments not used (and therefore deliberately null)

  // This is the TypeChecker under Test:
  TypeCalculator tc = new TypeCalculator(null,derLit);

  // ------------------------------------------------------  Tests for Function 2b

  // Mill used ... alternative would be a Parser for Literals
  @Test
  public void deriveTFromLiteral1Null() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.nullLiteralBuilder().build();
    assertEquals("null", tc.typeOf(lit).print());
  }

  @Test
  public void deriveTFromLiteral1Boolean() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.booleanLiteralBuilder().setSource(0).build();
    assertEquals("boolean", tc.typeOf(lit).print());
  }

  @Test
  public void deriveTFromLiteral1Char() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.charLiteralBuilder().setSource("c").build();
    assertEquals("char", tc.typeOf(lit).print());
  }

  @Test
  public void deriveTFromLiteral1String() throws IOException {
    ICombineExpressionsWithLiteralsGlobalScope gs = CombineExpressionsWithLiteralsMill.globalScope();
    TypeSymbol str = CombineExpressionsWithLiteralsMill.typeSymbolBuilder()
      .setName("String")
      .setEnclosingScope(gs)
      .setSpannedScope(CombineExpressionsWithLiteralsMill.scope())
      .build();
    gs.add(str);
    ASTLiteral lit = MCCommonLiteralsMill.stringLiteralBuilder().setSource("hjasdk").build();
    lit.setEnclosingScope(gs);
    assertEquals("String", tc.typeOf(lit).print());
  }

  @Test
  public void deriveTFromLiteral1Int() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.natLiteralBuilder().setDigits("17").build();
    assertEquals("int", tc.typeOf(lit).print());
  }

  @Test
  public void deriveTFromLiteral1BasicLong() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.basicLongLiteralBuilder().setDigits("17").build();
    assertEquals("long", tc.typeOf(lit).print());
  }


  @Test
  public void deriveTFromLiteral1BasicFloat() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.basicFloatLiteralBuilder().setPre("10").setPost("03").build();
    assertEquals("float", tc.typeOf(lit).print());
  }


  @Test
  public void deriveTFromLiteral1BasicDouble() throws IOException {
    ASTLiteral lit = MCCommonLiteralsMill.basicDoubleLiteralBuilder().setPre("710").setPost("93").build();
    assertEquals("double", tc.typeOf(lit).print());
  }

}
