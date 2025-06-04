package wordageddon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Score represents the index of relevance of a file.
 * @author group11
 */
public class Score {

    private BigDecimal ri;

    /**
     * Constructor of Score.
     */
    public Score() {
        ri = BigDecimal.valueOf(0);
        ri = ri.setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * 
     * @return the relevance of the index
     */
    public BigDecimal getRi() {
        return ri;
    }

    /**
     * Calculate the index of relevance based on the query.
     * @param wordsMap the map of the words of a single document and their occurences
     * @param query the map of the query sent by the user and their occurences
     */
    public void calculateIndex(Map<String, Integer> wordsMap, Map<String, Integer> query) {

        List<Integer> queryArray = new ArrayList<>();
        query.values().forEach(n -> {
            queryArray.add(n);
        });

        List<Integer> wordsArray = new ArrayList<>();
        wordsMap.values().forEach(n -> {
            wordsArray.add(n);
        });

        int n = 0;
        for (int i = 0; i < query.values().size(); i++) {
            n += queryArray.get(i) * wordsArray.get(i);
        }

        double queryModule = calculateModule(queryArray);
        double wordsModule = calculateModule(wordsArray);

        double result = n / (queryModule * wordsModule);

        try {
            ri = BigDecimal.valueOf(result);
        } catch (NumberFormatException ex) {
            ri = BigDecimal.valueOf(0);
        }
        ri = ri.setScale(3, RoundingMode.HALF_UP);

    }

    /**
     * 
     * @param vector the list of integer
     * @return the module of vector
     */
    private double calculateModule(List<Integer> vector) {
        /*
        int sum = 0;
        for(int n : vector) {
            sum += n * n;
        }
        return Math.sqrt(sum);
         */
        return Math.sqrt(
                vector.stream()
                        .mapToDouble(n -> n * n)
                        .sum()
        );
    }

    @Override
    public String toString() {
        return ri.toString();
    }
}
