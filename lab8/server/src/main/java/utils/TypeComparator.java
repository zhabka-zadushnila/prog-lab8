package utils;

import java.util.Comparator;

import structs.classes.Dragon;

/**
 * Simple comparator that is used for default sort method in arrays.
 */
public class TypeComparator implements Comparator<Dragon> {
    @Override
    public int compare(Dragon d1, Dragon d2) {
        return d1.getType().compareTo(d2.getType());
    }
}
