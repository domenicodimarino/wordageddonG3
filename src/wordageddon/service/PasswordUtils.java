package wordageddon.service;

import java.security.MessageDigest;

/**
 * Classe di utilità per la gestione delle password, fornisce metodi per l'hashing sicuro delle password.
 */
public class PasswordUtils {

    /**
     * Esegue l'hash della password specificata utilizzando l'algoritmo SHA-256.
     *
     * @param password la password da hashare
     * @return la rappresentazione esadecimale dell'hash della password
     * @throws RuntimeException se si verifica un errore durante il processo di hashing
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}