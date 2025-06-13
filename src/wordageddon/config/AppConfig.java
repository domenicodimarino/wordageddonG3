package wordageddon.config;

import wordageddon.util.PathUtils;
import java.io.*;
import java.util.Properties;

public class AppConfig {
    private static String documentiBasePath = PathUtils.getDataFolder("DocumentFolder").getAbsolutePath();
    private static String stopwordsPathIT = PathUtils.getDataFilePath("stopwords-it.txt");
    private static String stopwordsPathEN = PathUtils.getDataFilePath("stopwords-en.txt");
    private static final String CONFIG_FILE = PathUtils.getDataFilePath("config.properties");

    static {
        loadConfig();
    }

    public static String getDocumentiBasePath() { return documentiBasePath; }
    public static void setDocumentiBasePath(String path) { 
        documentiBasePath = path; 
        saveConfig();
    }
    public static String getStopwordsPathIT() { return stopwordsPathIT; }
    public static void setStopwordsPathIT(String path) {
        stopwordsPathIT = path;
        saveConfig();
    }
    public static String getStopwordsPathEN() { return stopwordsPathEN; }
    public static void setStopwordsPathEN(String path) {
        stopwordsPathEN = path;
        saveConfig();
    }

    private static void loadConfig() {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            p.load(in);
            documentiBasePath = p.getProperty("documentiBasePath", documentiBasePath);
            stopwordsPathIT = p.getProperty("stopwordsPathIT", stopwordsPathIT);
            stopwordsPathEN = p.getProperty("stopwordsPathEN", stopwordsPathEN);
        } catch (IOException e) {
            // Primo avvio o file mancante: usa i default
        }
    }

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