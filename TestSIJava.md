# TestSIJava
The [TestSIJava.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/types/CustomPrimitiveWithSIUnitTypes.mc4)
grammar serves as an example on how one could implement a grammar with SIUnits.
It uses variable and method declaration and all expressions introduced by MontiCore.
### TypeCheck
The [TypeCheckCoCo](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_cocos/TypeCheckCoCo.java)
checks for each variable assignment whether the declared type and the assignment are compatible. This includes 
compatibility checks for SIUnitsTypes and PrimitiveWithSIUnitTypes. In order for this CoCo to work, each variable
has to be assigned a SymType in the SymbolTable creation process.

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

