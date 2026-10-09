# Projet 3 — L'API de l'atelier : un serveur HTTP avec le JDK seul

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- le protocole **HTTP** : méthode, chemin, paramètres, en-têtes, corps, **codes de statut** ;
- une **API REST** : des ressources (`/tasks`, `/tasks/42`) et les méthodes `GET`, `POST`, `PUT`, `DELETE` ;
- un **routeur** écrit à la main : des modèles de chemin (`/tasks/{id}`), 404 et 405 ;
- un **seul endroit** qui traduit les exceptions en codes HTTP, et jamais de détail interne envoyé au client ;
- la **validation à la frontière** : un client peut envoyer n'importe quoi ;
- l'architecture **hexagonale** (chapitre 18) : le cœur se teste sans réseau, un **adaptateur** branche le vrai serveur ;
- les **tests d'intégration** avec un vrai serveur (port 0) et un vrai client (`java.net.http.HttpClient`) ;
- la **concurrence** côté serveur : plusieurs requêtes en même temps.

**Ce qui est FOURNI :** `Data.java` contient `SEED`, les 7 tâches de départ. Tu ne modifies pas ce fichier.

**Ce que TU réutilises :** tes classes JSON du **projet 1** (`Json`, les six records, `JsonException`, `JsonParser`, `JsonWriter` ; ni `JsonDemo` ni tes tests) et `Task`, `NewTask` du **projet 2**. Copie-les dans ce paquet : dans IntelliJ, sélectionne les fichiers, **Ctrl+C**, clique sur le paquet `p03_api`, **Ctrl+V** ; IntelliJ corrige la ligne `package`.

