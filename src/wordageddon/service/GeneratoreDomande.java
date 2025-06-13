package wordageddon.service;

import wordageddon.model.Domanda;
import wordageddon.model.Document;
import java.util.*;

/**
 * Classe responsabile della generazione di domande casuali a partire da una lista di documenti e un vocabolario.
 * Le domande possono essere di diverse tipologie: frequenza di una parola, confronto tra parole, esclusione e associazione parola-documento.
 */
public class GeneratoreDomande {

    /**
     * Lista dei documenti su cui generare le domande.
     */
    private final List<Document> documenti;

    /**
     * Vocabolario delle parole disponibili per le domande.
     */
    private final Set<String> vocabolario;

    /**
     * Generatore di numeri casuali.
     */
    private final Random random = new Random();

    /**
     * Costruttore della classe GeneratoreDomande.
     *
     * @param documenti   lista dei documenti
     * @param vocabolario set di parole disponibili
     */
    public GeneratoreDomande(List<Document> documenti, Set<String> vocabolario) {
        this.documenti = documenti;
        this.vocabolario = vocabolario;
    }

    /**
     * Genera una domanda casuale, scegliendo tra le tipologie disponibili in base al vocabolario e ai documenti.
     *
     * @return una domanda generata casualmente, o null se non è possibile generare una domanda valida
     */
    public Domanda generaDomandaRandom() {
        List<Integer> tipiValidi = new ArrayList<>();
        if (vocabolario.size() >= 1) tipiValidi.add(0); 
        if (vocabolario.size() >= 2) tipiValidi.add(1); 
        if (documenti.size() > 1 && vocabolario.size() >= 1) tipiValidi.add(2); 
        if (documenti.size() > 1 && vocabolario.size() >= 1) tipiValidi.add(3); 
        if (tipiValidi.isEmpty()) return null;
        int tipo = tipiValidi.get(random.nextInt(tipiValidi.size()));
        switch (tipo) {
            case 0: return generaDomandaFrequenza();
            case 1: return generaDomandaConfronto();
            case 2: return generaDomandaEsclusione();
            case 3: return generaDomandaParolaDocumento();
            default: return null;
        }
    }

    /**
     * Genera una domanda sulla frequenza di una parola nei documenti.
     *
     * @return domanda sulla frequenza di una parola
     */
    private Domanda generaDomandaFrequenza() {
        String parola = scegliParolaRandom();
        int occorrenze = contaOccorrenzeGlobale(parola);
        List<String> opzioni = generaOpzioniNumeriche(occorrenze);
        Collections.shuffle(opzioni);
        return new Domanda(
            "Quante volte compare la parola \"" + parola + "\" in tutti i documenti?",
            opzioni,
            String.valueOf(occorrenze),
            "frequenza"
        );
    }

    /**
     * Genera una domanda di confronto tra due parole.
     *
     * @return domanda di confronto tra parole
     */
    private Domanda generaDomandaConfronto() {
        if (vocabolario.size() < 2) return null;
        String parola1 = scegliParolaRandom();
        String parola2 = scegliParolaRandomDiversa(parola1);
        int freq1 = contaOccorrenzeGlobale(parola1);
        int freq2 = contaOccorrenzeGlobale(parola2);
        String risposta = freq1 == freq2 ? parola1 : (freq1 > freq2 ? parola1 : parola2);
        Set<String> opzioniSet = new LinkedHashSet<>();
        opzioniSet.add(parola1);
        opzioniSet.add(parola2);
        while (opzioniSet.size() < 4) {
            opzioniSet.add(scegliParolaRandomDiversa(parola1, parola2));
        }
        List<String> opzioni = new ArrayList<>(opzioniSet);
        Collections.shuffle(opzioni);
        return new Domanda(
            "Quale parola compare più volte nei documenti?",
            opzioni,
            risposta,
            "confronto"
        );
    }

    /**
     * Genera una domanda di esclusione, in cui si chiede in quale documento una parola non appare.
     *
     * @return domanda di esclusione
     */
    private Domanda generaDomandaEsclusione() {
        String parola = scegliParolaMaiPresente();
        if (parola == null) return null;
        List<String> opzioni = new ArrayList<>();
        for (Document doc : documenti) opzioni.add(doc.getTitleWithoutExtension());
        Collections.shuffle(opzioni);
        return new Domanda(
            "In quale documento NON appare la parola \"" + parola + "\"?",
            opzioni,
            trovaDocumentoDoveNonAppare(parola),
            "esclusione"
        );
    }

