package wordageddon.config;

public class AppConfig {
    private static String documentiBasePath = "DocumentFolder"; // valore di default

    public static String getDocumentiBasePath() {
        return documentiBasePath;
    }

    public static void setDocumentiBasePath(String path) {
        documentiBasePath = path;
    }
}