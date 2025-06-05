package wordageddon;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Document represents the .txt file in the selected folder.
 * @author group11
 */
public class Document {

    private final String path;
    private final String title;
    // private final Score score;
    private int wordsCount = 0;
    private final Set<String> vocabulary;
    private Map<String, Integer> wordsMap;
    
    /**
     * Constructor of Document
     * @param path, the path of the document
     * @param title, the name of the document
     * @param vocabulary, the set of words in the document
     * @throws IOException
     */
    public Document(String path, String title, Set<String> vocabulary) throws IOException {
        this.path = path;
        this.title = title;
        // this.score = new Score();
        this.vocabulary = vocabulary;
        wordsCounter();
        createWordsMap();
    }
    
    /**
     *
     * @return path
     */
    public String getPath() {
        return path;
    }
    
    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }
    
    
    /**
     *
     * @return number of the words
     */
    public int getWordsCount() {
        return wordsCount;
    }
    
    /**
     *
     * @return map of the words
     */
    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }
    
    /**
     * Called by the constructor.
     * Count the words in the document.
     * @throws IOException
     */
    private void wordsCounter() throws IOException {
        
        try(Scanner s = new Scanner(new BufferedReader(new FileReader(new File(path))))) {
            
            s.useDelimiter("\\s+");
            
            while(s.hasNext()) {
                s.next();
                wordsCount++;
            }
        }
    }
    
    /**
     * Called by the constructor.
     * Create a LinkedHashMap:
     * - keys (String) are the words of the Document;
     * - values (Integer) are the number of their occurrences.
     */
    private void createWordsMap() throws IOException {
        
        wordsMap = new LinkedHashMap<>();
        
        for (String word : vocabulary) {
            int count = 0;
            try(Scanner s = new Scanner(new BufferedReader(new FileReader(new File(path))))) {
                
                s.useDelimiter("\\s+|\\,|\\-|\\:|\\;|\\?|\\!|\\+|\\-|\\_|\\.|\\—|\\–|\\'|\\’");
                
                while(s.hasNext()) {
                    if(s.next().equalsIgnoreCase(word)) {
                        count++;
                    }
                }
            }
            wordsMap.put(word, count);
        }
    }
    
    @Override
    public String toString() {
        return title;
    }
}
