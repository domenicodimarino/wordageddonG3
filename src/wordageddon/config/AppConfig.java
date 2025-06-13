package wordageddon.config;

import wordageddon.util.PathUtils;
import java.io.*;
import java.util.Properties;

/**
 * Classe di configurazione centralizzata per l'applicazione Wordageddon.
 * Gestisce i percorsi delle cartelle e file principali (documenti e stopwords)
 * e permette il salvataggio/caricamento automatico delle preferenze tramite file di properties.
 */
public class AppConfig {
    
    private static String documentiBasePath = PathUtils.getDataFolder("DocumentFolder").getAbsolutePath();
    private static String stopwordsPathIT = PathUtils.getDataFilePath("stopwords-it.txt");
    private static String stopwordsPathEN = PathUtils.getDataFilePath("stopwords-en.txt");
    private static final String CONFIG_FILE = PathUtils.getDataFilePath("config.properties");

   
    static {
        loadConfig();
    }

    /**
     * Restituisce il percorso della cartella base dei documenti.
     * @return percorso assoluto della cartella documenti
     */
    public static String getDocumentiBasePath() { return documentiBasePath; }

    /**
     * Imposta e salva il percorso della cartella base dei documenti.
     * @param path nuovo percorso della cartella documenti
     */
    public static void setDocumentiBasePath(String path) {
        documentiBasePath = path;
        saveConfig();
    }

    /**
     * Restituisce il percorso del file delle stopwords italiane.
     * @return percorso assoluto del file stopwords italiane
     */
    public static String getStopwordsPathIT() { return stopwordsPathIT; }

    /**
     * Imposta e salva il percorso del file delle stopwords italiane.
     * @param path nuovo percorso del file stopwords italiane
     */
    public static void setStopwordsPathIT(String path) {
        stopwordsPathIT = path;
        saveConfig();
    }

    /**
     * Restituisce il percorso del file delle stopwords inglesi.
     * @return percorso assoluto del file stopwords inglesi
     */
    public static String getStopwordsPathEN() { return stopwordsPathEN; }

    /**
     * Imposta e salva il percorso del file delle stopwords inglesi.
     * @param path nuovo percorso del file stopwords inglesi
     */
    public static void setStopwordsPathEN(String path) {
        stopwordsPathEN = path;
        saveConfig();
    }

    /**
     * Carica la configurazione dai file properties, aggiornando i percorsi se presenti.
     * Se il file non esiste o non è leggibile lascia i valori di default.
     */
    private static void loadConfig() {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            p.load(in);
            documentiBasePath = p.getProperty("documentiBasePath", documentiBasePath);
            stopwordsPathIT = p.getProperty("stopwordsPathIT", stopwordsPathIT);
            stopwordsPathEN = p.getProperty("stopwordsPathEN", stopwordsPathEN);
        } catch (IOException e) {
           
        }
    }

    /**
     * Salva la configurazione corrente nei file properties.
     */
    private static void saveConfig() {
        Properties p = new Properties();
        p.setProperty("documentiBasePath", documentiBasePath);
        p.setProperty("stopwordsPathIT", stopwordsPathIT);
        p.setProperty("stopwordsPathEN", stopwordsPathEN);
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            p.store(out, "Configurazione Wordageddon");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}