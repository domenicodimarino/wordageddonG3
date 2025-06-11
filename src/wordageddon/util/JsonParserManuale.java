package wordageddon.util;

import wordageddon.model.Domanda;
import wordageddon.model.RispostaUtente;
import wordageddon.model.StatoGioco;

import java.util.ArrayList;
import java.util.List;

public class JsonParserManuale {

    public static StatoGioco parseStatoGioco(String json) {
        List<Domanda> domande = new ArrayList<>();
        List<RispostaUtente> risposteUtente = new ArrayList<>();
        int domandaCorrente = 0;

        try {
            String domandeRaw = estraiArray(json, "domande");
            String[] domandeItems = separaOggetti(domandeRaw);
            for (String item : domandeItems) {
                String testo = estraiValore(item, "testo");
                String corretta = estraiValore(item, "rispostaCorretta");
                String tipo = estraiValore(item, "tipo");
                String opzioniRaw = estraiArray(item, "opzioni");
                List<String> opzioni = separaArrayElementi(opzioniRaw);
                domande.add(new Domanda(testo, opzioni, corretta, tipo));
            }

            String risposteRaw = estraiArray(json, "risposteUtente");
            String[] risposteItems = separaOggetti(risposteRaw);
            for (String item : risposteItems) {
                int index = Integer.parseInt(estraiValore(item, "domandaIndex"));
                String scelta = estraiValore(item, "sceltaUtente");
                String testo = estraiValore(item, "testoDomanda");
                String corretta = estraiValore(item, "rispostaCorretta");
                boolean esatto = Boolean.parseBoolean(estraiValore(item, "esatto"));
                risposteUtente.add(new RispostaUtente(index, testo, scelta, corretta, esatto));
            }

            String correnteRaw = estraiValore(json, "domandaCorrente");
            domandaCorrente = Integer.parseInt(correnteRaw);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StatoGioco(domande, risposteUtente, domandaCorrente);
    }

    private static String estraiValore(String source, String chiave) {
        int i = source.indexOf("\"" + chiave + "\"");
        if (i == -1) return "";
        int start = source.indexOf(":", i) + 1;
        if (source.charAt(start) == '"') start++;
        int end = source.indexOf(",", start);
        if (end == -1) end = source.indexOf("}", start);
        if (end == -1) end = source.length();
        String val = source.substring(start, end).trim();
        return ripulisci(val);
    }

    private static String estraiArray(String source, String chiave) {
        int start = source.indexOf("\"" + chiave + "\":[");
        if (start == -1) return "";
        start = source.indexOf("[", start) + 1;
        int end = start;
        int brackets = 1;

        while (end < source.length() && brackets > 0) {
            if (source.charAt(end) == '[') brackets++;
            else if (source.charAt(end) == ']') brackets--;
            end++;
        }

        return source.substring(start, end - 1);
    }

    private static String[] separaOggetti(String array) {
        List<String> oggetti = new ArrayList<>();
        int braces = 0;
        int start = 0;
        for (int i = 0; i < array.length(); i++) {
            if (array.charAt(i) == '{') {
                if (braces == 0) start = i;
                braces++;
            } else if (array.charAt(i) == '}') {
                braces--;
                if (braces == 0) oggetti.add(array.substring(start, i + 1));
            }
        }
        return oggetti.toArray(new String[0]);
    }

    private static List<String> separaArrayElementi(String rawArray) {
        List<String> elementi = new ArrayList<>();
        StringBuilder corrente = new StringBuilder();
        boolean inString = false;
        for (int i = 0; i < rawArray.length(); i++) {
            char c = rawArray.charAt(i);
            if (c == '\"') continue;
            if (c == '"') {
                inString = !inString;
            } else if (c == ',' && !inString) {
                elementi.add(ripulisci(corrente.toString()));
                corrente.setLength(0);
            } else {
                corrente.append(c);
            }
        }
        if (corrente.length() > 0) {
            elementi.add(ripulisci(corrente.toString()));
        }
        return elementi;
    }

    private static String ripulisci(String s) {
        return s.replaceAll("^\"|\"$", "").replace("\\\"", "\"").replace("\\n", "\n");
    }
}
