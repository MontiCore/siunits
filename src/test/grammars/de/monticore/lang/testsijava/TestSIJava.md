<!--   
    TODO: Add generator description
-->

# TestSIJava
The TestSIJava language serves as an example on how to integrate [SIUnits][SIUnitGrammar], 
[SIUnitLiterals][SIUnitLiteralsGrammar], [SIUnitTypes4Math][SIUnitTypes4MathGrammar] and 
[SIUnitTypes4Computing][SIUnitTypes4ComputingGrammar] (see [SIUnits language][SIUnitsDoc]) 
and is a Java-like language. TypeCheck classes are setup for this language and can be used to 
check type compatibilities of common types and SI unit types. A generator takes models from
the language and computes java classes.

## [TestSIJava.mc4][TestSIJavaGrammar]
### Desciption
TestSIJava is a very limited Java-like grammar which combines expressions with SIUnits and SIUnitLiterals and each Type 
or ReturnType can be a SIUnitType or a SIUnitType4Computing. The language accepts models with a 
package, a classname and several Field- and Method declarations. A Method can have a return type and a return value and 
contains expressions which can be used to assign new values to variables.

### Functionality
An important goal of this language was to introduce the [TypeCheck][Types] mechanic to a language which also uses 
SIUnits. So there are a few classes which allows for this usage. (For more info on TypeChecking see [TypeCheck][Types])

* [TypeCheckCoCo.java][TypeCheckCoCo]
    checks for each field assignment whether the initialized value (can be an expression) is compatible to the 
    declared type. For each method the declared type and its return type is checked for compatibility.
    It also checks for each expression inside a method whether all assignments are valid (types are compatible). The 
    [TypeCheck][TypeCheck] class is used for that purpose. 
    It is initialized with the custom `SynthesizeSymTypeFromTestSIJava.java` and 
    `DeriveSymTypeOfTestSIJava.java` (see below).
* [TestSIJavaScope.java][TestSIJavaScope]
    allows for the correct resolving of methods, types, and fields.
* [TestSIJavaSymbolTableCreator.java][TestSIJavaSymbolTableCreator] 
    sets the [SymTypeExpression] for each FieldSymbol and MethodSymbol. This must be done while building the symbol table, otherwise 
    the derive classes do not know a symbol's type. It also sets the enclosing scope of each node in the ast. This is 
    mandatory in order for TypeChecking. In order to do so it uses the [TestSIJavaFlatExpressionScopeSetter.java][TestSIJavaFlatExpressionScopeSetter]
    which extends the [FlatExpressionScopeSetterAbs.java][FlatExpressionScopeSetterAbs]
* [SynthesizeSymTypeFromTestSIJava.java][SynthesizeSymTypeFromTestSIJava] 
    is the custom synthesize class to synthesize the [SymTypeExpression] of a node which extends the MCType interface. It uses the
    DelegatorVisitorPattern to utilize the existing synthesize classes. It sets the same [TypeCheckResult][TypeCheckResult] 
    object to each of the underlying synthesize classes, so they can share their results. It is mainly used in the SymbolTable
    creation process to set the [SymTypeExpression] for each declared FieldSymbol or MethodSymbol.
* [DeriveSymTypeOfTestSIJava.java][DeriveSymTypeOfTestSIJava] 
    is the custom derive class to derive the [SymTypeExpression] of an expression or a Literal (SignedLiteral). It uses the
    DelegatorVisitorPattern to utilize the existing derive classes. It sets the same [TypeCheckResult][TypeCheckResult] 
    object to each of the underlying derive class, so they can share their results. It is mainly used in the TypeCheckCoco 
    but also in the SymbolTable creation process to calculate the [SymTypeExpression] of an SIVariable Declaration of the form `si var = var_M/var_S`.

### Generator
The second important goal of this language was the implementation of a working generator that can handle SI units.
The generator presented here takes a valid model and _prettyprints_ it to a java file. For SI unit types there
are a few conversions to be done:
* Variable declaration and assignment
    * `km varKM = 3 km;` &rarr; `double varKM = 3`
        * `3 km` are converted to type `km`
    * `km varKM = 3 m;` &rarr; to `double varKM = 3 / 1000;`
        * `3 m` are converted to type `km`
    * `varKM = 4500 mm;` &rarr; `varKM = 4500 / 1000000;`
        * `4500 mm` are converted to type `km`
    * `m varM = varKM;` &rarr; `double varM = varKM * 1000;`
        * `varKM` is converted to type `m`
* Method calls
    * `mm method1 (mm varMM) { ... return 3 m; }` &rarr; `public double method1 (double varMM) { ... return 3 * 1000; }`
        * `3 m` is converted to return type `mm`
    * `m varM = method1 (3 km);` &rarr; `double varM = method1 (3 * 1000000) / 1000;`
        * `method1` is converted to type `m` (`/ 1000`) and the argument `3 km` is converted to
        the expected parameter type `mm` (`* 1000000`)
