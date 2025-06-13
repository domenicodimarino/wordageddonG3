package wordageddon.model;

import java.util.EnumMap;
import java.util.Map;

/**
 * Gestisce la configurazione delle impostazioni di gioco per ciascun livello di difficoltà.
 * Fornisce un mapping tra {@link Difficolta} e {@link GameDifficultyConfig}.
 */
public class GameConfig {

    /**
     * Mappa delle configurazioni di difficoltà del gioco, indicizzate per {@link Difficolta}.
     */
    private static final Map<Difficolta, GameDifficultyConfig> configMap = new EnumMap<>(Difficolta.class);

    static {
        configMap.put(Difficolta.FACILE, new GameDifficultyConfig(1, 1000, 240));
        configMap.put(Difficolta.MEDIO, new GameDifficultyConfig(2, 1000, 180));
        configMap.put(Difficolta.DIFFICILE, new GameDifficultyConfig(3, 1000, 120));
    }

    /**
     * Restituisce la configurazione associata al livello di difficoltà specificato.
     *
     * @param diff il livello di difficoltà
     * @return la configurazione di gioco per la difficoltà specificata
     */
    public static GameDifficultyConfig getConfig(Difficolta diff) {
        return configMap.get(diff);
    }
}