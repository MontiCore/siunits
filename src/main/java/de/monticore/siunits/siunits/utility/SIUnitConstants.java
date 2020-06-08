/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits.siunits.utility;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class SIUnitConstants {
    public static List<String> getAllUnits() {
        String[] prefixes = {"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "d", "c", "m", "µ", "u", "n", "p", "f", "a", "z", "y"};
        String[] unitBases = {"m", "g", "s", "A", "K", "mol", "cd", "Hz", "N", "Pa", "J", "W", "C", "V", "F", "Ohm", "Ω", "S", "Wb", "T", "H", "lm", "lx", "Bq", "Gy", "Sv", "kat", "l", "L"};
        String[] officallyAccepted = {"min", "h", "d", "ha", "t", "au", "Np", "B", "dB", "eV", "Da", "u"};
        String[] dimensionless = {"°", "deg", "rad", "sr"};
        String[] fahrenheitCelcius = {"°C", "°F"};


        List<String> combined = new ArrayList<>();
        combined.addAll(Lists.newArrayList(officallyAccepted));
        combined.addAll(Lists.newArrayList(dimensionless));
        combined.addAll(Lists.newArrayList(fahrenheitCelcius));

        for (String unitBase : unitBases) {
            combined.add(unitBase);
            for (String prefix : prefixes) {
                combined.add(prefix + unitBase);
            }
        }
        return combined;
    }

    //TODO: Return StandardUnits
}
