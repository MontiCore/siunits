<!-- (c) https://github.com/MontiCore/monticore -->

<!-- 
  TODO:  Links so anpassen, dass die Übertragung nach Github klappen wird
  (relative Links only)
  
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
The grammar files are (see details below):
* [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnits.mc4)
* [SIUnitLiterals.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitLiterals.mc4)
* [SIUnitTypes4Math.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitTypes4Math.mc4)
* [SIUnitTypes4Computing.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitTypes4Computing.mc4)

## [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnits.mc4)

### Description

This is the base grammar of the SIUnits language. It introduces SI 
units and other units that can be derived from them.
This grammar defines SI units as well as unit prefixes and SI unit 
expressions allowing us to parse simple and derived units 
such as `m`, `km`, `km^2` or `m*deg/(h^2*mg)`.

For calculating with the units the
[Unit](http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/Unit.html)
class from the 
[javax.measure](http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/package-summary.html) 
package is used.

### Functionality

The main classes that should always be used to handle a 
```javax.measure.Unit``` are the ```UnitFactory``` and the 
```UnitPrettyPrinter``` and can be found in the package 
```de.monticore.siunits.utility```.
* [UnitFactory](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/utility/UnitFactory.java)
    * Creates Units from an ```ASTSIUnit``` or a String
    * Creates the BaseUnit from an ```ASTSIUnit```,a String or a `
      ``javax.measure.Unit```
    * Creates the StandardUnit from an ```ASTSIUnit```,a String or a `
      ``javax.measure.Unit```, standard Units here
    are: A, lm, C, Sv, F, H, J, K, mol, bit, lx, N, Pa, Gy, S, rad, T, 
         V, W, kg, sr, kat, cd, Ohm, Wb, m, Bq, Hz, s
* [UnitPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/utility/UnitPrettyPrinter.java)
    Prints a Unit in a former and consistent way from an 
    ```ASTSIUnit```,a String or a ```javax.measure.Unit```
        as either BaseUnit, StandardUnit or asIs-Unit.

### Generators

There are two PrettyPrinters, the 
[SIUnitsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/prettyprint/SIUnitsPrettyPrinter.java)
to prettyprint the SIUnits normally ( ```kg/m*s``` &rarr; ```kg/m*s``` ) 
and the [SIUnitsWithBracketsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/prettyprint/SIUnitsWithBracketsPrettyPrinter.java)
to prettyprint the SIUnits with brackets around each expression 
( ```kg/m*s``` &rarr; ```((kg/m)*s)``` ). Those can be 
found in the package ```de.monticore.siunits.prettyprint```.

## [SIUnitLiterals.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitLiterals.mc4)

### Description

