<!-- (c) https://github.com/MontiCore/monticore -->

# SIUnits
This language introduces SI units and functionality.

## [SIUnits.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/SIUnits.mc4)
### Description
This is the base grammar of the SIUnits language. It introduces SI units and other units that can be derived from them.
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
Literal Expression (see grammar [ExpressionBasis.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4)).
### Functionality
The [SIUnitLiteralDecoder](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/literals/siunitliterals/utility/SIUnitLiteralDecoder.java)
extracts the number of an SIUnitLiteral as either a java ```double``` or a java ```Number``` and calculates the value of
a SIUnitLiteral (```valueOf(3 km)) = 3000 m```).
### Generators
The [SIUnitLiteralsPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/literals/siunitliterals/prettyprint/SIUnitLiteralsPrettyPrinter.java)
prettyprints a SIUnitLiteral.

## [SIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/SIUnitTypes.mc4)
### Description
The SIUnitTypes interprete the SIUnits as types.
### Generators
The [SIUnitTypesPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/prettyprint/SIUnitTypesPrettyPrinter.java)
prettyprints a SIUnitType and can be found in the package ```de.monticore.lang.types.prettyprint```.

## [PrimitiveWithSIUnitTypes](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/grammars/de/monticore/lang/types/PrimitiveWithSIUnitTypes.mc4)
### Description
A PrimitiveWithSIUnitType combines a MCPrimitiveType from the [MCBasicTypes](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4) 
grammar with a SIUnitType. It is mostly used to describe the type of a SIUnitLiteral but is also useful to derive the type
of an expression with both SIUnits and an NumericLiteral. The primitive part should always be a numeric type. <br>
Please note that this is only one way to represent this combination of types. An example on how to implement this in 
another way is presented in the [CustomPrimitiveWithSIUnitTypes.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/types/CustomPrimitiveWithSIUnitTypes.mc4)
grammar.
### Functionality
The [PrimitiveIsNumericType](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/primitivewithsiunittypes/_cocos/PrimitiveIsNumericType.java)
CoCo checks whether the MCPrimitiveType is a numeric type.
### Generators
The [PrimitiveWithSIUnitTypesPrettyPrinter](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/main/java/de/monticore/lang/types/prettyprint/PrimitiveWithSIUnitTypesPrettyPrinter.java)
prettyprints a PrimitiveWithSIUnitType and can be found in the package ```de.monticore.lang.types.prettyprint```.

## TypeCheck
The classes for the TypeCheck mechanic can be found in the package ```de.monticore.types.check```. SymTypes can be
synthesized from a SIUnitType and a PrimitiveWithSIUnitType and can be derived from SIUnitLiterals. In addition, the 
original derive classes for expressions from grammars [AssignmentExpressions.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/AssignmentExpressions.mc4)
and [CommonExpressions.mc4](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4) 
were extended to work with SIUnits and SIUnitLiterals as well.