package de.monticore.types.check;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.monticore.types.check.DefsTypeBasic.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeriveSymTypeOfCommonExpressionWithSIUnitTypesOnlyTest extends DeriveSymTypeOfCommonExpressionTest {

  @Override
  @Before
  public void doBefore() {
    super.doBefore();
  }

  /**
   * Focus: Deriving Type of Literals, here:
   * literals/MCLiteralsBasis.mc4
   */

  /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

  /**
   * test correctness of addition
   */
  @Test
  public void deriveFromPlusExpression() throws IOException {
    // example with siunit
    String s = "4.1[km/h] + 12[m/s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m/s", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidPlusExpression() throws IOException {
    String s = "4.1[km/h] + 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0210"));
    }
  }

  /**
   * test correctness of subtraction
   */
  @Test
  public void deriveFromMinusExpression() throws IOException {
    // example with siunit
    String s = "4.1[km/h] - 12[m/s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m/s", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidMinusExpression() throws IOException {
    String s = "4.1[km/h] - 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0213"));
    }
  }

  /**
   * test correctness of multiplication
   */
  @Test
  public void deriveFromMultExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] * 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m*s", tc.typeOf(astex).print());

    // example with siunit
    s = "4.1[km] * 12.3";
    astex = p.parse_StringExpression(s).get();
    assertEquals("m", tc.typeOf(astex).print());

    // example with siunit
    // TODO: what is the type of 3.2 m / 2m?
    s = "4.1[km] * 12.3[m^-1]";
    astex = p.parse_StringExpression(s).get();
    assertEquals("int", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidMultExpression() throws IOException {
    String s = "3 [m]*true";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0211"));
    }
  }

  /**
   * test correctness of division
   */
  @Test
  public void deriveFromDivideExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] / 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m/s", tc.typeOf(astex).print());

    // example with siunit
    s = "4.1[km] / 12.3";
    astex = p.parse_StringExpression(s).get();
    assertEquals("m", tc.typeOf(astex).print());

    // example with siunit
    s = "4[km] / 12 [m]";
    astex = p.parse_StringExpression(s).get();
    assertEquals("int", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidDivideExpression() throws IOException {
    String s = "3 [m]/true";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0212"));
    }
  }

  /**
   * tests correctness of modulo
   */
  @Test
  public void deriveFromModuloExpression() throws IOException {
    //example with two ints
    String s = "3 [m]%2[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidModuloExpression() throws IOException {
    String s = "3 [m]%2";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0214"));
    }
  }

  /**
   * test LessEqualExpression
   */
  @Test
  public void deriveFromLessEqualExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] <= 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidLessEqualExpression() throws IOException {
    String s = "4.1[km] <= 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0215"));
    }
  }

  /**
   * test GreaterEqualExpression
   */
  @Test
  public void deriveFromGreaterEqualExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] >= 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidGreaterEqualExpression() throws IOException {
    String s = "4.1[km] >= 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0216"));
    }
  }

  /**
   * test LessThanExpression
   */
  @Test
  public void deriveFromLessThanExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] < 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidLessThanExpression() throws IOException {
    String s = "4.1[km] < 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0217"));
    }
  }

  /**
   * test GreaterThanExpression
   */
  @Test
  public void deriveFromGreaterThanExpression() throws IOException {
    // example with siunit
    String s = "4.1[km] > 12[m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidGreaterThanExpression() throws IOException {
    String s = "4.1[km] > 12[s]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0218"));
    }
  }

  /**
   * initialize basic scope and a few symbols for testing
   */
  @Override
  public void init_basic() {
    super.init_basic();

    add2scope(scope, field("varM", SIUnitSymTypeExpressionFactory.createSIUnit("m", scope)));
    add2scope(scope, field("varKM", SIUnitSymTypeExpressionFactory.createSIUnit("km", scope)));
    add2scope(scope, field("varS", SIUnitSymTypeExpressionFactory.createSIUnit("s", scope)));

    derLit.setScope(scope);
  }

  /**
   * test EqualsExpression
   */
  @Test
  public void deriveFromEqualsExpression() throws IOException {
    //initialize symbol table
    init_basic();

    //example with two siunit types
    String s = "3 [km/s]==2[m^2]/1[(m*s)]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());

    //example with two siunit types
    s = "varM==varKM";
    astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidEqualsExpression() throws IOException {
    init_basic();

    String s = "varM==varS";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0219"));
    }
  }

  @Test
  public void testInvalidEqualsExpression2() throws IOException{
    init_basic();

    //person1 has the type Person, foo is a boolean
    String s = "varM==3";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0219"));
    }
  }

  /**
   * test NotEqualsExpression
   */
  @Test
  public void deriveFromNotEqualsExpression() throws IOException {
    //initialize symbol table
    init_basic();

    //example with two siunit types
    String s = "varKM/varS!=5[m^2/(m*s)]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());

    //example with two siunit types
    s = "varM!=varKM";
    astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidNotEqualsExpression() throws IOException {
    init_basic();

    String s = "varM!=varS";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0220"));
    }
  }

  @Test
  public void testInvalidNotEqualsExpression2() throws IOException{
    init_basic();
    //person1 is a Person, foo is a boolean
    String s = "varM!=3";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0220"));
    }
  }

  /**
   * test BooleanAndOpExpression
   */
  @Test
  public void deriveFromBooleanAndOpExpression() throws IOException {
    String s = "(3 [km/h]<=4[m/s]&&5>6)";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidAndOpExpression() throws IOException {
    //only possible with two booleans
    String s = "3 [km] &&true";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0223"));
    }
  }

  /**
   * test BooleanOrOpExpression
   */
  @Test
  public void deriveFromBooleanOrOpExpression() throws IOException {
    String s = "(3 [km/h]<=4[m/s]||5>6)";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("boolean", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidOrOpExpression() throws IOException {
    //only possible with two booleans
    String s = "3 [m]||true";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0226"));
    }
  }

  @Test
  public void testInvalidLogicalNotExpression() throws IOException {
    //only possible with a boolean as inner expression
    String s = "!4 [m]";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0228"));
    }
  }

  /**
   * test BracketExpression
   */
  @Test
  public void deriveFromBracketExpression() throws IOException {
    //initialize symbol table
    init_basic();

    //test with siunits
    String s = "(varS*3 [s]/(varM*varM-4 [km^2]))";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("s^2/m^2", tc.typeOf(astex).print());
  }

  /**
   * test ConditionalExpression
   */
  @Test
  public void deriveFromConditionalExpression() throws IOException {
    //initialize symbol table
    init_basic();

    //test with siunits
    String s = "3<9?varM:varKM";
    ASTExpression astex = p.parse_StringExpression(s).get();
    assertEquals("m", tc.typeOf(astex).print());
  }

  @Test
  public void testInvalidConditionalExpression() throws IOException {
    //initialize symbol table
    init_basic();

    //true and 7 are not of the same type
    String s = "3<4?varS:varM";
    ASTExpression astex = p.parse_StringExpression(s).get();
    try{
      tc.typeOf(astex);
    }catch(RuntimeException e){
      assertTrue(Log.getFindings().get(0).getMsg().startsWith("0xA0234"));
    }
  }
}
