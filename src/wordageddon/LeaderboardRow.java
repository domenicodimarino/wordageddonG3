package wordageddon;

import wordageddon.model.Difficolta;

public class LeaderboardRow {
    private final int posizione;
    private final String username;
    private final int risposteCorrette;
    private final String difficolta;
    private final int punteggio;

    public LeaderboardRow(int posizione, String username, int risposteCorrette, Difficolta difficolta, int punteggio) {
        this.posizione = posizione;
        this.username = username;
        this.risposteCorrette = risposteCorrette;
        this.difficolta = difficolta.toString();
        this.punteggio = punteggio;
    }

    public int getPosizione() {
        return posizione;
    }

    public String getUsername() {
        return username;
    }

    public int getRisposteCorrette() {
        return risposteCorrette;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public int getPunteggio() {
        return punteggio;
    }

    
}