* Expressions
    * Multiplication and Division is converted as is.
    * Addition, Substraction and Modulo is converted as follows: Convert to the type which has the larger number
    as double: `20 min + 2 h` &rarr; `20 + 2 * 60` (and has type `min`)
        * `100 km / ((2 h + 20 min) + 5 s)` &rarr; `100 / ((2 * 60 + 20) * 60 + 5)` (and has type `km/s`)
    
### Symbol Table
Each SIJavaClass spans a symbol scope. In this scope there can be the Symbols `FieldDeclaration` and `MethodDeclaration`.
The FieldDeclaration implements `Field` from the [de.monticore.symbols.BasicSymbols][BasicSymbolsGrammar] grammar.
The MethodDeclaration implements `Method` from the `de.monticore.types.TypeSymbols` grammar.
The `MethodDeclaration` symbol spans a scope which contains its given `SIJavaParameter` symbols. This scope again
spans a subscope with several `FieldDeclaration` and `SIJavaMethodExpression` symbols.

In the Symbol Table creation process the [TestSIJavaSymbolTableCreator][TestSIJavaSymbolTableCreator] 
assigns each of those symbols a type as [SymTypeExpression]. For a `FieldDeclaration`
this type is synthesized from the given `MCType` ([de.monticore.symbols.BasicSymbols][BasicSymbolsGrammar] grammar). 
For a `MethodDeclaration` it is synthesized from the given `MCReturnType` (`de.monticore.MCBasics` grammar).
For a `SIJavaParameter` it is synthesized from the given `MCType`.

In the Symbol Table creation process each Node in the AST is assigned its enclosing Scope, which is the last Scope 
on the ScopeStack, i.e. for a `SIJavaParameter` it is its method's scope.

In addition the SIJavaClass' spanned scope has 6 new method symbols:
* `void print(double)` and `void print(NumericWithSIUnitType)` which takes an expression and has no return type
    * These functions should print the given expression, e.g. `print(3.0) -> 3.0`, `print(3.0 km) -> 3.0km`
* `double value(double)` and `double value(NumericWithSIUnitType)` which takes an expressoin and has `double`
 as return type
    * These functions should give the value of a expression as double, 
    e.g. `value(3.0) -> 3.0`, `value(3.0 km) -> 3.0`
* `double basevalue(double)` and `double basevalue(NumericWithSIUnitType)` which takes an expressoin and has
 `double` as return type
    * These functions should give the value of a expression of its base unit as double, 
    e.g. `basevalue(3.0) -> 3.0`, `basevalue(3.0 km) -> 3000.0`

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

<!--   
    TODO:  Die MontiCore Links sind alle noch absolut:
    TypeCheckResult
    TypeCheck
    Types
-->

[SIUnitGrammar]: ../../../../../../main/grammars/de/monticore/SIUnits.mc4
[SIUnitLiteralsGrammar]: ../../../../../../main/grammars/de/monticore/SIUnitLiterals.mc4
[SIUnitTypes4MathGrammar]: ../../../../../../main/grammars/de/monticore/SIUnitTypes4Math.mc4
[SIUnitTypes4ComputingGrammar]: ../../../../../../main/grammars/de/monticore/SIUnitTypes4Computing.mc4
[TestSIJavaGrammar]: TestSIJava.mc4
[TestSIJavaWithCustomTypesGrammar]: TestSIJavaWithCustomTypes.mc4
[CustomSIUnitTypes4ComputingGrammar]: ../../CustomSIUnitTypes4Computing.mc4
[SIUnitTypes4ComputingGrammar]: ../../../../../../main/grammars/de/monticore/SIUnitTypes4Computing.mc4
[BasicSymbolsGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/symbols/BasicSymbols.mc4

[TestSIJavaScope]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaScope.java
[TestSIJavaSymbolTableCreator]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaSymbolTableCreator.java
[TestSIJavaFlatExpressionScopeSetter]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_symboltable/TestSIJavaFlatExpressionScopeSetter.java
[FlatExpressionScopeSetterAbs]: ../../../../../java/de/monticore/types/check/FlatExpressionScopeSetterAbs.java
[SynthesizeSymTypeFromTestSIJava]: ../../../../../java/de/monticore/types/check/SynthesizeSymTypeFromTestSIJava.java
[TypeCheckResult]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheckResult.java
[DeriveSymTypeOfTestSIJava]: ../../../../../java/de/monticore/types/check/DeriveSymTypeOfTestSIJava.java
[SymTypeExpression]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/SymTypeExpression.java

[MyClass]: ../../../../../resources/test/de/monticore/lang/testsijava/testsijava/MyClass.sijava
[ValidModels]: ../../../../../resources/test/de/monticore/lang/testsijava/testsijava

[TypeCheckCoCo]: ../../../../../java/de/monticore/lang/testsijava/testsijava/_cocos/TypeCheckCoCo.java
[TypeCheck]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheck.java

[SIUnitsDoc]: ../../../../../../main/grammars/de/monticore/SIUnits.md
[Types]: https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/java/de/monticore/types/check/Types.md