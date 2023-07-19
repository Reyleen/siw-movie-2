
SIWMovie
Per il progetto ho utilizzato la libreria di Swiper: [DOCS](https://swiperjs.com/swiper-api)
---
Casi d'uso:
---
UC1: Inserimento Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie l'operazione "Inserisci film"
  6. L'amministratore inserisce Titolo, Anno ed eventualmente un'immagine
  7. L'amministratore conferma l'inserimento
  8. Il sistema mostra la pagina con i dati del film appena inserito

UC2: Modifica Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie il film da modificare
  6. Il sistema mostra le operazioni che possono esser effettuate su quel film
  7. L'amministratore sceglie l'operazione "Modifica"
  8. L'amministratore inserisce Titolo, Anno ed eventualmente un'immagine
  9. L'amministratore conferma l'inserimento
  10. Il sistema mostra la pagina con i dati del film appena inserito

UC3: Rimozione Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie il film da modificare
  6. Il sistema mostra le operazioni che possono esser effettuate su quel film
  7. L'amministratore sceglie l'operazione "Elimina"
  8. Il sistema mostra la pagina con i film che si possono modificare

UC4: Modifica Regista per Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie il film da modificare
  6. Il sistema mostra le operazioni che possono esser effettuate su quel film
  7. L'amministratore sceglie l'operazione "Aggiungi Regista"
  7.a Se c'è già un regista per il film, l'amministratore sceglie l'operazione "Modifica Regista"
  8. Il sistema mostra la lista di artisti che possono essere scelti come registi
  9. L'amministratore sceglie l'artista
  10. Il sistema registra la scelta e mostra i dati del film appena modificato

UC5: Modifica Attori per Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie il film da modificare
  6. Il sistema mostra le operazioni che possono esser effettuate su quel film
  7. L'amministratore sceglie l'operazione "Modifica Attori"
  8. Il sistema mostra la lista di artisti che possono essere scelti come attori
  9. L'amministratore sceglie l'artista
  ripete l'operazione 9 finchè l'amministratore è soddisfatto
  10. L'amministratore conferma
  11. Il sistema registra la scelta e mostra i dati del film appena modificato

UC6: Aggiunta immagini per Film - Attore primario: Amministratore
  1. L'amministratore fa l'autenticazione nel sistema
  2. Il sistema mostra la pagina home dell'amministratore
  3. L'amministratore sceglie l'operazione di "Modifica Film"
  4. Il sistema mostra un elenco di film che possono essere modificati e le operazioni che possono essere effettuate
  5. L'amministratore sceglie il film da modificare
  6. Il sistema mostra le operazioni che possono esser effettuate su quel film
  7. L'amministratore inserisce l'immagine
  8. L'amministratore conferma
  9. Il sistema registra la scelta e mostra i dati del film appena modificato

