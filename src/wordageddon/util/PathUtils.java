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

    
    public static String getDataFilePath(String fileName) {
        File jarDir = getJarDir();
        File primary = new File(jarDir, fileName);
        if (primary.exists()) {
            return primary.getAbsolutePath();
        }
        File fallback = new File(fileName);
        return fallback.getAbsolutePath();
    }

    public static File getDataFolder(String folderName) {
        File jarDir = getJarDir();
        File primary = new File(jarDir, folderName);
        if (primary.exists()) {
            return primary;
        }
        File fallback = new File(folderName);
        return fallback;
    }
}