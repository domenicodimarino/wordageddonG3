package wordageddon.model;

import java.util.EnumMap;
import java.util.Map;

public class GameConfig {
    private static final Map<Difficolta, GameDifficultyConfig> configMap = new EnumMap<>(Difficolta.class);

    static {
        configMap.put(Difficolta.FACILE, new GameDifficultyConfig(1, 1000, 60));
        configMap.put(Difficolta.MEDIO, new GameDifficultyConfig(2, 1000, 45));
        configMap.put(Difficolta.DIFFICILE, new GameDifficultyConfig(3, 1000, 30));
    }

    public static GameDifficultyConfig getConfig(Difficolta diff) {
        return configMap.get(diff);
    }
}