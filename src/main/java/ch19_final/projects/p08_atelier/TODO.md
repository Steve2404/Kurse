# Projet 8 — Capstone : l'atelier en ligne, l'application complète

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (tout le parcours) :**
- **assembler** une vraie application à partir de briques écrites et testées séparément ;
- l'architecture **hexagonale** en grand : un cœur métier pur, des ports, des adaptateurs, des filtres, une racine de composition (chapitre 18) ;
- faire **évoluer** du code existant sans le casser : ses tests restent ton filet ;
- une **règle métier nouvelle** (le tableau Kanban et sa limite « En cours ») et la course « vérifier puis agir » ;
- des **décorateurs** autour de l'API : le limiteur de débit et les mesures ;
- des **tests de bout en bout** : vraie base, vrai serveur, vrai client ;
- **exploiter** l'application : la lancer, la mesurer, trouver ce qui la ralentit, la corriger.

**Ce qui est FOURNI :** `Data.java` contient `SEED`, les 7 tâches de l'ancien système, à reprendre au premier démarrage. Tu ne modifies pas ce fichier.

**Ce que TU réutilises (copie-les dans ce paquet, comme au projet 3) :**

| Projet | Fichiers |
|---|---|
| 1 | `Json`, `JsonNull`, `JsonBool`, `JsonNumber`, `JsonString`, `JsonArray`, `JsonObject`, `JsonException`, `JsonParser`, `JsonWriter` |
| 2 | `Task`, `NewTask`, `Migration`, `Migrations`, `TaskSchema`, `SqlWork`, `Transactions`, `StoreException`, `ConflictException`, `JdbcTaskRepository` |
| 3 | `Request`, `Response`, `Handler`, `Router`, `ApiException`, `WebServer`, `TaskJson` |
| 5 | `TokenBucket`, `RateLimiter`, `LatencyRecorder`, `ManualClock` |

Ne recopie **pas** `TaskApi`, `InMemoryTaskRepository`, le `TaskRepository` ni le `ConflictException` du projet 3 : le capstone a les siens (le `ConflictException` du projet 2, sous-classe de `StoreException`, convient au `Router`).

**Ce que TU crées :** `RequestHandler`, `TaskRepository` (le nouveau port), `Board`, `BoardService`, `AtelierApi`, `ApiMetrics`, `MetricsFilter`, `RateLimitFilter`, `AtelierApp`, et tes tests (par exemple `BoardTest`, `BoardServiceTest`, `FiltersTest`, `EndToEndTest`).

**Règle du crescendo :** tout ce que tu as appris. Aucune méthode de plus de **18 lignes**. Le cœur ne connaît ni la base ni HTTP : `Board.java` ne contient ni `java.sql` ni `Request` ; `BoardService.java` ni `Jdbc` ni `Response` ; `AtelierApi.java` ni `Jdbc` ni `Response.error(`. Pas de `printStackTrace`. Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni le port `8080`.

---

## Le plan

```
                 ┌──────────────────────────── AtelierApp.create (la racine) ───────────────────────────┐
  client HTTP →  │ WebServer → MetricsFilter → RateLimitFilter → Router (AtelierApi) → BoardService     │
                 │ (adaptateur)  (décorateur)    (décorateur)     (routes)              │  (cœur)        │
                 │                                                                     ├→ Board (règles) │
                 │                                                                     └→ TaskRepository │
                 │                                                                        (port)   ↑     │
                 │                                                      JdbcTaskRepository (adaptateur)  │
                 │                                                      Transactions, Migrations → H2    │
                 └───────────────────────────────────────────────────────────────────────────────────────┘
```

Les flèches vont toujours **vers le cœur** : `Board` ne dépend de rien, `BoardService` dépend du port, et ce sont les adaptateurs (`JdbcTaskRepository`, `WebServer`) qui dépendent du métier.

---

## Tableau de bord

### ☐ Étape 1 — Faire évoluer les briques

**📖 La leçon : changer du code qui marche.** Tes briques ont des tests (dans leurs projets). Chaque changement ici est petit, et la compilation te guide : change une signature, puis suis les erreurs rouges d'IntelliJ jusqu'à ce qu'il n'y en ait plus.

