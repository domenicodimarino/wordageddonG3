package wordageddon.model;

public enum Lingua {
    ITALIANO("ita"),
    INGLESE("eng");

    private final String folderName;

    Lingua(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    @Override
    public String toString() {
        switch(this) {
            case ITALIANO: return "Italiano";
            case INGLESE: return "Inglese";
            default: return super.toString();
        }
    }

    public static Lingua fromString(String s) {
        if (s == null) return ITALIANO; // default
        if (s.equalsIgnoreCase("Italiano") || s.equalsIgnoreCase("ita")) return ITALIANO;
        if (s.equalsIgnoreCase("Inglese") || s.equalsIgnoreCase("eng")) return INGLESE;
        throw new IllegalArgumentException("Lingua non riconosciuta: " + s);
    }
}