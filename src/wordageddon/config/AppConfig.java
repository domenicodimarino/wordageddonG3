package wordageddon.config;

import java.io.*;
import java.util.Properties;

public class AppConfig {
    private static String documentiBasePath = "DocumentFolder";
    private static String stopwordsPath = "stopwords-it.txt";
    private static final String CONFIG_FILE = "config.properties";

    static {
        // Carica al primo utilizzo
        loadConfig();
    }

    public static String getDocumentiBasePath() { return documentiBasePath; }
    public static void setDocumentiBasePath(String path) { 
        documentiBasePath = path; 
        saveConfig();
    }
    public static String getStopwordsPath() { return stopwordsPath; }
    public static void setStopwordsPath(String path) { 
        stopwordsPath = path; 
        saveConfig();
    }

    private static void loadConfig() {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            p.load(in);
            documentiBasePath = p.getProperty("documentiBasePath", documentiBasePath);
            stopwordsPath = p.getProperty("stopwordsPath", stopwordsPath);
        } catch (IOException e) {
            // Primo avvio o file mancante: usa i default
        }
    }

    private static void saveConfig() {
        Properties p = new Properties();
        p.setProperty("documentiBasePath", documentiBasePath);
        p.setProperty("stopwordsPath", stopwordsPath);
        try (OutputStream out = new FileOutputStream(CONFIG_FILE)) {
            p.store(out, "Configurazione Wordageddon");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}