    /**
     * Genera una domanda di associazione tra parola e documento.
     *
     * @return domanda di associazione parola-documento
     */
    private Domanda generaDomandaParolaDocumento() {
        if (documenti.size() <= 1) return null;
        Document doc = scegliDocumentoRandom();
        String parola = scegliParolaPresenteIn(doc);
        if (parola == null) return null;
        Set<String> opzioniSet = new LinkedHashSet<>();
        opzioniSet.add(doc.getTitleWithoutExtension());
        while (opzioniSet.size() < 4 && opzioniSet.size() < documenti.size()) {
            String tit = scegliDocumentoRandom().getTitleWithoutExtension();
            opzioniSet.add(tit);
        }
        List<String> opzioni = new ArrayList<>(opzioniSet);
        Collections.shuffle(opzioni);
        return new Domanda(
            "In quale documento appare la parola \"" + parola + "\"?",
            opzioni,
            doc.getTitleWithoutExtension(),
            "parola-documento"
        );
    }

    /**
     * Sceglie una parola casuale dal vocabolario.
     *
     * @return parola casuale
     */
    private String scegliParolaRandom() {
        List<String> parole = new ArrayList<>(vocabolario);
        return parole.get(random.nextInt(parole.size()));
    }

    /**
     * Sceglie una parola casuale dal vocabolario, escludendo alcune parole specificate.
     *
     * @param exclude parole da escludere
     * @return parola casuale diversa da quelle escluse
     */
    private String scegliParolaRandomDiversa(String... exclude) {
        Set<String> excl = new HashSet<>(Arrays.asList(exclude));
        List<String> parole = new ArrayList<>(vocabolario);
        parole.removeAll(excl);
        if (parole.isEmpty()) return null;
        return parole.get(random.nextInt(parole.size()));
    }

    /**
     * Conta il numero totale di occorrenze di una parola in tutti i documenti.
     *
     * @param parola parola da contare
     * @return numero di occorrenze totali
     */
    private int contaOccorrenzeGlobale(String parola) {
        int tot = 0;
        for (Document doc : documenti) {
            Integer count = doc.getWordsMap().get(parola);
            if (count != null) tot += count;
        }
        return tot;
    }

    /**
     * Genera una lista di opzioni numeriche per una domanda a partire dalla risposta corretta.
     *
     * @param corretta valore corretto
     * @return lista di opzioni numeriche come stringhe
     */
    private List<String> generaOpzioniNumeriche(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);
        while (opzioni.size() < 4) {
            int val = Math.max(0, corretta + random.nextInt(6) - 3);
            opzioni.add(val);
        }
        List<String> strOpzioni = new ArrayList<>();
        for (int i : opzioni) strOpzioni.add(String.valueOf(i));
        return strOpzioni;
    }

    /**
     * Sceglie una parola dal vocabolario che non è presente almeno in un documento.
     *
     * @return parola mai presente in almeno un documento, o null se non esiste
     */
    private String scegliParolaMaiPresente() {
        for (String parola : vocabolario) {
            for (Document doc : documenti) {
                Integer count = doc.getWordsMap().get(parola);
                if (count == null || count == 0) {
                    return parola;
                }
            }
        }
        return null;
    }

    /**
     * Trova il titolo del documento in cui una parola non appare.
     *
     * @param parola parola da cercare
     * @return titolo del documento dove la parola non appare, o "Nessuno" se appare ovunque
     */
    private String trovaDocumentoDoveNonAppare(String parola) {
        for (Document doc : documenti) {
            Integer c = doc.getWordsMap().get(parola);
            if (c == null || c == 0) return doc.getTitle();
        }
        return "Nessuno";
    }

    /**
     * Sceglie un documento casuale dalla lista dei documenti.
     *
     * @return documento casuale
     */
    private Document scegliDocumentoRandom() {
        return documenti.get(random.nextInt(documenti.size()));
    }

    /**
     * Sceglie una parola presente nel documento specificato.
     *
     * @param doc documento in cui cercare la parola
     * @return parola presente nel documento, o null se nessuna parola è presente
     */
    private String scegliParolaPresenteIn(Document doc) {
        List<String> parolePresenti = new ArrayList<>();
        for (Map.Entry<String, Integer> e : doc.getWordsMap().entrySet()) {
            if (e.getValue() > 0) parolePresenti.add(e.getKey());
        }
        if (parolePresenti.isEmpty()) return null;
        return parolePresenti.get(random.nextInt(parolePresenti.size()));
    }
}