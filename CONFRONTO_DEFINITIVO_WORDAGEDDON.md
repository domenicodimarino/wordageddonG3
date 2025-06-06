# Confronto definitivo Checklist ↔ Traccia ↔ File di Repo (giugno 2025)

## 1. Login / Registrazione / Multiutente
- **Checklist:** Giorno 2 “Il Dom” — login/registrazione: ✓ FATTO
- **Traccia:** Multiutente, login, salvataggio sessioni/punteggi
- **Repo:**  
  - `Utente.java`, `UtenteDAO`, `UtenteDAOSQL`, `UtenteService`, `PasswordUtils` — tutto presente e funzionante
  - Test automatici su DAO utenti
- **Stato:**  
  ✔️ COMPLETO

---

## 2. Database / Persistenza Sessioni & Punteggi
- **Checklist:** Giorno 1 e 3 — setup database, salvataggio sessioni, storico punteggi: ✓ FATTO
- **Traccia:** Ogni sessione produce un punteggio, storicizzato
- **Repo:**  
  - `DatabaseManager.java` (schema ok)
  - `Sessione.java`, `Punteggio.java`, DAO e test relativi
- **Stato:**  
  ✔️ COMPLETO lato backend/test
  🟡 Manca solo l’integrazione sicura nella GUI (cioè che ogni sessione e punteggio vengano salvati davvero quando si gioca)

---

## 3. Caricamento documenti, stopwords, analisi testi
- **Checklist:** Giorno 2 John — GUI caricamento documenti/stopwords, area admin: 🟡 INCOMPLETO
- **Traccia:** Caricamento file e stopwords, analisi testi
- **Repo:**  
  - `WordageddonController.java` — logica per scegliere cartella documenti e file stopwords, usa `VocabularyService`
  - `Document.java`, `VocabularyService.java` — parsing file, filtraggio stopwords, conteggio parole, vocabolario
- **Stato:**  
  ✔️ LOGICA BACKEND OK  
  🟡 GUI funziona solo per caricamento base; mancano funzioni avanzate/admin panel vero e proprio  
  🟡 La GUI è solo abbozzata (FXML incompleto, serve terminare la schermata e testarla a fondo)

---

## 4. Lettura a tempo, livelli, parametri difficoltà
- **Checklist:** Giorno 1 Callœ — logica timer, livelli, lettura a tempo: ❌ NON FATTO
- **Traccia:** Lettura documenti a tempo, parametri in base a difficoltà
- **Repo:**  
  - Nessun controller/timer/gestione livelli trovato  
  - FXML/Controller non mostrano schermata di lettura a tempo/documenti in base a livello
- **Stato:**  
  ❌ **DA FARE**: serve implementare la fase di lettura a tempo, scelta livello e parametri in GUI/controllore

---

## 5. Generazione domande & flusso partita
- **Checklist:** Giorno 3 Callœ — generazione domande: ✓ FATTO
- **Traccia:** Domande multiple-choice di vari tipi, dipendenti da analisi documenti
- **Repo:**  
  - `Domanda.java`, `GeneratoreDomande.java` — tutte le tipologie richieste sono coperte (frequenza, confronto, esclusione, parola-documento)
- **Stato:**  
  ✔️ LOGICA DI GENERAZIONE PRONTA  
  🟡 **Manca** la vera integrazione nella GUI:  
    - Non c’è una schermata in cui vengono poste domande all’utente
    - Non c’è flusso partita (lettura → domande → salvataggio)

---

## 6. Gestione risposte/calcolo punteggio/sessione
- **Checklist:** Giorno 4 Callœ — gestione risposte/calcolo punteggio: ❌ NON FATTO
- **Traccia:** Flusso partita, gestione risposte, timer sessione, assegnazione punteggio
- **Repo:**  
  - Manca controller/view per la gestione delle domande/risposte, timer e salvataggio sessione/punteggio a fine partita
