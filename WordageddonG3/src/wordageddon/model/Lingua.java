package wordageddon.model;

/**
 * Enum che rappresenta le lingue supportate dal gioco.
 * Ogni lingua è associata al nome della cartella corrispondente.
 */
public enum Lingua {
    
    ITALIANO("ita"),
    INGLESE("eng");

    /**
     * Nome della cartella associata alla lingua.
     */
    private final String folderName;

    /**
     * Costruttore dell'enum Lingua.
     *
     * @param folderName nome della cartella associata alla lingua
     */
    Lingua(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Restituisce il nome della cartella associata alla lingua.
     *
     * @return nome della cartella
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Restituisce la rappresentazione in stringa della lingua per l'utente.
     *
     * @return "Italiano" o "Inglese" a seconda della lingua
     */
    @Override
    public String toString() {
        switch(this) {
            case ITALIANO: return "Italiano";
            case INGLESE: return "Inglese";
            default: return super.toString();
        }
    }

    /**
     * Restituisce la lingua corrispondente alla stringa specificata.
     *
     * @param s stringa rappresentante la lingua ("Italiano", "ita", "Inglese", "eng")
     * @return l'enum {@link Lingua} corrispondente
     * @throws IllegalArgumentException se la stringa non corrisponde a nessuna lingua supportata
     */
    public static Lingua fromString(String s) {
        if (s == null) return ITALIANO; 
        if (s.equalsIgnoreCase("Italiano") || s.equalsIgnoreCase("ita")) return ITALIANO;
        if (s.equalsIgnoreCase("Inglese") || s.equalsIgnoreCase("eng")) return INGLESE;
        throw new IllegalArgumentException("Lingua non riconosciuta: " + s);
    }
}