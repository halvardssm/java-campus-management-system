package nl.tudelft.oopp.group39.config;

import java.util.HashSet;
import java.util.Set;

public interface Utils {
    static <T> Set<T> initSet(Set<T> set) {
        return set != null ? set : new HashSet<>();
    }
}
