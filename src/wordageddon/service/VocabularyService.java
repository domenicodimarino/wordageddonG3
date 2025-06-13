package wordageddon.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio JavaFX che estrae un vocabolario di parole uniche da tutti i file di testo
 * presenti in una directory specificata, escludendo eventuali stop word fornite.
 * L'elaborazione viene eseguita in un thread separato tramite JavaFX {@link Service}.
 */
public class VocabularyService extends Service<Set<String>> {

    /**
     * Percorso della directory contenente i file di testo da cui estrarre il vocabolario.
     */
    private final String directoryPath;

    /**
     * Insieme di stop word da escludere dal vocabolario.
     */
    private final Set<String> stopWords;

    /**
     * Costruttore della classe VocabularyService.
     *
     * @param directoryPath percorso della directory contenente i file di testo
     * @param stopWords insieme di parole da escludere dal vocabolario
     */
    public VocabularyService(String directoryPath, Set<String> stopWords) {
        this.directoryPath = directoryPath;
        this.stopWords = stopWords;
    }

    /**
     * Crea il task JavaFX che esegue l'estrazione asincrona del vocabolario.
     *
     * @return un {@link Task} che elabora i file della directory e restituisce il vocabolario
     */
    @Override
    protected Task<Set<String>> createTask() {

        return new Task<Set<String>>() {

            /**
             * Elabora tutti i file .txt nella directory specificata,
             * estraendo le parole (filtrate dalle stop word e da caratteri speciali)
             * e restituendo l'insieme risultante.
             *
             * @return insieme delle parole presenti nei file, escluse le stop word
             */
            @Override
            protected Set<String> call() {

                Set<String> vocabulary = new HashSet<>();

                try (Stream<Path> paths = Files.list(Paths.get(directoryPath))) {
                    paths.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".txt"))
                            .forEach(file -> {
                                try {
                                    Files.lines(file)
                                            .flatMap(line -> Stream.of(line.split("\\s+")))
                                            .map(String::toLowerCase)
                                            .map(s -> s.replaceAll("[\\,\\-\\:\\;\\?\\!\\+\\-\\_\\.\\—\\–\\(\\)\\«\\»\\…]", ""))
                                            .map(s -> {
                                                if (s.contains("’")) {
                                                    String[] newS = s.split("’");
                                                    s = s.substring(newS[0].length() + 1);
                                                }
                                                return s;
                                            })
                                            .map(s -> {
                                                if (s.contains("'")) {
                                                    String[] newS = s.split("'");
                                                    s = s.substring(newS[0].length() + 1);
                                                }
                                                return s;
                                            }).forEach(s -> {
                                                boolean flag = true;
                                                for (String stopWord : stopWords) {
                                                    if (s.equalsIgnoreCase(stopWord)) {
                                                        flag = false;
                                                    }
                                                }
                                                if (flag) {
                                                    vocabulary.add(s);
                                                }
                                            });

                                    vocabulary.removeIf(f -> f.matches("\\s*"));

                                } catch (IOException ex) {
                                   
                                }
                            });
                } catch (IOException e) {
                    
                }

                return vocabulary;
            }
        };
    }
}