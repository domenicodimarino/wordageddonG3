package wordageddon.util;

import java.io.File;

/**
 * Classe di utilità per la gestione dei percorsi di file e cartelle relativi al JAR dell'applicazione.
 */
public class PathUtils {

    /**
     * Restituisce la directory in cui si trova il file JAR dell'applicazione.
     *
     * @return la directory del JAR come oggetto {@link File}; se non è possibile determinare il percorso, restituisce la directory corrente (".")
     */
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

    /**
     * Restituisce il percorso assoluto di un file dati, cercando prima nella directory del JAR e poi come fallback nella directory corrente.
     *
     * @param fileName il nome del file dati da cercare
     * @return il percorso assoluto del file dati
     */
    public static String getDataFilePath(String fileName) {
        File jarDir = getJarDir();
        File primary = new File(jarDir, fileName);
        if (primary.exists()) {
            return primary.getAbsolutePath();
        }
        File fallback = new File(fileName);
        return fallback.getAbsolutePath();
    }

    /**
     * Restituisce un oggetto {@link File} rappresentante una cartella dati, cercando prima nella directory del JAR e poi come fallback nella directory corrente.
     *
     * @param folderName il nome della cartella dati da cercare
     * @return l'oggetto File rappresentante la cartella dati
     */
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