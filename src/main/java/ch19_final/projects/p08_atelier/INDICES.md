# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Faire évoluer les briques

<details><summary>Indice 1</summary>

Copie d'abord **toutes** les briques, sans rien changer, et fais compiler le paquet (les seules erreurs viennent des classes du projet 3 qui attendent un `TaskRepository` : crée tout de suite le nouveau port). Puis un changement à la fois : `RequestHandler`, `Router implements RequestHandler`, `WebServer`, `Request`.

</details>

<details><summary>Indice 2</summary>

Quand tu ajoutes `client` à `Request`, IntelliJ souligne chaque `new Request(…)` à 5 arguments : il y en a dans `Request` lui-même (les fabriques) et dans `WebServer`. Dans le constructeur compact : `client = client == null || client.isBlank() ? ANONYMOUS : client;`.

</details>

---

## Étape 2 — Le cœur

<details><summary>Indice 1</summary>

Travaille avec les **positions** des colonnes : `COLUMNS.indexOf(nom)` (−1 si inconnue). Même position : déjà dedans ; `Math.abs(arrivée - départ) != 1` : plus d'une colonne d'écart.

</details>

<details><summary>Indice 2</summary>

L'ordre des contrôles compte pour les messages : d'abord la colonne inconnue, puis la même colonne, puis l'écart, et la limite en dernier. `counts.getOrDefault("En cours", 0)` : une colonne vide n'apparaît pas dans les comptes de la base.

</details>

---

## Étape 3 — Le service

<details><summary>Indice 1</summary>

`move` en deux lignes : `board.checkMove(get(id), to, tasks.countByColumn());` puis `return tasks.move(id, to, expectedVersion);`. Le mot `synchronized` devant la méthode suffit.

</details>

<details><summary>Indice 2</summary>

Pour la course, chaque fil rend `true` s'il a réussi, `false` s'il a reçu une `ApiException`. Compte les `true` après `allOf(...).join()` : `moves.stream().filter(CompletableFuture::join).count()`.

</details>

---

## Étape 4 — Les routes

<details><summary>Indice 1</summary>

Reprends `TaskApi` du projet 3 : mêmes aides (`id`, `intParam`, `requireJson`), mais chaque handler appelle le `BoardService`. Les routes très courtes peuvent être des lambdas : `(r, p) -> Response.json(200, metrics.toJson())`.

</details>

<details><summary>Indice 2</summary>

Pour `/board` : `service.board().forEach(builder::add)` remplit un `JsonObject.Builder` dans l'ordre (la méthode `add(String, long)` accepte un `Integer`). Pour l'historique : `new JsonArray(lignes.stream().map(l -> (Json) new JsonString(l)).toList())`.

</details>

---

## Étape 5 — Les filtres

<details><summary>Indice 1</summary>

`RateLimitFilter.handle` : `Duration wait = limiter.acquire(request.client())` ; `wait.isZero()` → `next.handle(request)` ; sinon le 429. L'arrondi : `Math.max(1, (wait.toMillis() + 999) / 1000)`.

</details>

<details><summary>Indice 2</summary>

`MetricsFilter.handle` : `int status = 500;` **avant** le `try` ; dans le `try`, appelle `next`, range son statut, rends la réponse ; dans le `finally`, `metrics.record(status, …)`. Si `next` lance une exception, `status` vaut encore 500.

</details>

---

## Étape 6 — La racine et les tests de bout en bout

<details><summary>Indice 1</summary>

Dans `create`, l'ordre de fabrication suit les dépendances : `Transactions`, les migrations, le dépôt, le `Board`, le service, les mesures, le `Router` (qui a besoin du service et des mesures), le limiteur, puis les deux filtres.

</details>

<details><summary>Indice 2</summary>

Une méthode d'aide dans le test : `call(method, path, json, client)`, qui construit la requête avec `header("X-Client", client)`, ajoute `Content-Type` seulement s'il y a un corps, et utilise `BodyPublishers.noBody()` sinon.

</details>

---

## Étape 7 — En production, les mutants

<details><summary>Indice 1</summary>

Pour l'expérience : une base H2 dans un fichier est **fermée** dès que sa dernière connexion se ferme. Or `Transactions` ouvre une connexion par opération et la referme aussitôt : entre deux requêtes, plus aucune connexion n'est ouverte.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | on peut reculer de deux colonnes | `Fini -> A faire` |
| 2 | une tâche de plus en cours | la limite avec les vrais comptes |
| 3 | la limite s'applique à toutes les colonnes | la limite ne concerne que « En cours » |
| 4 | une tâche peut naître ailleurs que dans « A faire » | la création refusée |
| 5 | « déjà dans » n'est plus détecté | le message de la même colonne |
| 6 | `move` sans `synchronized` | la course des 6 fils |
| 7 | `PUT` change la colonne | le `PUT` refusé |
| 8 | les colonnes vides valent `null` | le tableau vide |
| 9 | l'historique d'une tâche inconnue rend `[]` | la tâche inconnue |
| 10 | `Retry-After: 0` | l'arrondi au-dessus |
| 11 | un seul seau pour tous les clients | `bob` passe pendant qu'`ada` attend |
| 12 | un plantage compté comme un succès | le plantage compté 500 |
| 13 | les temps valent tous 0 | les temps mesurés |
| 14 | les 429 comptés comme erreurs du client | les mesures du limiteur |
| 15 | les filtres dans l'autre ordre | `/metrics` compte les refus |
| 16 | l'ancien système repris deux fois | la reprise unique |
| 17 | un client blanc n'est plus anonyme | `"  "` partage le seau anonyme |
| 18 | l'en-tête `X-Client` ignoré | le limiteur de bout en bout |
| 19 | `move` accepte un corps sans `Content-Type` | le 415 |

</details>
