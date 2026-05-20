# - eCommerce Web Server — Spring Boot

Backend REST API per una piattaforma di e-commerce, sviluppata con **Spring Boot 3** e **MySQL**. Espone endpoint per la gestione di prodotti, utenti e acquisti seguendo un'architettura a livelli (Controller → Service → Repository → Entity).

---

## - Tecnologie

| Tecnologia | Versione |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.1 |
| Spring Data JPA | — |
| MySQL Connector | — |
| Lombok | 1.18.32 |
| javax.validation | 2.0.1 |
| Maven | — |

---

## 📁 Struttura del progetto

```
src/main/java/com/example/demo/
├── controllers/          # Endpoint REST
│   ├── ControllerProdotto.java
│   ├── ControllerAcquisto.java
│   └── ControllerUtente.java
├── entities/             # Modello del dominio (JPA)
│   ├── Prodotto.java
│   ├── Utente.java
│   ├── Acquisto.java
│   └── ProdottoInAcquisto.java
├── services/             # Logica di business
├── repositories/         # Accesso ai dati (Spring Data)
├── configurations/       # Configurazioni Spring
├── support/              # Eccezioni custom e classi di supporto
└── DemoApplication.java  # Entry point
```

---

## - Endpoint API

### Prodotti — `/prodotti`

| Metodo | Endpoint | Descrizione |
|---|---|---|
| `POST` | `/prodotti` | Aggiunge un nuovo prodotto (richiede barCode univoco) |
| `DELETE` | `/prodotti/rimuovi?barCode={bc}` | Rimuove un prodotto per barCode |
| `GET` | `/prodotti/listaProd` | Lista completa dei prodotti |
| `GET` | `/prodotti/listaProdPaginata` | Lista paginata (params: `numeroPagina`, `dimPagina`, `ordinaPer`) |
| `GET` | `/prodotti/ricercaPerNome?name={nome}` | Ricerca per nome |
| `GET` | `/prodotti/ricercaPerBarCode/{barCode}` | Ricerca per barCode |
| `GET` | `/prodotti/prodottiPerPrezzoMax?prezzomax={val}` | Prodotti con prezzo ≤ valore |
| `GET` | `/prodotti/prodottiPerPrezzoMin?prezzomin={val}` | Prodotti con prezzo ≥ valore |
| `GET` | `/prodotti/prodottiPerIntervalloPrezzo` | Prodotti in fascia di prezzo |
| `GET` | `/prodotti/ricercaAvanzataProdotti` | Ricerca avanzata (nome, descrizione, prezzi, tipo) |

### Acquisti — `/acquisti`

| Metodo | Endpoint | Descrizione |
|---|---|---|
| `POST` | `/acquisti?idUtente={id}` | Crea un acquisto per l'utente con lista prodotti |
| `GET` | `/acquisti` | Tutti gli acquisti |
| `GET` | `/acquisti/{user}` | Acquisti di un utente |
| `GET` | `/acquisti/{user}/{startDate}/{endDate}` | Acquisti in un periodo (`dd-MM-yyyy`) |
| `GET` | `/acquisti/{user}/{Date}` | Acquisti in una data specifica |

### Utenti — `/utenti`

Gestione degli utenti registrati sulla piattaforma.

---

## - Configurazione

Il file `src/main/resources/application.yaml` contiene la configurazione del datasource:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dbserver
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: update
```

> ⚠️ Prima di avviare il server, assicurarsi che MySQL sia in esecuzione e che il database `dbserver` esista.

---

## - Avvio del progetto

**Prerequisiti:** Java 17+, Maven, MySQL in esecuzione.

```bash
# Clona la repository
git clone https://github.com/Mattdib/eCommerce_WebServerSpring.git
cd eCommerce_WebServerSpring

# Avvia con Maven Wrapper
./mvnw spring-boot:run
```

Il server sarà disponibile su `http://localhost:8080`.

---

## - Autore

**Mattia** — [@Mattdib](https://github.com/Mattdib)