**👉 À toi :**
- `@FunctionalInterface public interface RequestHandler` : `Response handle(Request request)` ; `Router` l'**implémente** ;
- `Request` gagne un dernier composant `String client` : `null` ou blanc devient `"anonyme"` (une constante `ANONYMOUS`) ; `Request.of` et `Request.json` mettent `null` ; ajoute `withClient(String)` et `withQuery(Map)`, qui rendent une copie ;
- `WebServer.start(int port, RequestHandler handler)` (au lieu d'un `Router`) ; la `Request` reçoit l'en-tête `X-Client` ;
- `public interface TaskRepository` (le **port** du capstone) : `create`, `find`, `page`, `update`, `move(long id, String toColumn, int expectedVersion)`, `history(long id)`, `delete`, `countByColumn()` ; `JdbcTaskRepository` l'implémente (ajoute `implements` et des `@Override`) ;
- dans `TaskJson` : `public record Move(String to, int version)` et `static Move move(String body)` (le corps `{"to":"En cours","version":0}`).

**❓ Question :** pourquoi le nouveau port s'appelle-t-il comme celui du projet 3, mais n'a-t-il pas les mêmes méthodes ? Qui décide de ce que contient un port ?

### ☐ Étape 2 — Le cœur : les règles du tableau

**📖 La leçon : le cœur est pur.** Les règles du métier ne dépendent ni de la base, ni du réseau, ni de l'heure. Elles se testent en quelques microsecondes, des centaines de fois, et elles ne changeront pas si demain l'atelier passe à une autre base ou à une application mobile.

**👉 À toi :** `public final class Board`, avec `public static final List<String> COLUMNS = List.of("A faire", "En cours", "Fini")`, constructeur `Board(int wipLimit)` (< 1 : `IllegalArgumentException("limite En cours invalide : 0")`) :
- `void checkNew(NewTask task)` : une nouvelle tâche commence dans « A faire », sinon `IllegalArgumentException("une nouvelle tache commence dans A faire, pas dans Fini")` ;
- `void checkMove(Task task, String to, Map<String, Integer> counts)` (`counts` : les tâches par colonne **avant** le déplacement) :
  - colonne inconnue (la casse compte) → `"colonne inconnue : Archive"` ;
  - même colonne → `"la tache 7 est deja dans En cours"` ;
  - plus d'une colonne d'écart, en avant **ou en arrière** → `"deplacement impossible : A faire -> Fini (une colonne a la fois)"` ;
  - vers « En cours » quand il y a déjà `wipLimit` tâches → `new ApiException(409, "limite atteinte : En cours (2 taches au plus)")`. La limite ne concerne **que** « En cours ».

**🧪 Les tests (`BoardTest`) :** les quatre déplacements permis, cinq interdits (`@ParameterizedTest`), la limite (1 passe, 2 non, et le code 409), la limite qui ne gêne pas les autres colonnes, la création, le constructeur.

### ☐ Étape 3 — Le service, et la course « vérifier puis agir »

**📖 La leçon : vérifier puis agir n'est pas un seul geste.** `move` lit le nombre de tâches « En cours » (1, la limite est 2 : c'est bon), **puis** déplace. Deux requêtes simultanées peuvent lire **toutes les deux** « 1 » avant que l'une ou l'autre ait déplacé sa tâche : les deux passent, et la limite est dépassée. C'est la course *check-then-act* (chapitre 13). Ici, une seule instance du programme : `synchronized` sur `move` fait des deux étapes un seul geste. Avec plusieurs serveurs, il faudrait un verrou **dans la base** (`SELECT … FOR UPDATE`, ou une contrainte).

**👉 À toi :** `public final class BoardService`, constructeur `(TaskRepository tasks, Board board)` :
- `create` (vérifie avec le `Board`, puis le dépôt) ; `get(long id)` (inconnue → `NoSuchElementException("tache 9 introuvable")`) ; `page` ;
- `update(Task task)` : le titre et les points seulement ; une colonne différente de l'actuelle → `IllegalArgumentException("la colonne se change avec /move, pas avec PUT")` ;
- `synchronized Task move(long id, String to, int expectedVersion)` : les règles, avec les comptes **de la base**, puis le dépôt ;
- `history(long id)` (404 si la tâche n'existe pas), `delete(long id)` (404 si elle n'existait pas) ;
- `Map<String, Integer> board()` : **toutes** les colonnes, dans l'ordre du tableau, même vides (`{A faire=0, En cours=0, Fini=0}`).

**🧪 Les tests (`BoardServiceTest`, avec une base H2 neuve et une `ManualClock`) :** le tableau vide puis rempli ; la création refusée ne crée rien ; deux déplacements et leur historique ; la limite avec les vrais comptes ; le `PUT` qui change de colonne ; les tâches inconnues ; et **la course** : 6 tâches, 6 fils libérés ensemble par un `CountDownLatch`, qui les déplacent toutes vers « En cours » : exactement **2** réussissent.

**🧪 Expérience :** retire `synchronized` de `move` et lance le test de la course plusieurs fois. Remets-le.

### ☐ Étape 4 — Les routes

