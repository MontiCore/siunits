# TestSIJava
The TestSIJava language serves as an example on how to integrate SIUnits, SIUnitLiterals, SIUnitTypes and 
SIUnitTypes4Computing (see [SIUnits language][SIUnitsDoc]) 
is a Java-like language. TypeCheck classes are setup for this language and can be used to check
whether a field declaration is initialized with a compatible type or expression. 

## [TestSIJava.mc4][TestSIJavaGrammar]
### Desciption
TestSIJava is a very limited Java-like grammar which combines expressions with SIUnits and SIUnitLiterals and each Type 
or ReturnType can be a SIUnitType or a SIUnitType4Computing. Accepted are  models with a 
package, a classname and several Field- and Method declarations. A Method contains expressions which can be used to 
assign new values to variables. In addition to "normal" variable declations of the form ```type name = assignment```,
there are wildcard SI variable declarations of the form ```si name = assignment```, where the assignment is either itself
the SIUnitType, e.g. ```si var_m = m``` or an expression of other si variables, e.g. ```si var = var_M/var_S```. For an
example model see [MyClass.sijava][MyClass].

### Functionality
A main goal of this language was to introduce the TypeCheck mechanic to a language which also uses SIUnits. So there are
a few classes which allows for this usage. (For more info on TypeChecking see [TypeCheck][Types])
* [TypeCheckCoCo.java][TypeCheckCoCo]
    checks for each field assignment whether the initialized value (can be an expression) is compatible to the declared type. 
    It also checks for each expression inside a method whether all assignments are valid (types are compatible). The 
    [TypeCheck][TypeCheck]
    class is used for that purpose. It is initialized with the custom ```SynthesizeSymTypeFromTestSIJava.java``` and 
    ```DeriveSymTypeOfTestSIJava.java``` (see below).
* [TestSIJavaScope.java][TestSIJavaScope]
    allows for the correct resolving of methods, types, and fields.
* [TestSIJavaSymbolTableCreator.java][TestSIJavaSymbolTableCreator] 
    sets the SymType for each FieldSymbol and MethodSymbol. This must be done while building the symbol table, otherwise the 
    derive classes cannot know the type of a field. It also sets the enclosing scope of each node in the ast. This is 
    mandatory in order for TypeChecking. In order to do so it uses the [TestSIJavaFlatExpressionScopeSetter.java][TestSIJavaFlatExpressionScopeSetter]
    which extends the [FlatExpressionScopeSetterAbs.java][FlatExpressionScopeSetterAbs]
* [SynthesizeSymTypeFromTestSIJava.java][SynthesizeSymTypeFromTestSIJava] 
    is the custom synthesize class to synthesize the SymType of a node which extends the MCType interface. It uses the
    DelegatorVisitorPattern to utilize the existing synthesize classes. It sets the same [TypeCheckResult][TypeCheckResult] 
    object to each of the underlying synthesize class, so they can share their results. It is mainly used in the SymbolTable
    creation process to set the SymType for each declared FieldSymbol or MethodSymbol.
* [DeriveSymTypeOfTestSIJava.java][DeriveSymTypeOfTestSIJava] 
    is the custom derive class to derive the SymType of an expression or a Literal (SignedLiteral). It uses the
    DelegatorVisitorPattern to utilize the existing derive classes. It sets the same [TypeCheckResult][TypeCheckResult] 
    object to each of the underlying derive class, so they can share their results. It is mainly used in the TypeCheckCoco 
    but also in the SymbolTable creation process to calculate the SymType of an SIVariable Declaration of the form ```si var = var_M/var_S```.

### Symbol Table
Each SIJavaClass spans a symbol scope. In this scope there can be the Symbols ```FieldDeclaration```, ```SIFieldDeclaration```
and ```MethodDeclaration```.
The FieldDeclaration and SIFieldDeclaration both implement ```Field``` from the ```de.monticore.types.TypeSymbols``` grammar.
The MethodDeclaration implements the ```Method``` from the ```de.monticore.types.TypeSymbols``` grammar.
The ```MethodDeclaration``` symbol again spans a scope which can contain the Symbols ```FieldDeclaration``` 
and ```SIFieldDeclaration```. It also contains the ```SIJavaParameter``` symbols which are given as the method's parameters.

In the Symbol Table creation process the [TestSIJavaSymbolTableCreator][TestSIJavaSymbolTableCreator] 
assigns each of those symbols a type as SymTypeExpression. For a ```FieldDeclaration```
this type is synthesized from the given ```MCType``` (```de.monticore.MCBasics``` grammar). For a ```SIFieldDeclaration```
it is derived from the given assignment expression or synthesized from the given assignment ```SIUnitType4Math```.
For a ```MethodDeclaration``` it is synthesized from the given ```MCReturnType``` (```de.monticore.MCBasics``` grammar).
For a ```SIJavaParameter``` it is synthesized from the given ```MCType```.

In the Symbol Table creation process, the [TestSIJavaSymbolTableCreator][TestSIJavaSymbolTableCreator]
assigns each Node in the AST the enclosing Scope, which is the last Scope on the ScopeStack, i.e. for a ```SIJavaParameter```
it is the method's scope, the ```SIJavaParameter``` is a parameter of.

### Models
Valid models can be found [here][ValidModels].
This package also contains models which can be parsed but do not fulfil the TypeCheckCoCo.

## [TestSIJavaWithCustomTypes.mc4][TestSIJavaWithCustomTypesGrammar] 
This grammar has a similar purpose as the [TestSIJava](#testsijavamc4) grammar, but uses [CustomSIUnitTypes4Computing][CustomSIUnitTypes4ComputingGrammar] 
instead of the given [SIUnitTypes4Computing][SIUnitTypes4ComputingGrammar] 
which come with the SIUnit language.

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

[TestSIJavaGrammar]: TestSIJava.mc4
[TestSIJavaWithCustomTypesGrammar]: TestSIJavaWithCustomTypes.mc4
[CustomSIUnitTypes4ComputingGrammar]: ../../CustomSIUnitTypes4Computing.mc4
[SIUnitTypes4ComputingGrammar]: ../../../../../../main/grammars/de/monticore/SIUnitTypes4Computing.mc4

[TestSIJavaScope]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaScope.java
[TestSIJavaSymbolTableCreator]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaSymbolTableCreator.java
[TestSIJavaFlatExpressionScopeSetter]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaFlatExpressionScopeSetter.java
[FlatExpressionScopeSetterAbs]: ../../../../../java/de/monticore/types/check/FlatExpressionScopeSetterAbs.java
[SynthesizeSymTypeFromTestSIJava]: ../../../../../java/de/monticore/types/check/SynthesizeSymTypeFromTestSIJava.java
[TypeCheckResult]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheckResult.java
[DeriveSymTypeOfTestSIJava]: ../../../../../java/de/monticore/types/check/DeriveSymTypeOfTestSIJava.java

[MyClass]: ../../../../../resources/test/de/monticore/lang/testsijava/MyClass.sijava
[ValidModels]: ../../../../../resources/test/de/monticore/lang/testsijava

[TypeCheckCoCo]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_cocos/TypeCheckCoCo.java
[TypeCheck]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheck.java

[SIUnitsDoc]: ../../../../../../main/grammars/de/monticore/SIUnits.md
[Types]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/Types.md