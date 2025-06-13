package wordageddon.model;

import java.util.Map;

/**
 * Rappresenta un documento di testo (.txt) caricato nella cartella selezionata.
 * Contiene il percorso del file, il titolo, una mappa delle occorrenze delle parole e il conteggio totale delle parole.
 * 
 * @author Gruppo 3
 */
public class Document {

   
    private final String path;
   
    private final String title;
   
    private final Map<String, Integer> wordsMap; 
    
    private final int wordsCount;

    /**
     * Costruttore della classe Document.
     * 
     * @param path      percorso assoluto del file
     * @param title     titolo del documento (nome del file)
     * @param wordsMap  mappa delle parole e delle rispettive occorrenze
     */
    public Document(String path, String title, Map<String, Integer> wordsMap) {
        this.path = path;
        this.title = title;
        this.wordsMap = wordsMap;
        this.wordsCount = wordsMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Restituisce il percorso assoluto del file.
     * 
     * @return percorso del file
     */
    public String getPath() {
        return path;
    }

    /**
     * Restituisce il titolo del documento (nome del file).
     * 
     * @return titolo del documento
     */
    public String getTitle() {
        return title;
    }

    /**
     * Restituisce il numero totale di parole presenti nel documento.
     * 
     * @return numero totale di parole
     */
    public int getWordsCount() {
        return wordsCount;
    }

    /**
     * Restituisce la mappa delle parole e delle rispettive occorrenze.
     * 
     * @return mappa delle parole
     */
    public Map<String, Integer> getWordsMap() {
        return wordsMap;
    }
    
    /**
     * Restituisce il titolo del documento senza l'estensione ".txt".
     * 
     * @return titolo senza estensione, oppure il titolo originale se non termina con ".txt"
     */
    public String getTitleWithoutExtension() {
        if (title != null && title.endsWith(".txt")) {
            return title.substring(0, title.length() - 4);
        }
        return title;
    }

    /**
     * Restituisce il titolo del documento come rappresentazione testuale.
     * 
     * @return titolo del documento
     */
    @Override
    public String toString() {
        return title;
    }
}