package wordageddon.model;

public class GameDifficultyConfig {
    private int numDocumenti;
    private int lunghezzaTesto;
    private int tempoLettura; 

    public GameDifficultyConfig(int numDocumenti, int lunghezzaTesto, int tempoLettura) {
        this.numDocumenti = numDocumenti;
        this.lunghezzaTesto = lunghezzaTesto;
        this.tempoLettura = tempoLettura;
    }

    public int getNumDocumenti() {
        return numDocumenti;
    }

    public int getLunghezzaTesto() {
        return lunghezzaTesto;
    }

    public int getTempoLettura() {
        return tempoLettura;
    }
}