**👉 À toi :** `public final class AtelierApi` (constructeur privé) et `public static Router routes(BoardService service, ApiMetrics metrics, Consumer<Throwable> onInternalError)` (crée `ApiMetrics` à l'étape 5, ou commence avec une version vide) :

| Route | Réponse |
|---|---|
| `GET /health` | `{"status":"ok"}` |
| `GET /metrics` | les mesures (étape 5) |
| `GET /board` | `{"A faire":3,"En cours":2,"Fini":2}` |
| `GET /tasks`, `POST /tasks`, `GET`, `PUT`, `DELETE /tasks/{id}` | comme au projet 3, par le `BoardService` |
| `POST /tasks/{id}/move` | corps `{"to":"En cours","version":0}` ; 200 et la tâche déplacée |
| `GET /tasks/{id}/history` | un tableau de chaînes |

Comme au projet 3 : les corps exigent `application/json` (415), et les handlers **lancent** ; le `Router` traduit.

### ☐ Étape 5 — Les filtres : limiter et mesurer

**📖 La leçon : des décorateurs autour de l'API.** Le limiteur et les mesures ne sont pas des routes : ils concernent **toutes** les requêtes. Chacun est un `RequestHandler` qui reçoit le **suivant**, fait son travail, et lui passe la main (le décorateur du chapitre 18). L'**ordre** compte : `MetricsFilter(RateLimitFilter(Router))` mesure aussi les requêtes refusées par le limiteur ; dans l'autre sens, elles seraient invisibles.

**👉 À toi :**
- `public final class RateLimitFilter implements RequestHandler`, constructeur `(RequestHandler next, RateLimiter limiter)` : un seau par `request.client()` ; refusé → `429`, corps `{"error":"trop de requetes : reessayer dans 1 s"}` et en-tête `Retry-After` (les secondes d'attente, arrondies **au-dessus**, au moins 1) ; sinon, `next` ;
- `public final class ApiMetrics`, constructeur `(LatencyRecorder latency)` : `void record(int status, Duration duration)` (accès paquet) compte les requêtes, les erreurs du client (4xx sauf 429), du serveur (5xx) et les refus (429) ; `JsonObject toJson()` → `{"requests":7,"clientErrors":2,"serverErrors":2,"throttled":1,"p50":0,"p95":0,"max":0}` (sans percentiles s'il n'y a encore aucune mesure) ;
- `public final class MetricsFilter implements RequestHandler`, constructeur `(RequestHandler next, ApiMetrics metrics, Clock clock)` : mesure le temps de `next` avec l'horloge, et enregistre le statut **dans un `finally`** : si `next` lance une exception, la requête compte comme un 500 (et l'exception continue).

**🧪 Les tests (`FiltersTest`, avec une lambda comme suivant) :** la rafale puis le 429 (le suivant n'a pas été appelé) ; un seau par client (et `"  "` compte comme anonyme) ; `Retry-After` arrondi ; le client anonyme par défaut ; sept statuts comptés ; les temps mesurés (une lambda qui avance la `ManualClock`) ; un plantage compté comme 500 ; les mesures vides.

### ☐ Étape 6 — La racine de composition, et les tests de bout en bout

**📖 La leçon : un seul endroit branche tout.** `AtelierApp.create` fabrique chaque pièce et les relie. C'est le seul endroit qui connaît **toutes** les classes concrètes ; les tests l'appellent avec une base en mémoire et une horloge manuelle, `main` avec un fichier et la vraie horloge. Tout le reste ignore dans quel cas il se trouve.

**👉 À toi :** `public final class AtelierApp` (constructeur privé) :
- `public record Config(int wipLimit, int burst, int perSecond)` et `public static final Config DEFAULT = new Config(2, 20, 10)` ;
- `public static RequestHandler create(DataSource dataSource, Clock clock, Config config, Consumer<Throwable> onInternalError)` : les migrations, le dépôt JDBC, le `Board`, le service, les mesures (une fenêtre de 1 000), les routes, le limiteur, et `return new MetricsFilter(new RateLimitFilter(router, limiter), metrics, clock)` ;
- `static int importLegacy(DataSource dataSource, Clock clock)` : si la base est vide, crée les 7 tâches de `SEED` **directement par le dépôt** (elles ont déjà leur colonne, les règles de création ne s'appliquent pas à l'ancien système) et rend 7 ; sinon 0 ;
- `main` : une base dans un fichier (`jdbc:h2:./build/ch19/atelier-app`), la vraie horloge, `DEFAULT`, les erreurs internes sur `System.err`, l'import, puis le serveur sur le port 8080 : `l'atelier est ouvert : http://localhost:8080/board (Entree pour arreter)`.

**🧪 Les tests (`EndToEndTest`) :** une base H2 neuve (avec son `keeper`), `AtelierApp.create`, `WebServer.start(0, …)`, un `HttpClient` qui envoie l'en-tête `X-Client` :
- **une journée à l'atelier** : le tableau vide, quatre créations, deux tâches « En cours », la troisième refusée (409 et son message), une tâche finie, la troisième passe, l'historique, le tableau final `{"A faire":1,"En cours":2,"Fini":1}`, et aucune erreur interne ;
- **les règles en erreurs HTTP** : création hors « A faire », saut de colonne, version périmée, `PUT` qui change de colonne, tâche inconnue, 415, effacement ;
- **le limiteur** : une rafale de 3 pour `ada`, la 4e en 429 (`Retry-After: 1`), `bob` passe, une seconde plus tard `ada` repasse, et `/metrics` compte 6 requêtes dont 1 refusée ;
- **la reprise de l'ancien système**, une seule fois ;
- **le redémarrage** : un second `create` sur la même base garde les données.

### ☐ Étape 7 — En production : lancer, mesurer, corriger

**👉 À toi :** lance `AtelierApp`. Ouvre `http://localhost:8080/board` dans ton navigateur. Puis, dans le terminal :
- crée `build/ch19/move.json` contenant `{"to":"En cours","version":0}`, puis `curl.exe -i -X POST -H "Content-Type: application/json" --data-binary "@build/ch19/move.json" http://localhost:8080/tasks/3/move` ;
- déplace la tâche 2 vers « Fini », puis recommence la tâche 3 ;
- `curl.exe http://localhost:8080/tasks/2/history`, puis `curl.exe http://localhost:8080/metrics`.

**🧪 Expérience — mesurer, trouver, corriger :** regarde `p50` dans `/metrics`. Combien de millisecondes faut-il pour répondre à une requête aussi simple que `/board` ? Cherche la cause : que fait H2 quand la **dernière** connexion à une base en fichier se ferme, et combien de connexions `Transactions` garde-t-il ouvertes entre deux requêtes ? Ajoute `;DB_CLOSE_DELAY=-1` à l'adresse de la base dans `main`, relance (supprime d'abord `build/ch19/atelier-app.mv.db` si tu veux repartir de zéro), et compare.

**👉 Puis :** lance `Check`. Les 19 mutants touchent les règles du tableau, la course de `move`, le `PUT`, le tableau complet, l'historique, le limiteur (l'arrondi, le client), les mesures (le 500 du plantage, le temps, les 429), l'ordre des filtres, la reprise de l'ancien système, le client anonyme, l'en-tête `X-Client` et le 415.