The SIUnitLiterals combine a NumericalLiteral or a SignedNumericalLiteral from the 
[MCCommonLiterals.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4) 
grammar with a SIUnit from the [SIUnits](#siunitsmc4) grammar. A SIUnitLiteral 
can be used in an expression as a 
Literal Expression (see grammar 
[ExpressionBasis.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4)).

SIUnitLiterals allows us to parse literals of the following forms. 
Literals in combination with a unit. The unit may be of any form allowed 
by the SIUnits.mc4 grammar, i.e. including unit expressions:
* Integer with unit: `5km` or `5 km`; The space is obligatory for liters to 
  avoid confusion with longs (`5 l`)
* Long with unit: `5l km`
* Float with unit: `5.0km` or `5.0 km`; The space is obligatory for Farads to 
  avoid confusion with floats (`5.0 F`)
* Float (explicit) with unit: `5.0f kg` or `5.0F kg`

Standard unitless literals are parsed as Literals as provided by the 
MCCommonLiterals grammar:
* Unitless integer: `5`
* Unitless long: `5l` or `5L`
* Unitless float: `5.0`
* Unitless float (explicit): `5.0f` or `5.0F`

### Functionality

The [SIUnitLiteralDecoder](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunitliterals/utility/SIUnitLiteralDecoder.java)
extracts the number of an SIUnitLiteral as either a java ```double``` or a java 
```java.lang.Number``` and calculates the value of
a SIUnitLiteral (```valueOf(3 km)) = 3000``` or ```valueOf(3 km, mm)) = 3000000```). 
For this it uses the 
[NumberDecoder](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunitliterals/utility/NumberDecoder.java)
which extracts the number of a NumericLiteral or SignedNumericLiteral as 
either a java ```double``` or a java ```java.lang.Number```.

### Generators

The [SIUnitLiteralsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunitliterals/prettyprint/SIUnitLiteralsPrettyPrinter.java)
prettyprints a SIUnitLiteral.

## [SIUnitTypes4Math.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitTypes4Math.mc4)

### Description

The SIUnitTypes interprete the SIUnits as type. Therefore, the grammar extends 
[SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnits.mc4) 
and [de.monticore.types.MCBasicTypes.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4). 
A SIUnitType implements the MCObjectType and can therefore be used as a MCType 
or MCReturnType.

### Generators

The [SIUnitTypes4MathPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunittypes4math/prettyprint/SIUnitTypes4MathPrettyPrinter.java)
prettyprints a SIUnitType and can be found in the package 
```de.monticore.siunittypes4math.prettyprint```.

## [SIUnitTypes4Computing.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitTypes4Computing.mc4)

### Description

A SIUnitType4Computing combines a MCPrimitiveType from the 
[MCBasicTypes](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4) 
grammar with a SIUnitType. It is mostly used to describe the type of a 
SIUnitLiteral but is also useful to derive the type
of an expression with both SIUnits and an NumericLiteral. The primitive part 
should always be a numeric type (not boolean).

The grammar provides an exemplary syntax for primitive types combined with a 
unit, e.g.
`int in km/s^2`, where `int` is a primitive type from 
[MCBasicTypes](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4),
`in` is the concrete syntax introduced by the grammar and the unit 
expression is an SI unit type from 
[SIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnitTypes4Computing.mc4)

A DSL developer should implement his own rule in order to use an alternative syntax.

### Functionality

The [PrimitiveIsNumericType](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunittypes4computing/_cocos/PrimitiveIsNumericType.java)
CoCo checks whether the MCPrimitiveType part is a numeric type.

### Generators

The [SIUnitTypes4ComputingPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunittypes4computing/prettyprint/SIUnitTypes4MathPrettyPrinter.java)
prettyprints a SIUnitType4Computing and can be found in the package 
```de.monticore.siunittypes4computing.prettyprint```.

## TypeCheck

The classes for the TypeCheck mechanic can be found in the package 
```de.monticore.types.check```. SymTypes can be
synthesized from a SIUnitType and a SIUnitType4Computing and can be derived 
from SIUnitLiterals or Expressions.
The classes for deriving from AssignmentExpressions and CommonExpressions were 
extended, in order to work with SIUnits and
SIUnitLiterals as well. E.g. ```3m + 2s``` should not be possible, while the 
type of ```3m / 2.2s``` is ```(double,m/s)```.
Note that the type of a dimensionless Unit other than the pre defines is 
invalid. E.g. ```3 km/m``` can not be typechecked.

The TypeCheck classes here are:

* [SymTypeOfSIUnitBasic](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SymTypeOfSIUnitBasic.java)
    which defines the SymType of a SIUnit basis, e.g. ```m``` or ```kg``` 
    without any other prefixes.
* [DefsSIUnitType](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/DefsSIUnitType.java)
    which contains the list with all SIUnit basic types.
* [SymTypeOfSIUnit](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SymTypeOfSIUnit.java)
    which contains a numerator and denominator list of SymTypeOfSIUnitBasic. 
    It also provides the declared Unit
    as ```javax.measure.Unit```.
* [SymTypeOfNumericWithSIUnit](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SymTypeOfNumericWithSIUnit.java)
    which contains a numeric type (SymTypeConstant) and a SymTypeOfSIUnit. 
    It also provides the declared Unit
    as ```javax.measure.Unit```.
* [SIUnitSymTypeExpressionFactory](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SIUnitSymTypeExpressionFactory.java)
    which is the only class to create SymTypeOfSIUnitBasic, SymTypeOfSIUnit 
    and SymTypeOfNumericWithSIUnit.
* [SynthesizeSymTypeFromSIUnitTypes4Math](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SynthesizeSymTypeFromSIUnitTypes4Math.java)
    which synthesizes a ```SymTypeOfSIUnit``` from a ```SIUnitType4Math```.
* [SynthesizeSymTypeFromSIUnitTypes4Computing](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SynthesizeSymTypeFromSIUnitTypes4Computing.java)
    which synthesizes a ```SymTypeOfNumericWithSIUnit``` from a 
    ```SIUnitType4Computing```. If one defined your own
    custom ```SIUnitType4Computing```, the class needs to be extended and the 
    ```traverseSIUnitType4Computing()``` can be called. This is exemplary done in 
    [SynthesizeSymTypeFromCustomSIUnitTypes4Computing](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/SynthesizeSymTypeFromCustomSIUnitTypes4Computing.java).
* [DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/DeriveSymTypeOfAssignmentExpressionsWithSIUnitTypes.java)
    which extends the [DeriveSymTypeOfAssignmentExpressions](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/DeriveSymTypeOfAssignmentExpressions.java)
    class in order to work with SIUnitTypes as well. 
* [DeriveSymTypeOfCommonExpressionsWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/DeriveSymTypeOfCommonExpressionsWithSIUnitTypes.java)
    which extends the 
    [DeriveSymTypeOfCommonExpressions](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/java/de/monticore/types/check/DeriveSymTypeOfCommonExpressions.java)
    class in order to work with SIUnitTypes as well.
    
(For more info see [TypeCheck](https://git.rwth-aachen.de/monticore/monticore/-/tree/master/monticore-grammar/src/main/java/de/monticore/types/check))

To get the TypeCheck mechanic work with your DSL, the language needs to extend the 
[de.monticore.types.TypeSymbols.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/TypeSymbols.mc4)
grammar. Create a derive and a synthesize class for your language which 
usually combine the existing derive/synthsize
classes with a DelegetorVisitor. In addition, each TypeSymbol, 
TypeVarSymbol, FieldSymbol, and MethodSymbol needs to be 
set a SymType while building the symbol table. Furthermore, each 
Expression, Literal, SignedLiteral, and MCType and
MCReturnType need to have an enclosing scope. Exemplary, this is 
all done for the 
[TestSIJava](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.md)
language. For further explanation, see the documentation for the 
[TestSIJava](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.md) language.

## Extensibility

* Additional units can be added directly extending the grammar 
  [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/SIUnits.mc4)
    * There might be changes necessary in the classes
        * [UnitFactory](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/utility/UnitFactory.java)
        * [UnitPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/siunits/utility/UnitPrettyPrinter.java)
        * [DefsSIUnitType](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/DefsSIUnitType.java)
* An alternative syntax fot the definition of primitive types combined with 
  SI units can be defined by implementing `MCObjectType` as it is done in
  [CustomSIUnitTypes4Computing](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/CustomSIUnitTypes4Computing.mc4)
    * To include the new custom types to the TypeCheck mechanic, a DSL developer needs to implement a Synthesize-Class 
    which extends the 
    [SynthesizeSymTypeFromSIUnitTypes4Computing](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SynthesizeSymTypeFromSIUnitTypes4Computing.java)
    class and implements its visitor. For reference have a look on 
    [SynthesizeSymTypeFromCustomSIUnitTypes4Computing.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/SynthesizeSymTypeFromCustomSIUnitTypes4Computing.java).

## Usage

Usage of the SI Units package is shown using the [SI Java test language](https://git.rwth-aachen.de/monticore/languages/siunits/-/tree/master/src/test/grammars/de/monticore/lang/testsijava) and 
is explained in [this tutorial](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.md)

# Further Functions

| Beschreibung | DSL-Syntax | funktionale Syntax | Java-Syntax | Returnwert |
| ------ | ------ | ------ |------| ------|
| Unit von SILiteral | `[5km]` | `unit(5km)` | `SIUnitLiteralDecoder d = new SIUnitLiteralDecoder(...); javax.measure.Unit u=d.UnitOf(ASTSILiteral)` | km
| BaseUnit von SILiteral | `B[5km]` | `baseunit(5km)` |  `UnitFactory.createBaseUnit(u)` | m
| Literal von SILiteral | `{5km}` | `value(5km)` | `SIUnitLiteralDecoder d = new SIUnitLiteralDecoder(...); d.NumberOf(ASTSILiteral)` oder `d.DoubleOf(ASTSILiteral)` | 5
| Literal von SILiteral umgerechnet auf Basisunits | `B{5km}` | `basevalue(5km)` | `SIUnitLiteralDecoder d = new SIUnitLiteralDecoder(...); d.ValueOf(ASTSILiteral)` | 5000
| SILiteral zu TargetSILiteral | `5km==>cm` | `convert(5km, cm)` | `SIUnitLiteralDecoder d = new SIUnitLiteralDecoder(...); double res=d.ValueOf(ASTSILiteral, javax.measure.Unit); ...` | 500.000 cm 

# Library Functions

| Beschreibung | Java-Syntax | Returnwert |
| ------ | ------ | ------ |
| Convert Units | `c=getConverter(srcUnit/*km*/, targetUnit/*m*/); c.convert(5.2);`  | 5200 | 
| Convert to Base Units | `c=getConverter(srcUnit/*km*/); c.convert(5.2);`  | 5200 |
| Create Unit | `UnitFactory.createUnit("km/s")` | `javax.measure.Unit` |
| Print javax.measure.Unit | `UnitPrettyPrinter.printUnit(javax.measure.Unit / String / SIUnit)` `UnitPrettyPrinter.printBaseUnit(javax.measure.Unit / String / SIUnit)` | String |
