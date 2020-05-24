/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.testsijava.testsijava._symboltable;

import de.monticore.symboltable.IScopeSpanningSymbol;
import de.monticore.symboltable.modifiers.AccessModifier;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.MethodSymbol;
import de.monticore.types.typesymbols._symboltable.TypeSymbol;

import java.util.List;
import java.util.function.Predicate;

public class TestSIJavaScope extends TestSIJavaScopeTOP {

  public TestSIJavaScope() {
    super();
  }

  public TestSIJavaScope(boolean isShadowingScope) {
    super(isShadowingScope);
  }

  public TestSIJavaScope(ITestSIJavaScope enclosingScope) {
    this(enclosingScope, false);
  }

  public TestSIJavaScope(ITestSIJavaScope enclosingScope, boolean isShadowingScope) {
    super(enclosingScope,isShadowingScope);
  }

  /**
   * override method from ExpressionBasisScope to resolve all methods correctly
   * method needed to be overridden because of special cases: if the scope is spanned by a type symbol you have to look for fitting methods in its super types too because of inheritance
   * the method resolves the methods like the overridden method and if the spanning symbol is a type symbol it additionally looks for methods in its super types
   * it is used by the method getMethodList in SymTypeExpression
   */
  @Override
  public List<MethodSymbol> resolveMethodLocallyMany(boolean foundSymbols, String name, AccessModifier modifier,
                                                     Predicate<MethodSymbol> predicate) {
    //resolve methods by using overridden method
    List<MethodSymbol> set = super.resolveMethodLocallyMany(foundSymbols,name,modifier,predicate);
    if(this.isPresentSpanningSymbol()){
      IScopeSpanningSymbol spanningSymbol = getSpanningSymbol();
      //if the methodsymbol is in the spanned scope of a typesymbol then look for method in super types too
      if(spanningSymbol instanceof TypeSymbol){
        TypeSymbol typeSymbol = ((TypeSymbol) spanningSymbol);
        for(SymTypeExpression t : typeSymbol.getSuperTypeList()){
          set.addAll(t.getMethodList(name));
        }
      }
    }
    return set;
  }

  /**
   * override method from ExpressionBasisScope to resolve all fields correctly
   * method needed to be overridden because of special cases: if the scope is spanned by a type symbol you have to look for fitting fields in its super types too because of inheritance
   * the method resolves the fields like the overridden method and if the spanning symbol is a type symbol it additionally looks for fields in its super types
   * it is used by the method getFieldList in SymTypeExpression
   */
  @Override
  public List<FieldSymbol> resolveFieldLocallyMany(boolean foundSymbols, String name, AccessModifier modifier, Predicate predicate){
    //resolve methods by using overridden method
    List<FieldSymbol> result = super.resolveFieldLocallyMany(foundSymbols,name,modifier,predicate);
    if(this.isPresentSpanningSymbol()){
      IScopeSpanningSymbol spanningSymbol = getSpanningSymbol();
      //if the fieldsymbol is in the spanned scope of a typesymbol then look for method in super types too
      if(spanningSymbol instanceof TypeSymbol){
        TypeSymbol typeSymbol = (TypeSymbol) spanningSymbol;
        for(SymTypeExpression superType : typeSymbol.getSuperTypeList()){
          result.addAll(superType.getFieldList(name));
        }
      }
    }
    return result;
  }

  @Override
  public List<TypeSymbol> resolveTypeLocallyMany(boolean foundSymbols, String name, AccessModifier modifier, Predicate predicate){
    List<TypeSymbol> result = super.resolveTypeLocallyMany(foundSymbols,name,modifier,predicate);
    //    TODO ND: uncomment when adding inner types
    //    if(this.isPresentSpanningSymbol()){
    //      IScopeSpanningSymbol spanningSymbol = getSpanningSymbol();
    //      if(spanningSymbol instanceof TypeSymbol){
    //        TypeSymbol typeSymbol = (TypeSymbol) spanningSymbol;
    //        for(SymTypeExpression superType : typeSymbol.getSuperTypeList()){
    //          result.addAll(superType.getInnerTypeList(name));
    //        }
    //      }
    //    }
    return result;
  }
}