package wordageddon.model;

import java.util.EnumMap;
import java.util.Map;

public class GameConfig {
    private static final Map<Difficolta, GameDifficultyConfig> configMap = new EnumMap<>(Difficolta.class);

    static {
        configMap.put(Difficolta.FACILE, new GameDifficultyConfig(1, 200, 60));
        configMap.put(Difficolta.MEDIO, new GameDifficultyConfig(2, 350, 45));
        configMap.put(Difficolta.DIFFICILE, new GameDifficultyConfig(3, 500, 30));
    }

    public static GameDifficultyConfig getConfig(Difficolta diff) {
        return configMap.get(diff);
    }
}