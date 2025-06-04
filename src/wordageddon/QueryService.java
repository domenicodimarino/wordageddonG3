package wordageddon;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * QueryService represents the Service that links
 * the words of the query and their occurences in a Map
 * @author group11
 */
public class QueryService extends Service<Map<String, Integer>> {

    private final Set<String> vocabulary;
    private final String[] queryWords;

    /**
     * Constructor of QueryService
     * @param queryWords the words of the query sent by the user
     * @param vocabulary the set of words of all the files
     */    
    public QueryService(String[] queryWords, Set<String> vocabulary) {
        this.queryWords = queryWords;
        this.vocabulary = vocabulary;
    }

    @Override
    protected Task<Map<String, Integer>> createTask() {

        return new Task<Map<String, Integer>>() {

            /**
             * return a Map:
             * - keys (String) are the words of the vocabulary;
             * - values (Integer) are the number of
             * occurences of the query words in the vocabulary.
             */
            @Override
            protected Map<String, Integer> call() {

                // Creazione della QueryMap
                Map<String, Integer> queryMap = new LinkedHashMap<>();

                vocabulary.forEach(word -> {
                    queryMap.put(word, occurencyCounter(word));
                });
                
                /*
                vocabulary.forEach(word -> {
                    int count = 0;
                    for (String qw : queryWords) {
                        qw = qw.replaceAll("[\\,\\-\\:\\;\\?\\!\\+\\-\\_\\.\\—\\–]", "");
                        if (word.equals(qw)) {
                            count++;
                        }
                    }
                    queryMap.put(word, count);
                });
                */
                return queryMap;
            }
        };
    }

    /**
     * 
     * @param s the word of the vocabulary
     * @return the number of occurency of s in the query
     */
    private int occurencyCounter(String s) {
        return (int) Stream.of(queryWords)
                .filter(w -> !w.contains("[\\,\\-\\:\\;\\?\\!\\+\\-\\_\\.\\—\\–]"))
                .filter(w -> w.equals(s))
                .count();
    }
}
