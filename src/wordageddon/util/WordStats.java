package wordageddon.util;

import java.util.*;

public class WordStats {
    public final Set<String> parole;
    public final Map<String, Integer> frequenze;

    public WordStats() {
        this.parole = new HashSet<>();
        this.frequenze = new HashMap<>();
    }
}