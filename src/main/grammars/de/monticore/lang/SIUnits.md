<!-- (c) https://github.com/MontiCore/monticore -->

# SIUnits
This language introduces SI units and allows language developers to integrate literals enriched with an SI unit to their language. Furthermore,
this language provides functionality for unit compatibility and type checking.
The grammar files are (see details below):
* [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/SIUnits.mc4)
* [SIUnitLiterals.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/literals/SIUnitLiterals.mc4)
* [SIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/SIUnitTypes.mc4)

## [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/SIUnits.mc4)
### Description
This is the base grammar of the SIUnits language. It introduces SI units and other units that can be derived from them.
This grammar defines SI units as well as unit prefixes and SI unit expressions allowing us to parse simple and derived units 
such as `m`, `km`, `km^2` or `m*deg/(h^2*mg)`.

For calculating with the units the [Unit](http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/Unit.html)
class from the [javax.measure](http://unitsofmeasurement.github.io/unit-api/site/apidocs/javax/measure/package-summary.html) 
package is used.
### Functionality
The main classes to handle a ```javax.measure.Unit``` are the [UnitFactory](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/siunits/utility/UnitFactory.java)
to create Units and the [UnitPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/siunits/utility/UnitPrettyPrinter.java).
Those can be found in the package ```de.monticore.lang.siunits.utility```.
### Generators
There are two PrettyPrinters, the [SIUnitsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/siunits/prettyprint/SIUnitsPrettyPrinter.java)
to prettyprint the SIUnits normally ( ```kg/m*s``` &rarr; ```kg/m*s``` ) and the [SIUnitsWithBracketsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/siunits/prettyprint/SIUnitsWithBracketsPrettyPrinter.java)
to prettyprint the SIUnits with brackets around each expression ( ```kg/m*s``` &rarr; ```((kg/m)*s)``` ). Those can be 
found in the package ```de.monticore.lang.siunits.prettyprint```.

## [SIUnitLiterals.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/literals/SIUnitLiterals.mc4)
### Description
The SIUnitLiterals combine a NumericalLiteral or a SignedNumericalLiteral from the [MCCommonLiterals.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4) 
grammar with a SIUnit from the [SIUnits](#siunitsmc4) grammar. A SIUnitLiteral can be used in an expression as a 
Literal Expression (see grammar [ExpressionBasis.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4)).

SIUnitLiterals allows us to parse literals of the following forms. 
Literals in combination with a unit. The unit may be of any form allowed by the SIUnits.mc4 grammar, i.e. including unit expressions:
* Integer with unit: `5km` or `5 km`; The space is obligatory for Farads and liters to avoid confusion with floats and longs (`5 F` and `5 l`)
* Long with unit: `5l km`
* Gloat with unit: `5.0km` or `5.0 km`; The space is obligatory for Farads and liters to avoid confusion with floats and longs  (`5.0 F` and `5.0 l`)
* Float (explicit) with unit: `5.0f kg` or `5.0F kg`

Standard unitless literals are parsed as Literals as provided by the MCCommonLiteraks grammar:
* Unitless integer: `5`
* Unitless long: `5l`
* Unitless float: `5.0`
* Unitless float (explicit): `5.0f` or `5.0F`

### Functionality
The [SIUnitLiteralDecoder](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/literals/siunitliterals/utility/SIUnitLiteralDecoder.java)
extracts the number of an SIUnitLiteral as either a java ```double``` or a java ```Number``` and calculates the value of
a SIUnitLiteral (```valueOf(3 km)) = 3000```). For this it uses the [NumberDecoder](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/literals/siunitliterals/utility/NumberDecoder.java)
which extracts the number of a NumericLiteral or SignedNumericLiteral as either a java ```double``` or a java ```Number```.

### Generators
The [SIUnitLiteralsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/literals/siunitliterals/prettyprint/SIUnitLiteralsPrettyPrinter.java)
prettyprints a SIUnitLiteral.

## [SIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/SIUnitTypes.mc4)
### Description
The SIUnitTypes interprete the SIUnits as type. Therefore, the grammar extends de.monticore.lang.SIUnits and de.monticore.types.MCBasicTypes

### Generators
The [SIUnitTypesPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/prettyprint/SIUnitTypesPrettyPrinter.java)
prettyprints a SIUnitType and can be found in the package ```de.monticore.lang.types.prettyprint```.

## [PrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/PrimitiveWithSIUnitTypes.mc4)
### Description
A PrimitiveWithSIUnitType combines a MCPrimitiveType from the [MCBasicTypes](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4) 
grammar with a SIUnitType. It is mostly used to describe the type of a SIUnitLiteral but is also useful to derive the type
of an expression with both SIUnits and an NumericLiteral. The primitive part should always be a numeric type.

The grammar provides an exemplary syntax for primitive types combined with a unit, e.g.
`int in km/s^2`, where `int` is a primitive type from [MCBasicTypes](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4),
`in` is the concrete syntax introduced by the grammar and the unit expression is an SI unit type from [SIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/SIUnitTypes.mc4)

A DSL developer should implement his own rule in order to use an alternative syntax.
### Functionality
The [PrimitiveIsNumericType](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/primitivewithsiunittypes/_cocos/PrimitiveIsNumericType.java)
CoCo checks whether the MCPrimitiveType is a numeric type.
### Generators
The [PrimitiveWithSIUnitTypesPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/prettyprint/PrimitiveWithSIUnitTypesPrettyPrinter.java)
prettyprints a PrimitiveWithSIUnitType and can be found in the package ```de.monticore.lang.types.prettyprint```.

## TypeCheck
The classes for the TypeCheck mechanic can be found in the package ```de.monticore.types.check```. SymTypes can be
synthesized from a SIUnitType and a PrimitiveWithSIUnitType and can be derived from SIUnitLiterals or Expressions.
The classes for deriving from AssignmentExpressions and CommonExpressions got extended, so they work with SIUnits and
SIUnitLiterals as well. E.g. ```3m + 2s``` should not be possible, while the type of ```3m / 2.2s``` is ```(double,m/s)```.

(For more info see [TypeCheck](https://git.rwth-aachen.de/monticore/monticore/-/tree/master/monticore-grammar/src/main/java/de/monticore/types/check))

To get the TypeCheck mechanic work with your DSL, the language needs to extend the [de.monticore.types.TypeSymbols.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/master/monticore-grammar/src/main/grammars/de/monticore/types/TypeSymbols.mc4)
grammar. Create a derive and a synthesize class for your language which usually combine the existing derive/synthsize
classes with a DelegetorVisitor. In addition, each TypeSymbol, TypeVarSymbol, FieldSymbol, and MethodSymbol needs to be 
set a SymType while building the symbol table. Furthermore, each Expression, Literal, SignedLiteral, and MCType and
MCReturnType need to have an enclosing scope. Exemplary, this is all done for the [TestSIJava](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.mc4)
language. For further explanation, see the documentation for the [TestSIJava](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/testsijava/TestSIJava.md) language.

## Extensibility
* New units can be added directly in the grammar [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/SIUnits.mc4)
* An alternative syntax fot the definition of primitive types combined with SI units can be defined by implementing `MCObjectType` as it is done in
[CustomPrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/types/CustomPrimitiveWithSIUnitTypes.mc4)
    * To include the new custom types to the TypeCheck mechanic, a DSL developer needs to implement a Synthesize-Class 
    which extends the [SynthesizeSymTypeFromPrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/types/check/SynthesizeSymTypeFromPrimitiveWithSIUnitTypes.java)
    class and implements its visitor. For reference have a look on [SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes.java](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/types/check/SynthesizeSymTypeFromCustomPrimitiveWithSIUnitTypes.java).
    