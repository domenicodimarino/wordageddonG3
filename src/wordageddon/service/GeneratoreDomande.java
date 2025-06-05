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

    /**
     * Genera una lista di domande.
     * @param numeroDomande 
     * @return  
     */
    public List<Domanda> generaDomande(int numeroDomande) {
        List<Domanda> domande = new ArrayList<>();
        // Scelta casuale del tipo di domande
        for (int i = 0; i < numeroDomande; i++) {
            int tipo = random.nextInt(4);
            switch (tipo) {
                case 0: domande.add(generaDomandaFrequenza()); break;
                case 1: domande.add(generaDomandaConfronto()); break;
                case 2: {
                    Domanda domanda = generaDomandaEsclusione();
                    if (domanda != null)
                    {
                        domande.add(domanda);
                    }
                    else
                    {
                        i--; //in modo che la domanda non venga conteggiata.
                    }
                } break;
                case 3: domande.add(generaDomandaParolaDocumento()); break;
            }
        }
        return domande;
    }

    // Tipologia 1: Domanda sulla frequenza di una parola.
    private Domanda generaDomandaFrequenza() {
        // Scegli una parola frequente
        String parola = scegliParolaRandom();
        int occorrenze = contaOccorrenzeGlobale(parola);
        // Genera opzioni (distrattori)
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
        String parola1 = scegliParolaRandom();
        String parola2 = scegliParolaRandomDiversa(parola1);
        int freq1 = contaOccorrenzeGlobale(parola1);
        int freq2 = contaOccorrenzeGlobale(parola2);
        String risposta = freq1 > freq2 ? parola1 : parola2;
        List<String> opzioni = Arrays.asList(parola1, parola2, scegliParolaRandomDiversa(parola1, parola2), scegliParolaRandomDiversa(parola1, parola2));
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
        if (parola == null) return null; // oppure salta questa domanda
        List<String> opzioni = new ArrayList<>();
        for (Document doc : documenti) opzioni.add(doc.getTitle());
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
        Document doc = scegliDocumentoRandom();
        String parola = scegliParolaPresenteIn(doc);
        List<String> opzioni = new ArrayList<>();
        opzioni.add(doc.getTitle());
        while (opzioni.size() < 4) {
            String tit = scegliDocumentoRandom().getTitle();
            if (!opzioni.contains(tit)) opzioni.add(tit);
        }
        Collections.shuffle(opzioni);
        return new Domanda(
            "In quale documento appare la parola \"" + parola + "\"?",
            opzioni,
            doc.getTitle(),
            "parola-documento"
        );
    }

    // --- Utilità interne ---
    private String scegliParolaRandom() {
        int idx = random.nextInt(vocabolario.size());
        return new ArrayList<>(vocabolario).get(idx);
    }

    private String scegliParolaRandomDiversa(String... exclude) {
        Set<String> excl = new HashSet<>(Arrays.asList(exclude));
        List<String> parole = new ArrayList<>(vocabolario);
        parole.removeAll(excl);
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

    // per domande su numero di occorrenze
    private List<String> generaOpzioniNumeriche(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);
        while (opzioni.size() < 4) {
            int val = Math.max(0, corretta + random.nextInt(6) - 3);
            // random.nextInt(6) - 3 dà dei valori compresi tra -3 e +3
            // questo in modo tale da dare opzioni vicini alla risposta corretta.
            opzioni.add(val);
        }
        List<String> strOpzioni = new ArrayList<>();
        for (int i : opzioni) strOpzioni.add(String.valueOf(i));
        return strOpzioni;
    }

    // Da implementare.
    // “Quale parola NON compare in tutti i documenti?”
    // Oppure per trovare parole che sono “escluse” da qualche documento.
    private String scegliParolaMaiPresente() {
        for (String parola : vocabolario) {
            for (Document doc : documenti) {
                Integer count = doc.getWordsMap().get(parola);
                if (count == null || count == 0) {
                    return parola;
                }
            }
        }
        // se tutte le parole compaiono nei documenti.
        return null;
    }

    //“In quale documento NON appare la parola X?”
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
        return parolePresenti.isEmpty() ? scegliParolaRandom() : parolePresenti.get(random.nextInt(parolePresenti.size()));
    }
}