<!-- (c) https://github.com/MontiCore/monticore -->

<!-- This is a MontiCore stable explanation. -->

<!-- 
  TODO:  Links so anpassen, dass die Übertragung nach Github klappen wird
  (relative Links only)
  Done:
  TODO:  Die MontiCore Links sind alle noch absolut
  
  TODO:
  Wir müssen über die Umsetzung (mit oder ohne explizites Mitschleppen 
  der Einheiten zur Laufzeit)
  sowie über die Bedeutung der Funktionen nochmal nachdenken.
 -->


# SIUnits

This language introduces SI units and allows language developers to 
integrate literals enriched with an SI unit to their language. Furthermore,
this language provides functionality for unit compatibility and type 
checking.

[[_TOC_]]

## The grammar files are (see details below):

* [SIUnits.mc4][SIUnitGrammar]
* [SIUnitLiterals.mc4][SIUnitLiteralsGrammar]
* [SIUnitTypes4Math.mc4][SIUnitTypes4MathGrammar]
* [SIUnitTypes4Computing.mc4][SIUnitTypes4ComputingGrammar]


## [SIUnits.mc4][SIUnitGrammar]

### Description

This is the base grammar of the SIUnits language. It introduces SI 
units and other units that can be derived from them.
This grammar defines SI units with their prefixes and complex, 
composed SI units allowing us to parse simple and derived units 
such as `m`, `km`, `km^2` or `km^2/VA^2h`.

### Functionality

* [UnitPrettyPrinter][UnitPrettyPrinter]
    Prints a Unit in a former and consistent way from an 
    `ASTSIUnit`,a String or a `javax.measure.unit.Unit`
        as either BaseUnit or asIs-Unit.

### Generators

The [SIUnitsPrettyPrinter][SIUnitsPrettyPrinter]
prettyprints the SIUnits normally from its AST.


## [SIUnitLiterals.mc4][SIUnitLiteralsGrammar]

### Description

