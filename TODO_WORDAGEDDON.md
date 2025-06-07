# Confronto definitivo Checklist ↔ Traccia ↔ File di Repo (giugno 2025)

## 1. Login / Registrazione / Multiutente
- **Checklist:** Giorno 2 “Il Dom” — login/registrazione: ✓ FATTO
- **Traccia:** Multiutente, login, salvataggio sessioni/punteggi
- **Repo:**  
  - `Utente.java`, `UtenteDAO`, `UtenteDAOSQL`, `UtenteService`, `PasswordUtils`
  - Test automatici su DAO utenti
- **Stato:**  
  ✔️ COMPLETO

---

## 2. Database / Persistenza Sessioni & Punteggi
- **Checklist:** Giorno 1 e 3 — setup database, salvataggio sessioni, storico punteggi: ✓ FATTO
- **Traccia:** Ogni sessione produce un punteggio, storicizzato
- **Repo:**  
  - `DatabaseManager.java`
  - `Sessione.java`, `Punteggio.java`, DAO e test relativi
- **Stato:**  
  ✔️ COMPLETO lato backend/test
  🟡 Integrazione nella GUI: **DA VERIFICARE CHE SALVI SEMPRE**

---

## 3. Caricamento documenti, stopwords, analisi testi
- **Checklist:** Giorno 2 John — GUI caricamento documenti/stopwords, area admin: ✓ SOLO CARICAMENTO BASE
- **Traccia:** Caricamento file e stopwords, analisi testi
- **Repo:**  
  - `WordageddonController.java` — logica per scegliere cartella documenti e file stopwords, usa `VocabularyService`
  - `Document.java`, `VocabularyService.java` — parsing file, filtraggio stopwords, conteggio parole, vocabolario
- **Stato:**  
  ✔️ LOGICA BACKEND OK  
  🟡 GUI: caricamento funzionante (ma **admin panel NON ESISTE**), manca gestione utenti/caricamento avanzato

---

## 4. Lettura a tempo, livelli, parametri difficoltà
- **Checklist:** Giorno 1 Callœ — logica timer, livelli, lettura a tempo: ✓ FATTO
- **Traccia:** Lettura documenti a tempo, parametri in base a difficoltà
- **Repo:**  
  - `LetturaTestoController.java`
- **Stato:**  
  ✔️ IMPLEMENTATO E FUNZIONANTE

---

## 5. Generazione domande & flusso partita
- **Checklist:** Giorno 3 Callœ — generazione domande: ✓ FATTO
- **Traccia:** Domande multiple-choice di vari tipi, dipendenti da analisi documenti
- **Repo:**  
  - `Domanda.java`, `GeneratoreDomande.java`
- **Stato:**  
  ✔️ LOGICA DI GENERAZIONE PRONTA  
  ✔️ Flusso partita presente: lettura → domande → risposte → punteggio

---

## 6. Gestione risposte/calcolo punteggio/sessione
- **Checklist:** Giorno 4 Callœ — gestione risposte/calcolo punteggio: ✓ FATTO
- **Traccia:** Flusso partita, gestione risposte, timer sessione, assegnazione punteggio
- **Repo:**  
  - `QuizController.java`
- **Stato:**  
  ✔️ COMPLETO E FUNZIONANTE

---

## 7. Classifica, storico, statistiche post-gioco
- **Checklist:** Giorno 4 — leaderboard/statistiche: 🟡 Solo abbozzato/test parziale
- **Traccia:** Leaderboard globale/personale, storico utente, statistiche post-gioco
- **Repo:**  
  - `Classifica.java`, `PunteggioDAOSQL`
  - FXML: pulsanti “Storico”, “Leaderboard” previsti, ma logica/view da rifinire/testare
- **Stato:**  
  🟡 **DA COMPLETARE** (alcune schermate/logica non ancora definitive)

---

## 8. GUI — FXML, CSS, icona, pannello admin
- **Checklist:** Giorno 1-3 John — GUI e styling: 🟡 Solo base/parziale
- **Traccia:** GUI JavaFX per tutte le fasi; styling CSS; pannello admin
- **Repo:**  
  - Tutte le schermate FXML principali ci sono, ma serve revisione/finitura
  - CSS presente ma da rifinire
  - Icona da aggiornare
- **Stato:**  
  🟡 **GUI OK per il flusso base**, da rifinire per dettagli/styling/funzionalità avanzate  
  ❌ **PANNELLO ADMIN NON ESISTE**

---

## 9. Supporto multilingua, ripresa sessioni, extra
- **Checklist:** Task Extra
- **Traccia:** Supporto documenti in più lingue, resume sessione, extra feature
- **Repo:**  
  - FXML: ComboBox lingua presente **ma NON collegato**
  - Logica multilingua/ripresa sessione **assente**
- **Stato:**  
  ❌ **DA FARE**

---

## 10. Javadoc, relazione, UML, demo
- **Checklist:** Giorno 2+ — IL FRA: javadoc, relazione, diagrammi, deliverable: 🟡 Parzialmente
- **Traccia:** Javadoc, relazione, UML, presentazione, demo
- **Repo:**  
  - Javadoc quasi assente o solo su poche classi
  - Relazione/UML/demo **assenti**
- **Stato:**  
  ❌ **DA FARE**

---

# SINTESI E PRIORITÀ

- **Backend:**
  - CRUD utenti/sessioni/punteggi e generazione domande: ✔️ PRONTO
- **GUI:**
  - Caricamento documenti/stopwords: ✔️
  - Lettura a tempo, domande, risposte, punteggio, flusso gioco: ✔️
  - Classifica, storico, statistiche: 🟡 DA COMPLETARE
  - Multilingua, ripresa sessione: ❌ DA FARE
  - Styling, icone, FXML: 🟡 Da rifinire
  - Pannello admin: ❌ NON ESISTE

- **Documentazione:**
  - Javadoc, relazione, UML: ❌ DA FARE

---

## COSA FARE SUBITO (roadmap finale)

1. **Completare GUI (storico, leaderboard, admin, styling)**
2. **Aggiungere supporto multilingua e ripresa sessione**
3. **Completare Javadoc, relazione, UML, demo**
4. **Testing generale, bugfixing, backup, simulazione consegna**
5. **Caricamento piattaforma/consegna**
