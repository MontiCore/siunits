# TestSIJava
The TestSIJava language serves as an example on how to integrate SIUnits, SIUnitLiterals, SIUnitTypes and 
PrimitiveWithSIUnitTypes (see [SIUnits language](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/SIUnits.md)) 
in a Java-like language. TypeCheck classes are setup for this language and can be used to check
whether a field declaration is initialized with a compatible type or expression. 

## [TestSIJava.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.mc4)
### Desciption
TestSIJava is a very limited Java-like grammar which combines expressions with SIUnits and SIUnitLiterals and each Type 
or ReturnType can be a SIUnitType or a PrimitiveWithSIUnitType. Accepted are  models with a 
package, a classname and several Field- and Method declarations. A Method contains expressions which can be used to 
assign new values to variables. In addition to "normal" variable declations of the form ```type name = assignment```,
there are wildcard SI variable declarations of the form ```si name = assignment```, where the assignment is either itself
the SIUnitType, e.g. ```si var_m = m``` or an expression of other si variables, e.g. ```si var = var_M/var_S```. For an
example model see [MyClass.sijava](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/resources/de/monticore/lang/testsijava/testsijava/MyClass.sijava).

### Functionality
A main goal of this language was to introduce the TypeCheck mechanic to a language which also uses SIUnits. So there are
a few classes which allows for this usage. (For more info on TypeChecking see [TypeCheck](https://git.rwth-aachen.de/monticore/monticore/-/tree/master/monticore-grammar/src/main/java/de/monticore/types/check))
* [TypeCheckCoCo.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_cocos/TypeCheckCoCo.java)
checks for each field assignment whether the initialized value (can be an expression) is compatible to the declared type. 
It also checks for each expression inside a method whether all assignments are valid (types are compatible). The 
[TypeCheck](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheck.java) 
class is used for that purpose. It is initialized with the custom SynthesizeSymTypeFromTestSIJava.java and 
DeriveSymTypeOfTestSIJava.java.
* [TestSIJavaScope.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaScope.java)
allows for the correct resolving of methods, types, and fields.
* [TestSIJavaSymbolTableCreator.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaSymbolTableCreator.java) 
sets the SymType for each FieldSymbol and MethodSymbol. This must be done while building the symbol table, otherwise the 
derive classes cannot know the type of a field. It also sets the enclosing scope of each node in the ast. This is 
mandatory in order for TypeChecking. In order to do so it uses the [TestSIJavaFlatExpressionScopeSetter.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaFlatExpressionScopeSetter.java)
which extends the [FlatExpressionScopeSetterAbs.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/FlatExpressionScopeSetterAbs.java)
* [SynthesizeSymTypeFromTestSIJava.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/SynthesizeSymTypeFromTestSIJava.java) 
is the custom synthesize class to synthesize the SymType of a node which extends the MCType interface. It uses the
DelegatorVisitorPattern to utilize the existing synthesize classes. It sets the same [TypeCheckResult](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheckResult.java) 
object to each of the underlying synthesize class, so they can share their results. It is mainly used in the SymbolTable
creation process to set the SymType for each declared FieldSymbol or MethodSymbol.
* [DeriveSymTypeOfTestSIJava.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/DeriveSymTypeOfTestSIJava.java) 
is the custom derive class to derive the SymType of an expression or a Literal (SignedLiteral). It uses the
DelegatorVisitorPattern to utilize the existing derive classes. It sets the same [TypeCheckResult](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheckResult.java) 
object to each of the underlying derive class, so they can share their results. It is mainly used in the TypeCheckCoco 
but also in the SymbolTable creation process to calculate the SymType of an SIVariable Declaration of the form ```si var = var_M/var_S```.

### Models
Valid models can be found [here](https://git.rwth-aachen.de/monticore/languages/siunits/-/tree/master/src/test/resources/de/monticore/lang/testsijava/testsijava).
This package also contains models which can be parsed but do not fulfil the TypeCheckCoCo.

## [TestSIJavaWithCustomTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJavaWithCustomTypes.mc4) 
This grammar has a similar purpose as the [TestSIJava](#testsijavamc4) grammar, but uses [CustomPrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/types/CustomPrimitiveWithSIUnitTypes.mc4) 
instead of the given [PrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/PrimitiveWithSIUnitTypes.mc4) 
which come with the SIUnit language.