- **Stato:**  
  ❌ **DA FARE**: va integrato in GUI e logica di flusso partita

---

## 7. Classifica, storico, statistiche post-gioco
- **Checklist:** Giorno 4 — leaderboard/statistiche: 🟡 Solo abbozzato
- **Traccia:** Leaderboard globale/personale, storico utente, statistiche post-gioco
- **Repo:**  
  - `Classifica.java` vuota; `PunteggioDAOSQL` supporta query classifica
  - FXML: pulsanti “Storico”, “Leaderboard” già previsti, ma logica/view non implementata
  - NESSUNA schermata storico/classifica/statistiche implementata
- **Stato:**  
  ❌ **DA FARE**: implementare logica e schermate classifica, storico, statistiche post-partita

---

## 8. GUI — FXML, CSS, icona, pannello admin
- **Checklist:** Giorno 1-3 John — GUI e styling: 🟡 Solo abbozzato
- **Traccia:** GUI JavaFX per tutte le fasi; styling CSS; pannello admin
- **Repo:**  
  - `MySimpleIRToolUI.fxml` — solo una bozza, mancano molte funzionalità e schermate (gioco, domande, punteggio, admin, ecc.)
  - CSS: presente (`MySimpleIRTool.css`), ma **VEROSIMILMENTE DA CAMBIARE** (come da nota tua)
  - Icona: presente (`icon.png`), **DA CAMBIARE** (come da nota tua)
- **Stato:**  
  🟡 **GUI solo base/parziale**, da completare con tutte le funzionalità richieste  
  🟡 Styling/icone non definitivi

---

## 9. Supporto multilingua, ripresa sessioni, extra
- **Checklist:** Non assegnato ancora
- **Traccia:** Supporto documenti in più lingue, resume sessione, extra feature
- **Repo:**  
  - FXML: ComboBox lingua già presente (ma non collegato a logica)
  - Logica multilingua/multisessione/ripresa sessione: **non presente**
- **Stato:**  
  🟡 **Solo parzialmente pianificato, DA IMPLEMENTARE**

---

## 10. Javadoc, relazione, UML, demo
- **Checklist:** Giorno 2+ — IL FRA: javadoc, relazione, diagrammi, deliverable: 🟡 Parzialmente
- **Traccia:** Javadoc, relazione, UML, presentazione, demo
- **Repo:**  
  - Javadoc solo su alcune classi
  - Nessun file relazione/UML/demo presente
- **Stato:**  
  🟡 **DA COMPLETARE**

---

# SINTESI E PRIORITÀ

- **Backend:**
  - CRUD utenti/sessioni/punteggi e generazione domande: ✔️ PRONTO
- **GUI:**
  - Caricamento documenti/stopwords: ✔️ BASE
  - Lettura a tempo, domande, risposte, punteggio, flusso gioco: ❌ DA FARE
  - Classifica, storico, statistiche: ❌ DA FARE
  - Multilingua, ripresa sessione: 🟡 Parzialmente pianificato, DA FARE
  - Styling, icone, FXML: 🟡 Da migliorare/concludere

- **Documentazione:**
  - Javadoc, relazione, UML: 🟡 Parziale, DA FARE

---

## COSA FARE SUBITO (roadmap finale)

1. **Chiudere la GUI completa**: flusso partita (lettura a tempo → domande → risposte → punteggio → storico/statistiche).
2. **Integrare gestione sessioni/punteggi/classifica/statistiche nella GUI.**
3. **Aggiungere/migliorare styling CSS, aggiorna icona.**
4. **Implementare supporto multilingua e ripresa sessione (se vuoi bonus).**
5. **Completare Javadoc, relazione finale, UML, demo e presentazione.**

---

**Se vuoi una tabella Excel/checklist operativa task per task, chiedi pure.**
**Se vuoi snippet di codice per timer, schermata domande, leaderboard… chiedi!**