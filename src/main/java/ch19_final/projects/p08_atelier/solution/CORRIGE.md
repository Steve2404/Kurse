# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier (40 fichiers de code : tes 31 briques et 9 pièces nouvelles), les tests de référence dans [`BoardTest.java`](BoardTest.java), [`BoardServiceTest.java`](BoardServiceTest.java), [`FiltersTest.java`](FiltersTest.java) et [`EndToEndTest.java`](EndToEndTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18**, **H2 2.3.232**, **JUnit 5.11.4** et le `curl.exe` de Windows 11, le 9 octobre 2026. Les tests de référence ont été lancés 15 fois de suite sans un échec.

---

## Étape 1 — Faire évoluer les briques

Le code : [`RequestHandler.java`](RequestHandler.java), [`Request.java`](Request.java), [`TaskRepository.java`](TaskRepository.java), et les petits changements dans [`Router.java`](Router.java), [`WebServer.java`](WebServer.java), [`JdbcTaskRepository.java`](JdbcTaskRepository.java) et [`TaskJson.java`](TaskJson.java).

**Question — le port :** un port est écrit **pour** le métier qui s'en sert, et c'est ce métier qui décide de ce qu'il contient (le principe d'inversion des dépendances, chapitre 18). L'API du projet 3 n'avait besoin que de créer, lire, paginer, modifier et effacer ; le tableau Kanban a aussi besoin de **déplacer** une tâche avec son historique et de **compter** les tâches par colonne. Même nom, parce que c'est le même rôle (« les tâches, vues du métier ») ; autres méthodes, parce que le métier a grandi. On n'ajoute pas au port ce que le métier n'utilise pas : `search` du projet 2 reste dans `JdbcTaskRepository`, sans être dans le port.

---

## Étape 2 — Le cœur

Le code : [`Board.java`](Board.java).

```java
public void checkMove(Task task, String to, Map<String, Integer> counts) {
    int from = COLUMNS.indexOf(task.column());
    int target = column(to);
    if (from == target) {
        throw new IllegalArgumentException("la tache " + task.id() + " est deja dans " + to);
    }
    if (Math.abs(target - from) != 1) {
        throw new IllegalArgumentException("deplacement impossible : " + task.column() + " -> " + to + " (une colonne a la fois)");
    }
    if (to.equals(IN_PROGRESS) && counts.getOrDefault(IN_PROGRESS, 0) >= wipLimit) {
        throw new ApiException(409, "limite atteinte : En cours (" + wipLimit + " taches au plus)");
    }
}
```

---

## Étape 3 — Le service, et la course

Le code : [`BoardService.java`](BoardService.java).

**Expérience — sans `synchronized` (vérifié) :** sur **30** lancements du test de la course, **30 échecs** : plus de 2 tâches se retrouvent « En cours ». Chaque `move` passe plusieurs millisecondes dans la base entre la lecture des comptes et le déplacement : la fenêtre est large, et les 6 fils s'y engouffrent. Avec `synchronized`, 15 lancements sur 15 donnent exactement 2.

---

## Étape 4 — Les routes

Le code : [`AtelierApi.java`](AtelierApi.java).

---

## Étape 5 — Les filtres

Le code : [`RateLimitFilter.java`](RateLimitFilter.java), [`ApiMetrics.java`](ApiMetrics.java), [`MetricsFilter.java`](MetricsFilter.java).

```java
@Override
public Response handle(Request request) {
    Instant start = clock.instant();
    int status = 500;
    try {
        Response response = next.handle(request);
        status = response.status();
        return response;
    } finally {
        metrics.record(status, Duration.between(start, clock.instant()));
    }
}
```

Le mutant 15 inverse les filtres : `RateLimitFilter(MetricsFilter(Router))`. Les requêtes refusées ne passent plus par la mesure : `/metrics` compterait 5 requêtes au lieu de 6, et 0 refus au lieu de 1. Le test de bout en bout du limiteur tue ce mutant (vérifié). Un tableau de bord qui ne voit pas les refus dirait « tout va bien » pendant que des clients reçoivent des 429.

---

## Étape 6 — La racine et les tests de bout en bout

Le code : [`AtelierApp.java`](AtelierApp.java).

```java
public static RequestHandler create(DataSource dataSource, Clock clock, Config config, Consumer<Throwable> onInternalError) {
    Transactions tx = new Transactions(dataSource);
    Migrations.apply(tx, TaskSchema.MIGRATIONS);
    BoardService service = new BoardService(new JdbcTaskRepository(tx, clock), new Board(config.wipLimit()));
    ApiMetrics metrics = new ApiMetrics(new LatencyRecorder(1000));
    Router router = AtelierApi.routes(service, metrics, onInternalError);
    RateLimiter limiter = new RateLimiter(config.burst(), config.perSecond(), clock);
    return new MetricsFilter(new RateLimitFilter(router, limiter), metrics, clock);
}
```

