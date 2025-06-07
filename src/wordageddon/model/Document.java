package wordageddon.model;

import java.util.Map;

/**
 * Document represents the .txt file in the selected folder.
 * @author Gruppo 3
 */
public class Document {

    private final String path;
    private final String title;
    private final Map<String, Integer> wordsMap; // parola -> frequenza
    private final int wordsCount;

    public Document(String path, String title, Map<String, Integer> wordsMap) {
        this.path = path;
        this.title = title;
        this.wordsMap = wordsMap;
        this.wordsCount = wordsMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }

    @Override
    public String toString() {
        return title;
    }
}