**Ce que TU crées :** dans `ch19_final.projects.p03_api` : `ConflictException`, `ApiException`, `TaskRepository`, `InMemoryTaskRepository`, `Request`, `Response`, `Handler`, `Router`, `TaskJson`, `TaskApi`, `WebServer`, `ApiDemo`, et tes tests (par exemple `RouterTest`, `TaskApiTest`, `WebServerTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito, **sans bibliothèque web** : le serveur est `com.sun.net.httpserver.HttpServer`, fourni avec le JDK. Aucune méthode de plus de **18 lignes**. Ni `printStackTrace`, ni `getStackTrace` dans ton code. Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni le port fixe `8080` (un test utilise le port `0`).

> **Pourquoi sans Spring ?** En entreprise, tu écriras ces API avec Spring Boot, en quelques annotations. Mais Spring fait exactement ce que tu vas écrire ici : lire la requête, choisir la route, convertir le JSON, transformer les exceptions en codes. Le jour où une requête renvoie un 415 incompréhensible ou un 500 sans trace, tu sauras où regarder.

---

## Tableau de bord

### ☐ Étape 1 — HTTP en dix minutes

**📖 La leçon : une requête, une réponse.** Ton navigateur et un serveur échangent du **texte**. Une requête :

```
POST /tasks?page=1 HTTP/1.1          <- la METHODE, le CHEMIN (avec ses parametres), la version
Host: localhost:8080                 <- des EN-TETES : nom: valeur
Content-Type: application/json
                                     <- une ligne vide
{"title":"Pneu","points":2}          <- le CORPS (facultatif)
```

La réponse a la même forme : `HTTP/1.1 201 Created`, des en-têtes, une ligne vide, un corps. Le **code de statut** dit comment ça s'est passé :

| Code | Nom | Quand |
|---|---|---|
| 200 | OK | tout va bien, voici le résultat |
| 201 | Created | une ressource a été créée ; l'en-tête `Location` dit où |
| 204 | No Content | c'est fait, rien à renvoyer (un `DELETE`) |
| 400 | Bad Request | la requête est fausse (JSON invalide, champ manquant, règle métier) |
| 404 | Not Found | cette ressource n'existe pas |
| 405 | Method Not Allowed | le chemin existe, pas avec cette méthode ; `Allow` dit lesquelles |
| 409 | Conflict | conflit avec l'état actuel (le verrou optimiste du projet 2) |
| 413 | Payload Too Large | corps trop gros |
| 415 | Unsupported Media Type | le corps n'est pas dans un format accepté |
| 500 | Internal Server Error | un **bug** du serveur |

Règle simple : **4xx**, c'est la faute du client (il peut corriger sa requête) ; **5xx**, c'est la faute du serveur.

**📖 La leçon : REST.** On nomme des **ressources** avec des chemins, et la **méthode** dit l'action : `GET /tasks` (la liste), `POST /tasks` (en créer une), `GET /tasks/42` (en lire une), `PUT /tasks/42` (la remplacer), `DELETE /tasks/42` (l'effacer). `GET` ne modifie **jamais** rien.

**👉 À toi :** copie tes classes du projet 1 et du projet 2 (voir plus haut). Crée `public class ConflictException extends RuntimeException`, constructeur `(long id, int staleVersion)`, message `"tache 3 : version 0 perimee"`, et `public class ApiException extends RuntimeException`, constructeur `(int status, String message)`, avec `int status()`.

**❓ Questions :**
- Pourquoi `GET` ne doit-il jamais modifier les données ? Pense à un navigateur qui précharge les liens d'une page, ou à un robot d'indexation.
- `PUT` et `DELETE` sont dits **idempotents** : les envoyer deux fois a le même effet qu'une fois. Et `POST` ? Pourquoi est-ce important quand le réseau coupe au milieu d'une requête ?

### ☐ Étape 2 — Requête, réponse, routeur : sans réseau

**📖 La leçon : le cœur ne connaît pas le serveur.** Si tes règles lisaient directement l'objet du serveur HTTP (`HttpExchange`), chaque test devrait démarrer un serveur. On fait comme au chapitre 18 : le cœur parle avec des objets **à lui** (`Request`, `Response`), et un **adaptateur** (étape 7) traduit. 90 % des tests tournent alors en quelques millisecondes, sans réseau.

**📖 La leçon : le routeur.** Il associe une **méthode** et un **modèle de chemin** à un handler. Dans `/tasks/{id}`, le segment `{id}` est **variable** : il correspond à n'importe quel segment non vide, et sa valeur est passée au handler (`{id=42}`). On découpe les chemins sur `/` : `/tasks/42` → `[tasks, 42]`. Deux réponses d'erreur à ne pas confondre :
- **aucune** route n'a ce chemin : **404** ;
- le chemin correspond à des routes, mais **pas** avec cette méthode : **405**, avec l'en-tête `Allow` qui liste les méthodes permises.

**👉 À toi :**
- `public record Request(String method, String path, Map<String, String> query, String body, String contentType)` : la map est copiée ; `contentType` peut être `null` ; deux fabriques, `Request.of(method, path)` (sans corps : corps `""`, aucun paramètre) et `Request.json(method, path, body)` (type `application/json`) ; `Optional<String> param(String name)` ;
- `public record Response(int status, Json body, Map<String, String> headers)` : `body` à `null` veut dire « pas de corps » ; les fabriques `json(status, body)`, `error(status, message)` (le corps `{"error":"…"}`, la même forme pour **toutes** les erreurs) et `noContent()` (204) ; `Response withHeader(String name, String value)` rend une **copie** ;
- `@FunctionalInterface public interface Handler` : `Response handle(Request request, Map<String, String> pathParams)` ;
- `public final class Router` : `Router add(String method, String template, Handler handler)` (rend `this`, pour enchaîner) et `Response handle(Request request)`. La **première** route qui correspond gagne. Un `/` final compte : `/tasks/` n'est pas `/tasks`. 404 : `"aucune route pour /nope"` ; 405 : `"methode PUT non permise sur /tasks/5"` et `Allow` = les méthodes permises, **triées**, séparées par `", "` (`DELETE, GET`).

**🧪 Les tests (`RouterTest`) :** des handlers jetables (des lambdas qui rendent un `JsonString` disant qui a répondu) ; les segments fixes et variables, deux variables (`/tasks/{id}/history/{n}`) ; la première route gagne ; 404 (dont `/`, `/tasks/1/2`, `/tasks/`, `/tasks//history/1`) ; 405 et `Allow` ; les aides de `Request` et `Response`.

**❓ Question :** `split("/")` sur `"tasks/"` rend `[tasks]`, alors que `split("/", -1)` rend `[tasks, ]`. Lequel veux-tu, et pourquoi ?

### ☐ Étape 3 — Un seul endroit pour les codes d'erreur

**📖 La leçon : les handlers lancent, le routeur traduit.** Un handler qui écrit lui-même `Response.error(404, …)` mélange le métier et HTTP, et chaque handler finit par traduire à sa façon. On fait l'inverse : les handlers lancent des **exceptions métier** (`NoSuchElementException`, `IllegalArgumentException`, `ConflictException`), et `Router.handle` les attrape **toutes** au même endroit (dans Spring : `@ControllerAdvice`) :

| Exception | Code |
|---|---|
| `ApiException` | son propre code |
| `IllegalArgumentException` (et donc `NumberFormatException`) | 400 |
| `NoSuchElementException` | 404 |
| `ConflictException` | 409 |
| toute autre `RuntimeException` | 500 |

**📖 La leçon : un 500 ne raconte rien.** Une exception inattendue est un **bug**. Son message et sa trace peuvent révéler le nom des classes, la requête SQL, des données d'un autre client : on ne les envoie **jamais**. Le client reçoit `{"error":"erreur interne"}` ; le détail part dans le **journal** du serveur, pour les développeurs.

**👉 À toi :** le constructeur `Router(Consumer<Throwable> onInternalError)`, et la traduction ci-dessus dans `handle`. Pour chaque 500, `onInternalError` reçoit l'exception.

**🧪 Les tests :** une route par exception (400, 404, 409, et une `ApiException(418, …)`) ; une `IllegalStateException("secret interne")` donne exactement `{"error":"erreur interne"}`, et la liste des erreurs internes (remplie par `internalErrors::add`) contient l'exception, avec son message.

**❓ Question :** l'ordre des `catch` compte-t-il ici ? Que se passerait-il si `catch (RuntimeException e)` venait en premier ?

### ☐ Étape 4 — Le port des tâches, et le dépôt en mémoire

**📖 La leçon : un serveur, plusieurs fils.** Le serveur traite les requêtes **en parallèle**, sur plusieurs fils (étape 7). Deux `POST` simultanés appellent `create` en même temps : sans précaution, `lastId++` (lire, ajouter, écrire) peut donner le **même** numéro aux deux (chapitre 13). La solution la plus simple : `synchronized` sur chaque méthode du dépôt.

**👉 À toi :**
- `public interface TaskRepository` (le **port**) : `Task create(NewTask)`, `Optional<Task> find(long)`, `List<Task> page(String column, int page, int size)`, `Task update(Task)`, `boolean delete(long)`, `int count()` ;
- `public final class InMemoryTaskRepository implements TaskRepository` : une `TreeMap<Long, Task>`, des identifiants 1, 2, 3… ; les mêmes règles que le dépôt JDBC du projet 2 : pages triées par `id`, mêmes messages pour `page` et `size`, verrou optimiste (`ConflictException`), `NoSuchElementException("tache 42 introuvable")` ; **toutes** les méthodes `synchronized`.

Dans le projet 8, le dépôt JDBC du projet 2 implémentera ce même port.

**❓ Question :** `synchronized` sur toutes les méthodes : un seul fil à la fois dans le dépôt. Est-ce un problème pour un serveur ? Quand faudrait-il faire mieux ?

### ☐ Étape 5 — La frontière : du JSON aux objets

**📖 La leçon : ne faire confiance à rien.** Le corps d'une requête vient d'un inconnu : il peut être vide, mal formé, un tableau au lieu d'un objet, avoir un champ manquant, du mauvais type, ou un nombre géant. Chaque cas doit donner un **400 avec un message utile**, jamais un 500. Et l'inverse : un champ **inconnu** est ignoré (un client plus récent peut envoyer plus que ce qu'on attend : c'est le « lecteur tolérant »).

**📖 Le piège des conversions :** `(int) -3000000000L` vaut `1294967296`, un nombre **positif**. Une conversion sans contrôle transforme une valeur absurde en valeur plausible.

**👉 À toi :** `public final class TaskJson` (constructeur privé) :
- `static JsonObject toJson(Task task)` : `{"id":2,"title":"…","column":"…","points":3,"version":0}`, dans cet ordre ;
- `static NewTask newTask(String body)` et `static Task task(long id, String body)` (pour un `PUT` : `version` en plus dans le corps) ;
- un JSON invalide → `IllegalArgumentException("JSON invalide : " + message de la JsonException)` ; autre chose qu'un objet → `"objet JSON attendu"` ; les champs se lisent avec tes `getString` et `getLong` du projet 1 ; un entier hors des limites d'un `int` → `"champ points : hors limites"` (ou `version`).

**🧪 Les tests (dans `TaskApiTest`, étape 6) :** au moins 10 corps faux pour `POST`, avec le message exact, dont : un champ manquant, l'objet non fermé, un tableau, un titre blanc, des points négatifs, `1.5`, `3000000000`, `-3000000000`, une colonne qui est un nombre. Et : aucune tâche n'a été créée.

### ☐ Étape 6 — Les routes de l'API

**👉 À toi :** `public final class TaskApi` (constructeur privé) et `public static Router routes(TaskRepository tasks, Consumer<Throwable> onInternalError)` :

| Route | Réponse |
|---|---|
| `GET /health` | 200 `{"status":"ok","tasks":7}` |
| `GET /tasks` | 200, un tableau ; paramètres facultatifs `column`, `page` (0 par défaut), `size` (20 par défaut, constante `DEFAULT_PAGE_SIZE`) ; un nombre illisible → 400 `"parametre page invalide : x"` |
| `POST /tasks` | 201, la tâche créée, et `Location: /tasks/8` |
| `GET /tasks/{id}` | 200, ou 404 `"tache 99 introuvable"` ; un identifiant illisible → 400 `"identifiant invalide : abc"` |
| `PUT /tasks/{id}` | 200, la tâche avec sa nouvelle version ; 404 ; 409 si la version est périmée |
| `DELETE /tasks/{id}` | 204 sans corps, ou 404 |

`POST` et `PUT` exigent un `Content-Type` qui **commence** par `application/json`, sans tenir compte des majuscules (`application/json; charset=utf-8` et `Application/JSON` sont acceptés) : sinon `ApiException(415, "Content-Type application/json attendu")`.

Dans `TaskApi.java`, aucun `Response.error(` : les handlers **lancent**, le routeur traduit.

**🧪 Les tests (`TaskApiTest`) :** les 7 tâches de `SEED` dans un dépôt neuf (`@BeforeEach`) ; chaque route, chaque code ; la taille par défaut (avec 37 tâches, `GET /tasks` en rend 20) ; un `@ParameterizedTest` pour les paramètres faux ; le 415 (sans type, `text/plain`) et `Application/JSON` accepté ; Ada et Bob en `PUT` (409) ; un dépôt **bogué** (une classe imbriquée dont `count()` lance une `NullPointerException`) : `/health` rend un 500 sans détail, et l'erreur est signalée.

**❓ Question :** un `GET /tasks?size=1000000` serait refusé par le dépôt (400). Pourquoi cette limite est-elle une question de **sécurité**, et pas seulement de confort ?

### ☐ Étape 7 — L'adaptateur : le vrai serveur

**📖 La leçon : `com.sun.net.httpserver`.** Le JDK contient un petit serveur HTTP :

```java
HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
server.setExecutor(Executors.newFixedThreadPool(4));   // les requetes en parallele
server.createContext("/", exchange -> { ... });         // tout passe par un seul handler
server.start();
```

Dans le handler, `exchange` donne la méthode (`getRequestMethod()`), l'adresse (`getRequestURI()` : `getPath()` est déjà décodé, `getRawQuery()` ne l'est pas), les en-têtes et le corps ; on répond avec `sendResponseHeaders(code, longueur)` (longueur `-1` : pas de corps), puis on écrit le corps et on ferme l'échange.