**❓ Question finale :** pour chaque pièce que tu as écrite pendant ce chapitre, quel outil la remplace dans une application Spring Boot ? (La réponse est dans le corrigé ; essaie d'abord.)

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class JsonParser`, `final class JdbcTaskRepository implements TaskRepository`, `final class Migrations`, `final class Router implements RequestHandler`, `final class WebServer`, `final class RateLimiter`, `final class LatencyRecorder`, `interface RequestHandler`, `interface TaskRepository`, `final class Board`, `final class BoardService`, `synchronized`, `final class ApiMetrics`, `final class MetricsFilter implements RequestHandler`, `final class RateLimitFilter implements RequestHandler`, `"Retry-After"`, `"X-Client"`, `final class AtelierApi`, `final class AtelierApp`, `record Config(`, `Migrations.apply(`, `Data.SEED` ; pas de `printStackTrace`.
- **La conception :** aucune méthode de plus de **18 lignes** ; `Board.java` ne contient ni `java.sql` ni `Request` ; `BoardService.java` ni `Jdbc` ni `Response` ; `AtelierApi.java` ni `Jdbc` ni `Response.error(` ; `AtelierApp.java` contient `new MetricsFilter(new RateLimitFilter(`.
- **Tes tests :** au moins **30** tests, `JdbcDataSource`, `ManualClock`, `WebServer.start(0`, `AtelierApp.create(`, `HttpClient`, `"X-Client"`, `CountDownLatch`, `@ParameterizedTest`, `@AfterEach` ; ni `System.out`, ni `Thread.sleep`, ni `8080`.
- **Les 19 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p08_atelier ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 33 tests, 33 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 19/19 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

**Pendant ce chapitre, tu as construit de tes mains** un format d'échange, une base qui évolue, une API HTTP, du travail en arrière-plan, de la résistance aux pannes, des mesures, et tu as assemblé l'essentiel en une application à l'architecture propre, protégée par un filet de tests qui attrape les régressions. C'est le métier.
