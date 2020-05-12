# TestSIJava
The [TestSIJava.mc4](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/grammars/de/monticore/lang/types/CustomPrimitiveWithSIUnitTypes.mc4)
grammar serves as an example on how one could implement a grammar with SIUnits.
It uses variable and method declaration and all expressions introduced by MontiCore.
### TypeCheck
The [TypeCheckCoCo](https://git.rwth-aachen.de/monticore/languages/siunits/-/blob/master/src/test/java/de/monticore/lang/testsijava/testsijava/_cocos/TypeCheckCoCo.java)
checks for each variable assignment whether the declared type and the assignment are compatible. This includes 
compatibility checks for SIUnitsTypes and PrimitiveWithSIUnitTypes. In order for this CoCo to work, each variable
has to be assigned a SymType in the SymbolTable creation process.