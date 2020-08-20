/* (c) https://github.com/MontiCore/monticore */
package de.monticore.siunits.utility;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SIUnitConstants {

    public static String[] prefixes =
            {"Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "d", "c", "m",
                    "µ", "u", "n", "p", "f", "a", "z", "y"};
    public static String[] unitBases =
            {"m", "g", "s", "A", "K", "mol", "cd", "Hz", "N", "Pa", "J", "W",
                    "C", "V", "F", "Ohm", "Ω", "S", "Wb", "T", "H", "lm", "lx",
                    "Bq", "Gy", "Sv", "kat", "l", "L"};
    public static String[]
            unitBasesWithoutPrefix =
            {"min", "h", "d", "ha", "t", "au", "Np", "B", "dB", "eV", "Da",
                    "u"};
    public static String[] dimensionless = {"°", "deg", "rad", "sr"};
    public static String[] fahrenheitCelcius = {"°C", "°F"};


    private static List<String> prefixUnits;
    private static List<String> allUnits;

    public static List<String> getAllUnits() {
        if (allUnits == null) {

            allUnits = new ArrayList<>();
            allUnits.addAll(Lists.newArrayList(unitBasesWithoutPrefix));
            allUnits.addAll(Lists.newArrayList(dimensionless));
            allUnits.addAll(Lists.newArrayList(fahrenheitCelcius));

            for (String unitBase : unitBases) {
                allUnits.add(unitBase);
                for (String prefix : prefixes) {
                    allUnits.add(prefix + unitBase);
                }
            }
        }
        return allUnits;
    }

    public static List<String> getUnitsWithPrefix() {
        if (prefixUnits == null) {
            prefixUnits = new LinkedList<>();
            for (String unitBase : unitBases) {
                for (String prefix : prefixes) {
                    prefixUnits.add(prefix + unitBase);
                }
            }
        }
        return prefixUnits;
    }
}
