package wordageddon.service;

import wordageddon.model.Domanda;
import wordageddon.model.Document;
import java.util.*;

/**
 * GeneratoreDomande si occupa di generare domande a risposta multipla
 * per la sessione di gioco, sulla base dei dati dei documenti letti.
 */
public class GeneratoreDomande {

    private final List<Document> documenti;
    private final Set<String> vocabolario;
    private final Random random = new Random();

    public GeneratoreDomande(List<Document> documenti, Set<String> vocabolario) {
        this.documenti = documenti;
        this.vocabolario = vocabolario;
    }

    public Domanda generaDomandaRandom() {
        List<Integer> tipiValidi = new ArrayList<>();
        if (vocabolario.size() >= 1) tipiValidi.add(0); // frequenza
        if (vocabolario.size() >= 2) tipiValidi.add(1); // confronto solo se almeno 2 parole
        if (documenti.size() > 1 && vocabolario.size() >= 1) tipiValidi.add(2); // esclusione solo se >1 documento e almeno una parola
        // SOLO SE hai più di UN documento ha senso la domanda "parola-documento"
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

    // Tipologia 1: Domanda sulla frequenza di una parola.
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

    // Tipologia 2: Domanda di confronto tra parole
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

    // Tipologia 3: Domanda di esclusione
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

    // Tipologia 4: Domanda di associazione parola-documento
    private Domanda generaDomandaParolaDocumento() {
        if (documenti.size() <= 1) return null; // sicurezza extra
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

    // --- Utilità interne ---
    private String scegliParolaRandom() {
        List<String> parole = new ArrayList<>(vocabolario);
        return parole.get(random.nextInt(parole.size()));
    }

    private String scegliParolaRandomDiversa(String... exclude) {
        Set<String> excl = new HashSet<>(Arrays.asList(exclude));
        List<String> parole = new ArrayList<>(vocabolario);
        parole.removeAll(excl);
        if (parole.isEmpty()) return null;
        return parole.get(random.nextInt(parole.size()));
    }

    private int contaOccorrenzeGlobale(String parola) {
        int tot = 0;
        for (Document doc : documenti) {
            Integer count = doc.getWordsMap().get(parola);
            if (count != null) tot += count;
        }
        return tot;
    }

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

    private String trovaDocumentoDoveNonAppare(String parola) {
        for (Document doc : documenti) {
            Integer c = doc.getWordsMap().get(parola);
            if (c == null || c == 0) return doc.getTitle();
        }
        return "Nessuno";
    }

    private Document scegliDocumentoRandom() {
        return documenti.get(random.nextInt(documenti.size()));
    }

    private String scegliParolaPresenteIn(Document doc) {
        List<String> parolePresenti = new ArrayList<>();
        for (Map.Entry<String, Integer> e : doc.getWordsMap().entrySet()) {
            if (e.getValue() > 0) parolePresenti.add(e.getKey());
        }
        if (parolePresenti.isEmpty()) return null;
        return parolePresenti.get(random.nextInt(parolePresenti.size()));
    }
}