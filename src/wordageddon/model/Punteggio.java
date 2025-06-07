package wordageddon.model;

import java.time.LocalDateTime;

public class Punteggio {
    private String username;
    private int valore; // Punteggio totale calcolato
    private int risposteCorrette;
    private int tempoResiduo; // in secondi
    private int difficolta;   // es: 1=facile, 2=media, 3=difficile
    private LocalDateTime data; // data e ora della partita

    // Costruttore completo, usato quando leggi dal DB
    public Punteggio(String username, int risposteCorrette, int tempoResiduo, int difficolta, LocalDateTime data) {
        this.username = username;
        this.risposteCorrette = risposteCorrette;
        this.tempoResiduo = tempoResiduo;
        this.difficolta = difficolta;
        this.data = data;
        this.valore = calcolaPunteggio();
    }

    // Costruttore per nuovo punteggio (data = ora attuale)
    public Punteggio(String username, int risposteCorrette, int tempoResiduo, int difficolta) {
        this(username, risposteCorrette, tempoResiduo, difficolta, LocalDateTime.now());
    }

    // Calcola il punteggio secondo la tua logica
    private int calcolaPunteggio() {
        return (risposteCorrette * 30) * difficolta + (risposteCorrette > 0 ? tempoResiduo : 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public int getRisposteCorrette() {
        return risposteCorrette;
    }

    public void setRisposteCorrette(int risposteCorrette) {
        this.risposteCorrette = risposteCorrette;
    }

    public int getTempoResiduo() {
        return tempoResiduo;
    }

    public void setTempoResiduo(int tempoResiduo) {
        this.tempoResiduo = tempoResiduo;
    }

    public int getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(int difficolta) {
        this.difficolta = difficolta;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    
}