Les cinq tests de bout en bout passent en **moins d'une seconde** (vérifié : 845 ms la première fois, 121 ms une fois la JVM chaude), base, serveur et client compris.

---

## Étape 7 — En production

**Le lancement (vérifié) :**

```
taches reprises de l'ancien systeme : 7
l'atelier est ouvert : http://localhost:8080/board (Entree pour arreter)
```

**Les requêtes avec curl (vérifiées) :** `/board` donne `{"A faire":3,"En cours":2,"Fini":2}`. Déplacer la tâche 3 vers « En cours » :

```
HTTP/1.1 409 Conflict
Content-type: application/json; charset=utf-8
Content-length: 57

{"error":"limite atteinte : En cours (2 taches au plus)"}
```

La tâche 2 vers « Fini » (avec `{"to":"Fini","version":0}`) : `{"id":2,"title":"Regler les freins","column":"Fini","points":3,"version":1}` ; puis la tâche 3 passe : `{"id":3,"title":"Commander 12 chambres a air","column":"En cours","points":1,"version":1}`. L'historique de la tâche 2 : `["2026-10-09T23:51:49.522773 En cours -> Fini"]` (la vraie horloge, d'où les microsecondes) ; le tableau : `{"A faire":2,"En cours":2,"Fini":3}`.

**Expérience — mesurer, trouver, corriger (vérifié) :** sans `DB_CLOSE_DELAY`, `/metrics` donnait `"p50":104,"p95":149` : **une centaine de millisecondes** pour lire trois nombres. La cause : une base H2 dans un fichier est **refermée** dès que sa dernière connexion se ferme, et `Transactions` ouvre une connexion par opération puis la referme. Entre deux requêtes, plus aucune connexion : chaque requête **rouvre** la base depuis le disque. Mesuré sur 20 requêtes `/board` sans HTTP : **60 ms** par requête sans l'option, **moins d'1 ms** avec `;DB_CLOSE_DELAY=-1`, qui garde la base ouverte jusqu'à l'arrêt du programme. Après la correction, `/metrics` donne `"p50":1,"p95":4`.

En entreprise, la vraie réponse est un **pool de connexions** (HikariCP est le plus répandu) : quelques connexions ouvertes une fois, prêtées à chaque requête, puis rendues au pool au lieu d'être fermées. Le pool règle aussi le cas des vraies bases sur le réseau (PostgreSQL, MySQL), où ouvrir une connexion coûte des dizaines de millisecondes. Retiens la démarche plus que la réponse : **mesurer** (le `/metrics` que tu as écrit), **trouver** la cause, **corriger**, **remesurer**.

**Les mutants :** avec les tests de référence, les **19 sont tués** (vérifié). Le `Check` prend environ 15 secondes.

**Question finale — ce que tu as écrit, et ce qui le remplace dans Spring Boot :**

| Ce que tu as écrit | Dans une application Spring Boot |
|---|---|
| `JsonParser`, `JsonWriter`, `TaskJson` | Jackson (`ObjectMapper`), et des records convertis automatiquement |
| `WebServer` (`com.sun.net.httpserver`) | Tomcat intégré |
| `Router` et ses routes | `@RestController`, `@GetMapping("/tasks/{id}")`, `@PathVariable` |
| la traduction exception → code HTTP | `@ControllerAdvice` et `@ExceptionHandler` |
| `RateLimitFilter`, `MetricsFilter` | des `Filter` (ou `HandlerInterceptor`), Bucket4j ou Resilience4j |
| `ApiMetrics`, `LatencyRecorder` | Micrometer et Spring Boot Actuator (`/actuator/metrics`) |
| `Transactions.write` | `@Transactional` |
| `JdbcTaskRepository` | `JdbcTemplate`, Spring Data JDBC ou JPA |
| `Migrations`, `TaskSchema` | Flyway ou Liquibase |
| `AtelierApp.create` (la racine) | le conteneur d'injection de Spring (`@Component`, `@Configuration`, `@Bean`) |
| `Clock` injectée | un `@Bean Clock`, injecté comme le reste |
| `CircuitBreaker`, `Retrier` (projets 4 et 5) | Resilience4j |
| `ReminderService` (projet 4) | `@Async`, `@Scheduled`, un `TaskExecutor` |
| tes tests de bout en bout | `@SpringBootTest` avec `WebTestClient` ou `MockMvc`, et Testcontainers pour une vraie base |

Spring fait en quelques annotations ce que tu as écrit en quelques milliers de lignes. Mais tu sais maintenant **ce qu'il fait** : quand un 415, un 429, une transaction qui ne s'annule pas ou une requête à 100 ms apparaîtront, tu sauras où chercher.
