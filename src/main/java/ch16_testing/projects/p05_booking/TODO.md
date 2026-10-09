# Projet 5 — Les réservations de salles (doublures de test, horloge injectée)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **l'injection de dépendances** : un objet **reçoit** ce dont il a besoin (par son constructeur), il ne le crée pas ;
- **des interfaces comme points de branchement** : en production une base de données, dans les tests un faux ;
- **les doublures de test** (*test doubles*), écrites à la main :
  - le **faux** (*fake*) : une version simple mais qui marche (un dépôt en mémoire) ;
  - l'**espion** (*spy*) : il note chaque appel, pour qu'on le vérifie après ;
  - le **bouchon** (*stub*) : il rend une réponse préparée d'avance, ou il échoue exprès ;
- **le temps dans les tests** : `java.time.Clock`, `Clock.fixed`, `LocalDateTime.now(clock)` ;
- **tester les chemins d'échec** : que se passe-t-il quand une dépendance casse ?

Côté algorithmes : le chevauchement de deux intervalles `[début, fin[`, des règles de délai.

**Ce que TU crées :** dans `ch16_testing.projects.p05_booking` :
- **`Booking`**, **`BookingRepository`**, **`Notifier`** et **`BookingService`** (leurs signatures sont imposées) ;
- **`BookingServiceTest`**, tes tests, **avec tes doublures dedans** : chaque doublure est une classe `static` **imbriquée** dans `BookingServiceTest` (pas un fichier à part : `Check` ne recopie avec tes tests que les fichiers `…Test.java`).

**Règle du crescendo :** chapitres 1 à 15, plus JUnit. Trois interdits, vérifiés par `Check` :
- dans ton code, **jamais** l'heure réelle : ni `LocalDateTime.now()`, ni `LocalDate.now()`, ni `Instant.now()`, ni `System.currentTimeMillis`. « Maintenant » vient **toujours** de l'horloge reçue ;
- dans tes tests, pas de Mockito (`org.mockito`) : les doublures s'écrivent à la main ici, Mockito viendra au projet 6 ;
- pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une machine à café connectée.

> **🧰 Tes outils pour ce projet**
>
> - **Implémenter une interface vite :** écris `static final class FakeStock implements Stock {}`, mets le curseur sur le nom souligné en rouge, **Alt+Entrée** → *Implement methods*. IntelliJ écrit toutes les méthodes vides.
> - **Lancer tes tests :** **Ctrl+Maj+F10** dans `BookingServiceTest`. **Lancer `Check` :** flèche verte à côté de `Check.main`.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle et les points de branchement

**📖 La leçon : recevoir au lieu de créer.** Une machine à café connectée doit retirer du stock et envoyer un SMS quand le café est prêt. Si elle fabrique elle-même sa base de données et son service SMS, impossible de la tester sans vraie base et sans vrais SMS. On lui **donne** ses dépendances, sous forme d'**interfaces** (chapitre 7) :

```java
interface Stock { boolean retire(String produit); }
interface Sms { void envoie(String numero, String texte); }

final class MachineACafe {
    private final Stock stock;
    private final Sms sms;

    MachineACafe(Stock stock, Sms sms) {      // elle REÇOIT ses dépendances : c'est l'injection de dépendances
        this.stock = stock;
        this.sms = sms;
    }
}
```

En production, on lui passe la vraie base et le vrai service SMS. Dans les tests, des **doublures** : des petites classes qui implémentent les mêmes interfaces.

**👉 À toi :**
- **`public record Booking(int id, String room, String user, LocalDateTime start, LocalDateTime end)`** : une réservation, de `start` (inclus) à `end` (exclu). Ajoute-lui une méthode **`public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd)`** : vrai si les deux intervalles se chevauchent.
- **`public interface BookingRepository`** : `int nextId()`, `void save(Booking booking)`, `Optional<Booking> find(int id)`, `List<Booking> forRoom(String room)`, `void delete(int id)`.
- **`public interface Notifier`** : `void send(String to, String message)`.

**❓ Question :** deux intervalles `[a, b[` et `[c, d[` se chevauchent quand `a < d` **et** `c < b`. Vérifie la formule sur papier avec 10 h–11 h et 11 h–12 h, puis avec 10 h–11 h et 10 h 45–11 h 15.

### ☐ Étape 2 — L'horloge, et les règles d'une réservation

**📖 La leçon : figer le temps.** Un test qui dépend de l'heure réelle passe aujourd'hui et casse demain. `java.time.Clock` est une **horloge qu'on peut injecter** :
- en production : `Clock.systemDefaultZone()`, la vraie heure ;
- dans un test : `Clock.fixed(instant, zone)`, une horloge **arrêtée** à l'instant choisi ;
- dans le code : `LocalDateTime.now(clock)` au lieu de `LocalDateTime.now()`.