**📖 La leçon : le port 0.** Un test ne doit pas prendre le port 8080 : il est peut-être déjà pris (par ta démo !), et deux tests en parallèle se battraient pour lui. Avec le port **0**, le système choisit un port **libre** ; `server.getAddress().getPort()` dit lequel.

**📖 La leçon : se protéger.** On lit au plus `MAX_BODY_BYTES + 1` octets (`readNBytes`) : si l'on en a obtenu plus que la limite, le corps est trop gros (413), et l'on n'a pas rempli la mémoire avec 10 Go envoyés par un inconnu.

**👉 À toi :** `public final class WebServer implements AutoCloseable` :
- `static final int MAX_BODY_BYTES = 64 * 1024` ;
- `public static WebServer start(int port, Router router)` : sur `127.0.0.1`, 4 fils ; `int port()` ; `close()` arrête le serveur (`stop(0)`) et ses fils ;
- pour chaque échange : un corps de plus de 65 536 octets → 413 `"corps trop gros (65536 octets au plus)"` ; sinon une `Request` (le corps décodé en UTF-8, l'en-tête `Content-Type`), puis la `Response` du routeur : ses en-têtes, `Content-Type: application/json; charset=utf-8` s'il y a un corps, le corps écrit avec `JsonWriter.compact` en **UTF-8** ;
- `static Map<String, String> query(String rawQuery)` : `null` ou vide → map vide ; on coupe sur `&` puis sur le premier `=`, et l'on décode **après** avoir coupé (`URLDecoder.decode(…, StandardCharsets.UTF_8)`) ; un paramètre sans `=` vaut `""` ; en cas de doublon, le **premier** gagne.

Dans `WebServer.java`, rien de `TaskApi` : l'adaptateur ne connaît que le `Router`.

**🧪 Les tests (`WebServerTest`) :** un serveur sur le port 0 dans `@BeforeEach`, fermé dans `@AfterEach` ; un `HttpClient` avec des délais (`connectTimeout`, `timeout`) ; un `GET` avec des accents et `€` (le corps et le `Content-Type` exacts) ; un `POST` (201, `Location`) ; des paramètres encodés (`En%20cours` et `En+cours`) ; `query` seul ; un `DELETE` (204, corps vide, pas de `Content-Type`) ; 404, 405 et 415 à travers le réseau ; un corps de 64 Ko accepté, un plus gros refusé (413, rien créé) ; **50 `POST` simultanés** (`sendAsync`, puis `join`) donnent 50 `Location` différentes ; après `close()`, la connexion est refusée (`IOException`).

**🧪 Expérience :** retire **un** `synchronized`, celui de `create`, et lance 10 fois le test des 50 `POST`. Combien d'échecs ? Remets-le.

**❓ Question :** pourquoi décoder **après** avoir coupé sur `&` ? Que deviendrait `q=x%26y` sinon ?

### ☐ Étape 8 — La démonstration, curl, et les mutants

**👉 À toi :** `public final class ApiDemo` avec `main` : un dépôt avec les tâches de `SEED`, le serveur sur le port **8080** (le message d'erreur interne affiché avec `System.err.println`), puis quatre requêtes faites avec un `HttpClient`, chacune affichée `METHODE chemin -> code corps` : la page 0 de taille 2 de « A faire », la création de « Vélo cargo », la tâche 99, l'effacement de la tâche 1. Enfin `serveur ouvert : http://localhost:8080/tasks (Entree pour arreter)`, et `System.in.read()` garde le serveur ouvert.

**🧪 Expériences (le serveur ouvert) :**
- ouvre `http://localhost:8080/tasks` dans ton navigateur, puis `http://localhost:8080/tasks/2` et `http://localhost:8080/rien` ;
- dans un terminal PowerShell : `curl.exe -i http://localhost:8080/tasks/2` (`-i` affiche les en-têtes). Que remarques-tu sur l'écriture des noms d'en-têtes ?
- crée le fichier `build/ch19/tache.json` contenant `{"title":"Pneu arriere","column":"A faire","points":2}`, puis : `curl.exe -i -X POST -H "Content-Type: application/json" --data-binary "@build/ch19/tache.json" http://localhost:8080/tasks`. Recommence **sans** `-H "Content-Type: application/json"`. Puis `curl.exe -i -X PATCH http://localhost:8080/tasks/2` ;
- le premier serveur toujours ouvert, lance `ApiDemo` une **seconde** fois. Que se passe-t-il ?

> Dans PowerShell, écris `curl.exe` et pas `curl` : `curl` y est un autre outil (`Invoke-WebRequest`), avec d'autres options. Et un fichier pour le corps (`@fichier`) évite l'enfer des guillemets.

**👉 Puis :** lance `Check`. Les 21 mutants touchent le routeur (404, 405, `Allow`, le `/` final, les codes, le 500), les routes (201, `Location`, la taille par défaut, le 415), la frontière JSON, l'adaptateur (413, le décodage, l'UTF-8) et le verrou optimiste.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** tes classes JSON et tes tâches recopiées, `class ConflictException extends RuntimeException`, `class ApiException extends RuntimeException`, `interface TaskRepository`, `final class InMemoryTaskRepository implements TaskRepository`, `synchronized`, `record Request(`, `record Response(`, `interface Handler`, `final class Router`, `final class TaskJson`, `final class TaskApi`, `final class WebServer implements AutoCloseable`, `final class ApiDemo`, `HttpServer.create(`, `setExecutor(`, `URLDecoder.decode(`, `readNBytes(`, `sendResponseHeaders(`, `StandardCharsets.UTF_8`, `"Allow"`, `"Location"`, `Data.SEED` ; ni `printStackTrace`, ni `getStackTrace`.
- **La conception :** aucune méthode de plus de **18 lignes** ; ni `Router.java` ni `TaskApi.java` ne contiennent `HttpExchange` ; `TaskApi.java` ne contient pas `Response.error(` ; `WebServer.java` ne contient pas `TaskApi`.
- **Tes tests :** au moins **50** tests, `HttpClient`, `WebServer.start(0`, `@AfterEach`, `sendAsync(`, `Request.json(`, `@ParameterizedTest`, `assertTimeoutPreemptively(` ; ni `System.out`, ni `Thread.sleep`, ni `8080`.
- **Les 21 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p03_api ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 50 tests, 50 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 21/21 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

« Les tests de référence sur ton code » utilisent **tes** copies du JSON et des tâches : si ce `[FAIL]` parle de JSON ou d'un titre, compare tes copies avec les solutions des projets 1 et 2.
