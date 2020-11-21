/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunitliterals.prettyprint;

import de.monticore.literals.mccommonliterals._ast.*;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor2;
import de.monticore.prettyprint.IndentPrinter;

public class MyMCCommonLiteralsPrettyPrinter implements MCCommonLiteralsVisitor2 {

  private IndentPrinter printer;

  public MyMCCommonLiteralsPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  /**
   * Prints a "null" literal
   * 
   * @param a null literal
   */
  @Override
  public void visit(ASTNullLiteral a) {
    printer.print("null");
  }
  
  /**
   * Prints a boolean literal
   * 
   * @param a boolean literal
   */
  @Override
  public void visit(ASTBooleanLiteral a) {
    printer.print(a.getValue());
  }
  
  /**
   * Prints a char literal
   * 
   * @param a char literal
   */
  @Override
  public void visit(ASTCharLiteral a) {
    printer.print("'" + a.getSource() + "'");
  }
  
  /**
   * Prints a string literal
   * 
   * @param a string literal
   */
  @Override
  public void visit(ASTStringLiteral a) {
    printer.print("\"" + a.getSource() + "\"");
  }


  /**
   * Prints a natural literal
   *
   * @param a Nat literal
   */
  @Override
  public void visit(ASTNatLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a SignedNat literal
   */
  @Override
  public void visit(ASTSignedNatLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a long literal
   */
  @Override
  public void visit(ASTBasicLongLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a SignedLong literal
   */
  @Override
  public void visit(ASTSignedBasicLongLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a double literal
   */
  @Override
  public void visit(ASTBasicDoubleLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a SignedDourble literal
   */
  @Override
  public void visit(ASTSignedBasicDoubleLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a float literal
   */
  @Override
  public void visit(ASTBasicFloatLiteral a) {
    printer.print(a.getSource());
  }

  /**
   * Prints a natural literal
   *
   * @param a SignedNat literal
   */
  @Override
  public void visit(ASTSignedBasicFloatLiteral a) {
    printer.print(a.getSource());
  }


}