```java
final class MachineACafe {
    private final Clock clock;
    MachineACafe(Clock clock) { this.clock = clock; }

    boolean estOuverte() {
        int heure = LocalTime.now(clock).getHour();      // l'heure de l'horloge REÇUE
        return heure >= 7 && heure < 19;
    }
}

// dans le test :
Clock septHeures = Clock.fixed(Instant.parse("2026-01-05T07:00:00Z"), ZoneOffset.UTC);
assertTrue(new MachineACafe(septHeures).estOuverte());   // vrai aujourd'hui, demain, dans dix ans
```

`Instant.parse` lit une date au format ISO ; le `Z` final veut dire UTC. `Clock.offset(horloge, Duration.ofHours(2))` rend une horloge **décalée** : pratique pour simuler « deux heures plus tard ».

**👉 À toi :** **`public final class BookingService`**, avec le constructeur **`public BookingService(BookingRepository repo, Notifier notifier, Clock clock)`**, et **`public Booking book(String room, String user, LocalDateTime start, int minutes)`**. Les contrôles, **dans cet ordre** (avec `LocalDateTime now = LocalDateTime.now(clock)`) :
1. la durée va de 15 à 240 minutes, **par quarts d'heure** (multiple de 15), sinon `IllegalArgumentException("duree invalide : " + minutes)` ;
2. le début est **strictement après** maintenant, sinon `IllegalArgumentException("debut dans le passe : " + start)` ;
3. le début n'est pas plus de **30 jours** après maintenant (`start.isAfter(now.plusDays(30))` est refusé), sinon `IllegalArgumentException("trop tot pour reserver : " + start)` ;
4. aucune réservation **de la même salle** ne chevauche `[start, start + minutes[`, sinon `IllegalStateException("salle occupee : " + room)`.

Puis : crée la réservation avec l'id `repo.nextId()`, **enregistre-la** (`repo.save`), **puis** envoie à l'utilisateur `"Reservation " + id + " : " + room + " le " + start`, et rends-la.

Dans `BookingServiceTest` : une constante `Clock` figée au **lundi 2 mars 2026, 9 h 00 UTC** (`Instant.parse("2026-03-02T09:00:00Z")`, `ZoneOffset.UTC`). Les tests des règles 1 à 3 : les durées 0, 10, 14, 25, 255 refusées ; 15, 45, 240 acceptées ; un début à 9 h 00 pile refusé, à 9 h 01 accepté ; un début à maintenant + 30 jours accepté, une minute de plus refusé. (Pour l'instant, une doublure vide suffit pour le dépôt ; tu la rempliras à l'étape 3.)

**❓ Question :** pourquoi la règle 2 s'écrit-elle `!start.isAfter(now)` et pas `start.isBefore(now)` ?

### ☐ Étape 3 — Le faux et l'espion

**📖 La leçon : les familles de doublures.** Pour la machine à café :

```java
/** Un FAUX : il marche vraiment, mais en mémoire. */
static final class StockEnMemoire implements Stock {
    final Map<String, Integer> quantites = new HashMap<>(Map.of("arabica", 2));
    @Override public boolean retire(String produit) {
        int q = quantites.getOrDefault(produit, 0);
        if (q == 0) return false;
        quantites.put(produit, q - 1);
        return true;
    }
}

/** Un ESPION : il ne fait rien, mais il note tout. */
static final class SmsEspion implements Sms {
    final List<String> envoyes = new ArrayList<>();
    @Override public void envoie(String numero, String texte) { envoyes.add(numero + " <- " + texte); }
}

@Test
void unCafePretEnvoieUnSms() {
    SmsEspion sms = new SmsEspion();
    new MachineACafe(new StockEnMemoire(), sms).sers("arabica", "0601");
    assertEquals(List.of("0601 <- votre arabica est pret"), sms.envoyes);    // on vérifie ce que l'espion a vu
}
```

Le vocabulaire complet (Gerard Meszaros) : le **mannequin** (*dummy*, passé mais jamais utilisé), le **bouchon** (*stub*, réponses préparées), l'**espion** (*spy*, il enregistre), le **faux** (*fake*, une implémentation simplifiée qui marche) et le **simulacre** (*mock*, il vérifie lui-même qu'on l'a bien appelé ; projet 6).

**👉 À toi :** dans `BookingServiceTest` :
- un **faux** `InMemoryRepository implements BookingRepository` : une `Map` des ids vers les réservations, et un compteur pour `nextId()` (1, 2, 3…) ;
- un **espion** `RecordingNotifier implements Notifier` : une liste publique des messages, au format `to + " <- " + message` ;
- un `@BeforeEach` qui crée un faux, un espion, et le service avec l'horloge figée ;
- les tests :
  - une réservation valide : la réservation rendue (compare avec un `new Booking(…)` complet), ce que contient le dépôt, et ce qu'a reçu l'espion (le message exact) ;
  - deux réservations : la 2e a l'id 2 ;
  - le chevauchement : refusé dans la même salle ; accepté **bout à bout** (avant et après) ; accepté dans une autre salle.

**❓ Question :** pourquoi un faux dépôt plutôt qu'une vraie base H2 en mémoire (chapitre 15) ?

### ☐ Étape 4 — Le bouchon qui échoue

