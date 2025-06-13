package wordageddon.util;

import java.util.*;

/**
 * Classe di utilità per la rappresentazione delle statistiche sulle parole.
 * Contiene un insieme di parole uniche e una mappa delle frequenze di ciascuna parola.
 */
public class WordStats {
    /**
     * Insieme delle parole uniche trovate.
     */
    public final Set<String> parole;

    /**
     * Mappa delle frequenze di ogni parola (parola → numero di occorrenze).
     */
    public final Map<String, Integer> frequenze;

    /**
     * Costruttore che inizializza l'insieme delle parole e la mappa delle frequenze.
     */
    public WordStats() {
        this.parole = new HashSet<>();
        this.frequenze = new HashMap<>();
    }
}