The SIUnitLiterals allow to describe concrete values, such as
`5km` or `23.4m/s` that can be used within ordinary expressions. 
For that purpose they combine a NumericalLiteral resp.
SignedNumericalLiteral from the 
[MCCommonLiterals.mc4][MCCommonLiteralsGrammar] 
grammar with a SIUnit from the [SIUnits](#siunitsmc4) grammar. 

SIUnitLiterals allows us to parse literals of the following forms. 
Literals in combination with a unit. The unit may be of any form allowed 
by the SIUnits.mc4 grammar, i.e. including unit expressions:
* Integer with unit: `5km` or `5 km/h`
* Long with unit: `5l km`
* Float with unit: `5.0km` or `5.0 km`; The space is obligatory for Farads to 
  avoid confusion with floats (`5.0 F`)
* Float (explicit) with unit: `5.0f kg` or `5.0F kg`
  * Caution: A space is obligatory for liters to 
  avoid confusion with longs (`5 l`)
Standard unitless literals are parsed as Literals as provided by the 
MCCommonLiterals grammar:
* Unitless integer: `5`
* Unitless long: `5l` or `5L`
* Unitless float: `5.0`
* Unitless float (explicit): `5.0f` or `5.0F`

### Functionality

The [SIUnitLiteralDecoder][SIUnitLiteralDecoder]
extracts the number of an SIUnitLiteral as either a java `double` or a java 
`java.lang.Number` and calculates the value of
a SIUnitLiteral (`3 km` has value `3000` and `3 km` in
`mm` has value `3000000`). 

### Generators

The [SIUnitLiteralsPrettyPrinter][SIUnitLiteralsPrettyPrinter]
prettyprints a SIUnitLiteral.


## [SIUnitTypes4Math.mc4][SIUnitTypes4MathGrammar]

### Description

The SIUnitTypes interprete the SIUnits as type in the MontiCore type universe. 
Therefore, the grammar extends 
[SIUnits.mc4][SIUnitGrammar] 
and [de.monticore.types.MCBasicTypes.mc4][MCBasicTypesGrammar]. 
A SIUnitType implements the MCType and can therefore be used wherever a type is used,
e.g. when a variable is declared or a method parameter is typed.

The idea of this grammar is that the types are used in a mathematically ideal world (e.g. 
also in early stages of systems development), where no limitations on
the type of numbers plays a role yet. This is opposed to the below defined
SIUnitTypes4Computing grammar, where concrete types of numbers can be added as well.

### Generators

The 
[SIUnitTypes4MathPrettyPrinter][SIUnitTypes4MathPrettyPrinter]
prettyprints a SIUnitType and can be found in the package 
`de.monticore.siunittypes4math.prettyprint`.


## [SIUnitTypes4Computing.mc4][SIUnitTypes4ComputingGrammar]

### Description

SIUnitType4Computing interprets teh SIUnits as generic type that 
have exactly one parameter, which is of numeric type.
This allows to specify SI unit and underlying numeric type, such as `km/h<float>`
or `m<long>` in combination and use that as type.
This is more oriented toward computation, because it allows to model 
numeric restrictions. Please note that e.g. `km<int>` has a different behavior that 
`m<int>` in terms of rounding and overflow.

Technically, SIUnitType4Computing defines a type expression with an 
SI unit as generic type and a MCPrimitiveType as argument. 
The primitive part must be a numeric type (defined as coco).

Remark: while the syntax of SI Units is very carefully standardized,
the use of SI Units as type definitions, and especially as generic types
is an invention by the MontiCore team. Alternative syntaxes are definitely 
possible.

### Functionality

The [PrimitiveIsNumericType][PrimitiveIsNumericType]
CoCo checks whether the MCPrimitiveType part is a numeric type.

### Generators

The [SIUnitTypes4ComputingPrettyPrinter][SIUnitTypes4ComputingPrettyPrinter]
prettyprints a SIUnitType`.



## TypeCheck

* The classes for the TypeCheck mechanic can be found in the package 
  `de.monticore.types.check`. Those extend the existing mechanics to 
  work with `SIUnits`. 
* With the [TypeCheck][TypeCheck] class [SymTypeExpressions][SymTypeExpression] 
 (MontiCore's internal form of storage for types) can be
 synthesized from a `MCType` and returns a [SymTypeOfSIUnit][SymTypeOfSIUnit] 
 or a [SymTypeOfNumericWithSIUnit][SymTypeOfNumericWithSIUnit] for a 
 `SIUnitType` or `SIUnitType4Computing`.
* With the [TypeCheck][TypeCheck] class [SymTypeExpressions][SymTypeExpression] 
 can be derived from a `Literal` or an `Expression` and returns a [SymTypeOfSIUnit][SymTypeOfSIUnit] 
 or a [SymTypeOfNumericWithSIUnit][SymTypeOfNumericWithSIUnit] for expressions
 containing `SIUnits`. Note that only AssignmentExpressions and CommonExpressions
 can result in a SymTypeExpression for `SIUnits`. The resulting types of expression
 is defined as follows:
  * The type of a SIUnitLiteral is a `SymTypeOfNumericWithSIUnit`
    as a combination of the types of its NumericLiteral and its SIUnit. \
    E.g. `typeOf(3.2f m) -> (float,m)`
  * The multiplication of two `SymTypeOfNumericWithSIUnit` results in the 
    combination of the multiplication of their numeric parts and 
    of their SI unit parts. \
    E.g. `typeOf(3 m * 1.5 s) -> (double,m*s)`
  * The division of two `SymTypeOfNumericWithSIUnit` results in the 
    combination of the division of their numeric parts 
    of their SI unit parts. \
    E.g. `typeOf(3 m / 2l s) -> (long,m/s)`
  * The addition of two `SymTypeOfNumericWithSIUnit` results in the 
    combination of the addition of their numeric parts and 
    their SI unit parts. The addition of the SI unit parts is only
    defined if the units are compatible (same base units with different prefix)
    and results in the _smaller_ unit. \
    E.g. `typeOf(3 m + 2 s) -> undefined` (results in an error) \
    `typeOf(1.25 h + 30 min) -> (double,min)` \
    `typeOf(30 min + 1.25 h) -> (double,min)`
  * Subtraction and modulo are defined in the same way as the addition.

The TypeCheck classes here are:

* [SymTypeOfSIUnitBasic][SymTypeOfSIUnitBasic]
    which defines the SymType of a SIUnit basis, e.g. `m` or `kg` 
    without any other prefixes.
* [DefsSIUnitType][DefsSIUnitType]
    which contains the list with all SIUnit basic types.
* [SymTypeOfSIUnit][SymTypeOfSIUnit]
    which contains a numerator and denominator list of SymTypeOfSIUnitBasic. 
    It also provides the declared Unit
    as `javax.measure.unit.Unit`.
* [SymTypeOfNumericWithSIUnit][SymTypeOfNumericWithSIUnit]
    which contains a numeric type (SymTypeConstant) and a SymTypeOfSIUnit. 
    It also provides the declared Unit
    as `javax.measure.unit.Unit`. Note that every [SymTypeOfNumericWithSIUnit][SymTypeOfNumericWithSIUnit]
    has the same super type which can be utilized for declaring methods which should take any
    [SymTypeOfNumericWithSIUnit][SymTypeOfNumericWithSIUnit] as parameter.
* [SIUnitSymTypeExpressionFactory][SIUnitSymTypeExpressionFactory]
    which is the only class to create SymTypeOfSIUnitBasic, SymTypeOfSIUnit 
    and SymTypeOfNumericWithSIUnit.
* [SynthesizeSymTypeFromSIUnitTypes4Math][SynthesizeSymTypeFromSIUnitTypes4Math]
    which synthesizes a `SymTypeOfSIUnit` from a `SIUnitType4Math`.
* [SynthesizeSymTypeFromSIUnitTypes4Computing][SynthesizeSymTypeFromSIUnitTypes4Computing]
    which synthesizes a `SymTypeOfNumericWithSIUnit` from a 
    `SIUnitType4Computing`. If one defined your own
    custom `SIUnitType4Computing`, the class needs to be extended and the 
    `traverseSIUnitType4Computing()` can be called. This is exemplary done in 
    [SynthesizeSymTypeFromCustomSIUnitTypes4Computing][SynthesizeSymTypeFromCustomSIUnitTypes4Computing].
* [DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes][DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes]
    which extends the [DeriveSymTypeOfAssignmentExpressions][DeriveSymTypeOfAssignmentExpressions]
    class in order to work with SIUnitTypes as well. 
* [DeriveSymTypeOfCommonExpressionsWithSIUnitTypes][DeriveSymTypeOfCommonExpressionsWithSIUnitTypes]
    which extends the 
    [DeriveSymTypeOfCommonExpressions][DeriveSymTypeOfCommonExpressions]
    class in order to work with SIUnitTypes as well.
    
For more info see [TypeCheck][Types]

<!--
To get the TypeCheck mechanic work with your DSL, the language needs to
1. extend the 
   [de.monticore.symbols.OOSymbols.mc4][OOSymbols]
   grammar. 
2. Create a derive and a synthesize class for your language which 
  usually combine the existing derive- / synthsize-
  classes using a DelegetorVisitor. 
3. While building the symbol table each TypeSymbol, 
  TypeVarSymbol, FieldSymbol, and MethodSymbol needs to be 
  assigned a [SymTypeExpressions][SymTypeExpression]. 
4. While building the symbol table each Expression, 
  Literal, SignedLiteral, MCType and MCReturnType 
  need to have an enclosing scope.
5. When using methods or functions, those need to have a
  spannedScope which only contains the parameters. In this
  scope, the scope of the method's body can be nested.

Exemplary, this is all done for the 
  [TestSIJava][TestSIJavaGrammar]
  language. For further explanation, see the documentation for the 
  [TestSIJava][TestSIJavaDoc] language.

((TODO: I would like to understand, whether the above two dues are completely new
or have been the same to'dos already without the given SIUnit Extension.
or is this piece of to do not necessary anymore, becaus we now DO have a concrete syntax and therefore typecheck
already?))
Done: Those are always todos when using TypeCheck, does not really belong here
-->

## Extensibility

* Additional units can be added directly extending the grammar 
  [SIUnits.mc4][SIUnitsGrammar]
  * There might be changes necessary in the classes
    * [UnitFactory][UnitFactory]
    * [UnitPrettyPrinter][UnitPrettyPrinter]
    * [SIUnitConstants][SIUnitConstants]
    * [DefsSIUnitType][DefsSIUnitType]
* An alternative syntax fot the definition of primitive types combined with 
  SI units can be defined by implementing `SIUnitType4ComputingInt` interface
  as it is done in
  [CustomSIUnitTypes4Computing][CustomSIUnitTypes4ComputingGrammar]
  * To include the new custom types to the TypeCheck mechanic, a DSL developer 
    needs to implement a Synthesize-Class which extends the 
    [SynthesizeSymTypeFromSIUnitTypes4Computing][SynthesizeSymTypeFromSIUnitTypes4Computing]
    class and implements its visitor. For reference have a look on 
    [SynthesizeSymTypeFromCustomSIUnitTypes4Computing.java][SynthesizeSymTypeFromCustomSIUnitTypes4Computing]

## Usage

Usage of the SI Units package is shown using the 
[SI Java test language][TestSIJavaGrammar] 
and  is explained in 
[this tutorial][TestSIJavaDoc].

# Further Functions

Two functions should be available in a DSL to work on unit literals and variables with units:
`baseunit(..)` takes a variable or constant and returns the value of the expression
after converting it to base units, e.g. `baseunit(5km)` returns `5000`.
`unit(..)` returns the value of the expression, e.g. `unit(5km)` returns `5`.
In case of a sum expression where several scales are used, a conversion to the smallest scale
is undertaken, e.g. `unit(5cm+10dm+2mm)` returns `1052` as milli is the smallest scale.
The concrete name and syntax can be adapted when building new DSLs.


# Library Functions

The following table contains a set of use cases and examples 
for the work with SI units on the AST: \
`ASTSIUnitLiteral astLiteral` is `5 km` \
`javax.measure.unit.Unit sourceUnit` is `km` \
`javax.measure.unit.Unit targetUnit` is `cm` \
`d = new SIUnitLiteralDecoder(...);`

| Description | Java-Syntax | Return value |
| ------ | ------ | ------ |
| Unit of SIUnitLiteral |  `javax.measure.unit.Unit u = d.getUnit(astLiteral)` | `javax.measure.unit.Unit` km |
| Create Unit | `UnitFactory.createUnit("km/s" / astLiteral.getSIUnit())` | `javax.measure.unit.Unit` |
| Base Units of SIUnitLiteral |  `UnitFactory.createBaseUnit("mm" / astLiteral.getSIUnit() / u)` | `javax.measure.unit.Unit` m |
| Literal of SIUnitLiteral | `d.getNumber(astLiteral)` or `d.getDouble(astLiteral)` | `java.lang.Integer` 5 or `double` 5.0 |
| Literal of SIUnitLiteral as Base Units  | `d.valueOf(astLiteral/*km*/)` | `double` 5000.0 |
| Literal of SIUnitLiteral as TargetUnit  | `d.valueOf(astLiteral/*km*/, targetUnit/*cm*/); ...` | `double` 500,000.0 |
| Convert Units | `UnitConverter c = Converter.getConverter(sourceUnit/*km*/, targetUnit/*cm*/); c.convert(5.2);` or `Converter.convert(5.2, sourceUnit/*km*/, targetUnit/*cm*/)` | `double` 5,200,000.0 | 
| Convert to Base Units | `Converter.convertFrom(5.2, u/*km*/);`  | `double` 5,200.0 |
| Convert from Base Units | `Converter.convertTo(5200, u/*km*/);`  | `double` 5.2 |
| Print javax.measure.unit.Unit | `UnitPrettyPrinter.printUnit(javax.measure.unit.Unit / String / ASTSIUnit)` `UnitPrettyPrinter.printBaseUnit(javax.measure.unit.Unit / String / ASTSIUnit)` | `String` |


## Runtime: Executing Calculations

While the previous part described, how SI Units are used in grammars 
and thus mainly for the concrete and abstract syntax of a modeling language,
we here also discuss possibilities to execute calculations.

There are mainly two approaches, while the latter also cames in variants:

1. Due to the strong typing system that is provided, the generator in principal
   can throw all type information away after consistency has been checked.
   This is efficient and type safe, as the type checker is correctly implemented.
   All neded scalar transformations can be explicitly added during the generation 
   process, SI Unit information is not present in the values anymore.
   (efficient compilers do exactly that with their datatypes today).
   I.e.  `km/h<int> x = 100 * 30m / 100sek` would be translated to
        `int x =  (100 * 30 / 60)  * 3600 / 1000` where the later multiplication 
        and division handles the scaling. (Further optimization is not needed here, 
        but handled by a subsequent compiler.)

2. In some target languages, such as C, C++, or python 
   strong typing doesn't really exist and people 
   have implemented frameworks that make units explicit. I.e. instead of storing 
   a value for `km/h<int> x` in form of a simple `int` value, an object
   with attributes like `int value`, `String siUnit`, and `int scaleFactor` are 
   carried around at runtime. 
   [Unit][JavaUnit]
   is such a framework for Java.

   * As a side effect the SI Units are themselves encoded as values
     (whereas in our language, we treat them as types, what they actually are)
     and thus the typing system is encoded explicitely - acommon approach,
     when the programming language doesn't provide a strong typing system.
     
It is up to the developer of a generator to decide which solution to be taken.

### Runtime: Execution without Carrying SI Units around

* This point is deliberately left open at them moment.

### Runtime: Execution with Carrying SI Units around

For Variant 2, we provide the following implementation:

* For calculating with the units the
  [Unit][JavaUnit]
  class from the 
  [javax.measure][JavaMeasure] 
  package is used.

* The main classes that used to handle a 
  `javax.measure.unit.Unit` are the `UnitFactory`, the 
  `UnitPrettyPrinter` and the `Converter` and can be found in the package 
  `de.monticore.siunits.utility`.

* [UnitFactory][UnitFactory]
  * Creates Units from an `ASTSIUnit` or a String
  * Creates the BaseUnit from an `ASTSIUnit`,a String or a 
    `javax.measure.unit.Unit`
* [Converter][Converter]
  * Creates a `javax.measure.converter.UnitConverter` from a
    source unit to a target unit, to a target unit from its base unit,
    or from a source unit to its base unit. This converter then can be
    used to convert a `double`
  * Directly converts a value from a
    source unit to a target unit, to a target unit from its base unit,
    or from a source unit to its base unit.


* The 
  [SIUnitLiteralDecoder][SIUnitLiteralDecoder]
  extracts the number of an SIUnitLiteral:
  For this it uses the 
  [NumberDecoder][NumberDecoder]
  which extracts the number of a NumericLiteral or SignedNumericLiteral as 
  either a java `double` or a java `java.lang.Number`.

<!--   
    TODO:  Die MontiCore Links sind alle noch absolut:
    MCCommonLiteralsGrammar
    MCBasicTypesGrammar
    OOSymbols
    DeriveSymTypeOfAssignmentExpressions
    DeriveSymTypeOfCommonExpressions
    Types
-->

[SIUnitGrammar]: SIUnits.mc4
[SIUnitLiteralsGrammar]: SIUnitLiterals.mc4
[SIUnitTypes4MathGrammar]: SIUnitTypes4Math.mc4
[SIUnitTypes4ComputingGrammar]: SIUnitTypes4Computing.mc4
[TestSIJavaGrammar]: ../../../../test/grammars/de/monticore/lang/testsijava/TestSIJava.mc4
[CustomSIUnitTypes4ComputingGrammar]: ../../../../test/grammars/de/monticore/CustomSIUnitTypes4Computing.mc4
[MCCommonLiteralsGrammar]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4
[MCBasicTypesGrammar]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4
[OOSymbols]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/OOSymbols.mc4

[SIUnitsPrettyPrinter]: ../../../java/de/monticore/siunits/prettyprint/SIUnitsPrettyPrinter.java
[SIUnitLiteralsPrettyPrinter]: ../../../java/de/monticore/siunitliterals/prettyprint/SIUnitLiteralsPrettyPrinter.java
[SIUnitTypes4MathPrettyPrinter]: ../../../java/de/monticore/siunittypes4math/prettyprint/SIUnitTypes4MathPrettyPrinter.java
[SIUnitTypes4ComputingPrettyPrinter]: ../../../java/de/monticore/siunittypes4computing/prettyprint/SIUnitTypes4ComputingPrettyPrinter.java

[UnitFactory]: ../../../java/de/monticore/siunits/utility/UnitFactory.java
[UnitPrettyPrinter]: ../../../java/de/monticore/siunits/utility/UnitPrettyPrinter.java 
[SIUnitConstants]: ../../../java/de/monticore/siunits/utility/SIUnitConstants.java
[Converter]: ../../../java/de/monticore/siunits/utility/Converter.java
[SIUnitLiteralDecoder]: ../../../java/de/monticore/siunitliterals/utility/SIUnitLiteralDecoder.java
[NumberDecoder]: ../../../java/de/monticore/siunitliterals/utility/NumberDecoder.java
[PrimitiveIsNumericType]: ../../../java/de/monticore/siunittypes4computing/_cocos/PrimitiveIsNumericType.java

[SymTypeOfSIUnitBasic]: ../../../java/de/monticore/types/check/SymTypeOfSIUnitBasic.java
[DefsSIUnitType]: ../../../java/de/monticore/types/check/DefsSIUnitType.java
[SymTypeOfSIUnit]: ../../../java/de/monticore/types/check/SymTypeOfSIUnit.java
[SymTypeOfNumericWithSIUnit]: ../../../java/de/monticore/types/check/SymTypeOfNumericWithSIUnit.java
[SIUnitSymTypeExpressionFactory]: ../../../java/de/monticore/types/check/SIUnitSymTypeExpressionFactory.java
[SynthesizeSymTypeFromSIUnitTypes4Math]: ../../../java/de/monticore/types/check/SynthesizeSymTypeFromSIUnitTypes4Math.java
[SynthesizeSymTypeFromSIUnitTypes4Computing]: ../../../java/de/monticore/types/check/SynthesizeSymTypeFromSIUnitTypes4Computing.java
[SynthesizeSymTypeFromCustomSIUnitTypes4Computing]: ../../../../test/java/de/monticore/types/check/SynthesizeSymTypeFromCustomSIUnitTypes4Computing.java
[DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes]: ../../../java/de/monticore/types/check/DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes.java
[DeriveSymTypeOfAssignmentExpressions]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/DeriveSymTypeOfAssignmentExpressions.java
[DeriveSymTypeOfCommonExpressionsWithSIUnitTypes]: ../../../java/de/monticore/types/check/DeriveSymTypeOfCommonExpressionsWithSIUnitTypes.java
[DeriveSymTypeOfCommonExpressions]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/DeriveSymTypeOfCommonExpressions.java
[SymTypeExpression]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/SymTypeExpression.java
[TypeCheck]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheck.java

[TestSIJavaDoc]: ../../../../test/grammars/de/monticore/lang/testsijava/TestSIJava.md
[Types]: https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/TypeCheck.md

[JavaUnit]: http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/Unit.html
[JavaMeasure]: http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/package-summary.html

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)
* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)
* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

