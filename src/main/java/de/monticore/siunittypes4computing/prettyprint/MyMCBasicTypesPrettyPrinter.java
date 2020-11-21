/* (c) https://github.com/MontiCore/monticore */

package de.monticore.siunittypes4computing.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.MCBasicTypesHelper;
import de.monticore.types.mcbasictypes._ast.*;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesHandler;
import de.se_rwth.commons.Names;

import java.util.Iterator;

public class MyMCBasicTypesPrettyPrinter implements MCBasicTypesHandler {

  public IndentPrinter getPrinter() {
    return printer;
  }

  public MyMCBasicTypesPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  public void setPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  // printer to use
  protected IndentPrinter printer;


  /**
   * Prints qualified names
   *
   * @param a qualified name
   */
  @Override
  public void handle(ASTMCQualifiedName a) {
    getPrinter().print(Names.getQualifiedName(a.getPartsList()));
  }


  /**
   * Prints a void type.
   *
   * @param a void type
   */
  @Override
  public void handle(ASTMCVoidType a) {
    getPrinter().print("void");
  }

  /**
   * Prints a primitive type.
   *
   * @param a primitive type
   */
  @Override
  public void handle(ASTMCPrimitiveType a) {
    getPrinter().print(MCBasicTypesHelper.primitiveConst2Name(a.getPrimitive()));
  }

  @Override
  public void handle(ASTMCImportStatement a){
    getPrinter().print("import " + a.getMCQualifiedName().toString());
    if(a.isStar()){
      getPrinter().print(".*");
    }
    getPrinter().print(";");
  }

  /**
   * Prints a list
   *
   * @param iter iterator for the list
   * @param separator string for separating list
   */
  protected void printList(Iterator<? extends ASTMCBasicTypesNode> iter, String separator) {
    // print by iterate through all items
    String sep = "";
    while (iter.hasNext()) {
      getPrinter().print(sep);
      iter.next().accept(getTraverser());
      sep = separator;
    }
  }

}