**📖 La leçon : tester ce qui casse.** Les pannes arrivent : disque plein, réseau coupé. Un bon code reste **cohérent** quand une dépendance échoue. Pour le tester, un **bouchon** qui échoue exprès :

```java
static final class StockEnPanne implements Stock {
    @Override public boolean retire(String produit) { throw new IllegalStateException("base injoignable"); }
}

@Test
void pasDeSmsSiLeStockEstEnPanne() {
    SmsEspion sms = new SmsEspion();
    MachineACafe machine = new MachineACafe(new StockEnPanne(), sms);
    assertThrows(IllegalStateException.class, () -> machine.sers("arabica", "0601"));
    assertEquals(List.of(), sms.envoyes);           // personne n'a reçu « votre café est prêt »
}
```

**👉 À toi :** un bouchon `BrokenRepository` dont `save` lance `IllegalStateException("disque plein")` (le plus simple : il **hérite** de ton faux et redéfinit `save`, chapitre 6 ; ton faux ne doit alors pas être `final`). Le test : `book` lance l'exception, **et** l'espion n'a rien reçu.

**❓ Question :** quel bug, dans `book`, ce test est-il le seul à pouvoir attraper ?

### ☐ Étape 5 — Annuler

**👉 À toi :** **`public void cancel(int id, String user)`**, contrôles dans cet ordre :
1. réservation introuvable : `NoSuchElementException("reservation inconnue : " + id)` (utilise `orElseThrow`, chapitre 10) ;
2. un autre utilisateur : `IllegalStateException("pas ta reservation : " + id)` ;
3. moins de **2 heures** avant le début (`now.plusHours(2).isAfter(start)`) : `IllegalStateException("trop tard pour annuler : " + id)`. Exactement 2 h avant, c'est encore permis.

Puis : supprime-la du dépôt, et envoie `"Annulation " + id` à l'utilisateur.

**Tes tests,** dans un groupe `@Nested` dont le `@BeforeEach` réserve la salle de 11 h 00 à 12 h 00 (pour l'utilisateur `ana`) puis vide l'espion :
- `ana` annule à 9 h 00 (2 h pile avant) : le dépôt ne la contient plus, l'espion a reçu `ana <- Annulation 1` ;
- une réservation qui commence à 10 h 59 (dans une **autre** salle, pour ne pas chevaucher) ne peut plus être annulée, et reste dans le dépôt ;
- `bob` ne peut pas annuler la réservation d'`ana`, et personne ne reçoit rien ;
- une réservation inconnue.

**🧪 Expérience :** au lieu de réserver à 10 h 59, crée un **deuxième service** avec `Clock.offset(tonHorloge, Duration.ofMinutes(1))` (9 h 01) et essaie d'annuler la réservation de 11 h 00. Que se passe-t-il ?

### ☐ Étape 6 — Les réservations du jour

**👉 À toi :** **`public List<Booking> todayFor(String room)`** : les réservations de la salle qui commencent **aujourd'hui** (`LocalDate.now(clock)`), triées par heure de début. Ton test : quatre réservations (deux aujourd'hui dans la salle, dans le désordre ; une demain dans la salle ; une aujourd'hui dans une autre salle) ; seules les deux bonnes, dans l'ordre.

### ☐ Étape 7 — Les mutants

**👉 À toi :** lance `Check` et tue les **15** mutants. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

### Expériences (hors sortie attendue)

1. Dans tes tests, remplace l'horloge figée par `Clock.systemUTC()` (la vraie heure). Lance-les : combien passent encore, et pourquoi ?
2. Dans ton code, remplace `LocalDateTime.now(clock)` par `LocalDateTime.now()` dans `book`. Lance `Check` : que disent tes tests, et que dit la partie API ? Remets le code.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Booking(int id, String room, String user, LocalDateTime start, LocalDateTime end)`, `interface BookingRepository`, `interface Notifier`, `final class BookingService`, le constructeur exact, `LocalDateTime.now(clock)`, `LocalDate.now(clock)`, `orElseThrow(` ; jamais `LocalDateTime.now()`, `LocalDate.now()`, `Instant.now()`, `System.currentTimeMillis`.
- **Tes tests :** au moins **18** tests, `Clock.fixed(`, `Instant.parse(`, `ZoneOffset.UTC`, `implements BookingRepository`, `implements Notifier`, une doublure `static … class … implements`, `@BeforeEach`, `assertThrows(`, `assertAll(` ; ni `org.mockito`, ni `System.out`, ni `Thread.sleep`.
- **Les 15 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p05_booking ===
[PASS] tes tests sur TON code : 19 tests, 19 reussis
[PASS] tes tests sur le code de REFERENCE : 19 tests, 19 reussis
[PASS] les tests de REFERENCE sur TON code : 19 tests, 19 reussis
   mutant 1 : tue (par invalidDurationsAreRejected [duree 0 refusee])
   …
   mutant 15 : tue (par ownerCanCancelTwoHoursBefore)
[PASS] mutants : 15/15 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
