package wordageddon.config;

public class AppConfig {
    private static String documentiBasePath = "DocumentFolder";
    private static String stopwordsPath = "stopwords-it.txt";

    public static String getDocumentiBasePath() { return documentiBasePath; }
    public static void setDocumentiBasePath(String path) { documentiBasePath = path; }
    public static String getStopwordsPath() { return stopwordsPath; }
    public static void setStopwordsPath(String path) { stopwordsPath = path; }
}