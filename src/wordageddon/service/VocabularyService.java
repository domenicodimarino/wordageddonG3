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


public class VocabularyService extends Service<Set<String>> {

    private final String directoryPath;
    private final Set<String> stopWords;

    
    public VocabularyService(String directoryPath, Set<String> stopWords) {
        this.directoryPath = directoryPath;
        this.stopWords = stopWords;
    }

    @Override
    protected Task<Set<String>> createTask() {

        return new Task<Set<String>>() {
            
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
