package wordageddon.util;

import java.io.File;

public class PathUtils {
    public static File getJarDir() {
        try {
            String path = PathUtils.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath();
            File jar = new File(path);
            return jar.getParentFile();
        } catch (Exception e) {
            return new File(".");
        }
    }

    // Cerca PRIMA nella cartella superiore rispetto al jar (caso jar in dist/)
    // Se non trova, cerca nella working dir (caso esecuzione da IDE/root progetto)
    public static String getDataFilePath(String fileName) {
        File jarDir = getJarDir();
        File parent = jarDir.getParentFile();
        File primary = new File(parent, fileName);
        if (primary.exists()) {
            return primary.getAbsolutePath();
        }
        File fallback = new File(fileName);
        return fallback.getAbsolutePath();
    }

    // Per cartelle (es. DocumentFolder)
    public static File getDataFolder(String folderName) {
        File jarDir = getJarDir();
        File parent = jarDir.getParentFile();
        File primary = new File(parent, folderName);
        if (primary.exists()) {
            return primary;
        }
        File fallback = new File(folderName);
        return fallback;
    }
}