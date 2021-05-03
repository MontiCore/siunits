# siunits
This documentation is intended for modelers who want to use si unit-based type systems. Language engineers aiming to incorporate
the si units framework into their languages can find a detailed technical description on the si units project [here](https://github.com/MontiCore/siunits/blob/master/src/main/grammars/de/monticore/SIUnits.md)

## Motivation and Basic Examples
In many disciplines software is used to control physical processes. Properties and variables of such processes often
represent physical quantities such as velocities, forces, electrical currents, etc. Unfortunately, 
modern languages such as Java, C++, or Python do not support physical quantities out of the box.
The type systems of these languages are strictly focused on the bare representation of numbers, e.g. integers, floats, doubles,
but they ignore the physical quantities they represent. Hence, developers need to keep track of the
physical meanings of the variables they use and perform checks and conversions manually, which is prone to errors and requires
a lot of effort. 

Our si units project tackles this issue by providing grammars introducing si unit enriched types, physical type checking, and unit conversion functionality.
The framework can be integrated easily into any MontiCore 6/7 based language as has been shown for the SIJava language, a Java-like language 
extended by the possibility to specify physical units for variables. A basic example is given in the following listing:

``` 
/* (c) https://github.com/MontiCore/monticore */

package test.de.monticore.lang.testsijava.testsijava;

siclass Main {

    s<int> main () {
        km/h<double> v = 3 dm/h;
        dm distance = 200000 µm;

        min time = 3 min;
        return calculateTime(v, distance) * 20 + time;
    }

    h<double> calculateTime(km/h v, mm d) {

        double d_R = value(d);
        double d_B = basevalue(d);

        print(d_R);
        print(d_B);
        print(d);

        return d / v;
    }

}
``` 

While it is still possible to use standard basic types such as doubles, some variables are enriched by physical information,
e.g. the velocity variable `v` is defined as `km/h<double>`, i.e. `v` is a double variable representing a quantity in kilometers per hour.
Whenever a variable or a literal is combined with or assigned to an si unit variable like `v`, the compiler checks whether the physical quantities are 
compatible and performs an automated conversion if needed. For instance, in the assignment `km/h<double> v = 3 dm/h;` the value of
`3 dm` needs to be converted to `km/h` first. After a successful conversion, `v` carries the value `0.0003 km/h`. 

Note that units are only needed at compile-time given that we have a static type system. Compatibility checks and conversions are performed
by the compiler and the unit information is thrown away at runtime. For instance, the model given above is generated to the following Java code without explicit unit information:

```
/* (c) https://github.com/MontiCore/monticore */
package test.de.monticore.lang.testsijava.testsijava;

public class Main {

    public int main() {
        double v = (double) ((3.0) / 10000.0);
        double distance = (double) ((200000.0) / 100000.0);
        double time = (double) (3.0);

        return (int) ((((calculateTime(v, (distance) * 100.0) * 20) * 60.0) + time) * 60.0);
    }


    public double calculateTime(double v, double d) {
        double d_R = (double) (d);
        double d_B = (double) (((d) / 1000.0));
        System.out.println(d_R);
        System.out.println(d_B);
        System.out.println(d + "mm");

        return (double) ((d / v) / 1000000.0);
    }
}

``` 

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)
* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)
* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)
