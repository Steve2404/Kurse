# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — HTTP

<details><summary>Indice 1</summary>

Pour l'idempotence, imagine que la réponse se perd : le client ne sait pas si sa requête est arrivée, et la renvoie. Que se passe-t-il pour un `DELETE /tasks/4` envoyé deux fois ? Pour un `POST /tasks` envoyé deux fois ?

</details>

<details><summary>Indice 2</summary>

`ApiException` ressemble à `StoreException` du projet 2 : un champ `final int status`, rempli par le constructeur, et un accesseur.

</details>

---

## Étape 2 — Requête, réponse, routeur

<details><summary>Indice 1</summary>

Range chaque route dans un petit `record Route(String method, List<String> segments, Handler handler)` privé, imbriqué dans `Router`. Une méthode `match(segments du modèle, segments du chemin)` rend un `Optional<Map<String, String>>` : vide si ça ne correspond pas, sinon les variables.

</details>

<details><summary>Indice 2</summary>

Dans `dispatch`, parcours **toutes** les routes : si le chemin correspond et la méthode aussi, appelle le handler ; si seul le chemin correspond, ajoute la méthode dans un `TreeSet` (déjà trié). À la fin : `TreeSet` vide → 404, sinon 405 avec `String.join(", ", allowed)`.

</details>

---

## Étape 3 — Un seul endroit pour les codes

<details><summary>Indice 1</summary>

`handle` est un `try { return dispatch(request); }` suivi d'un `catch` par ligne du tableau, du **plus précis** au plus général.

</details>

<details><summary>Indice 2</summary>

Dans le test, `private final List<Throwable> internalErrors = new ArrayList<>();` et `new Router(internalErrors::add)` : la référence de méthode est un `Consumer<Throwable>`.

</details>

---

## Étape 4 — Le port et le dépôt en mémoire

<details><summary>Indice 1</summary>

Reprends les règles du projet 2 : `page` vérifie `page` et `size`, puis un flux sur `tasks.values()` avec `filter`, `skip((long) page * size)` et `limit(size)` ; la `TreeMap` donne déjà l'ordre des identifiants.

</details>

<details><summary>Indice 2</summary>

`update` : la tâche actuelle (`tasks.get(id)`) absente → `NoSuchElementException` ; version différente → `ConflictException` ; sinon range une nouvelle `Task` avec `version + 1`.

</details>

---

## Étape 5 — La frontière JSON

<details><summary>Indice 1</summary>

Une méthode privée `object(String body)` : `JsonParser.parse` dans un `try`, la `JsonException` transformée en `IllegalArgumentException("JSON invalide : " + e.getMessage(), e)`, puis `if (json instanceof JsonObject o) return o;`.

</details>

<details><summary>Indice 2</summary>

Une méthode `intField(JsonObject o, String key)` : `long value = o.getLong(key)`, puis vérifie `Integer.MIN_VALUE <= value <= Integer.MAX_VALUE` avant `(int) value`.

</details>

---

## Étape 6 — Les routes

<details><summary>Indice 1</summary>

Chaque handler est une méthode privée de `TaskApi` avec la signature de `Handler` ; `routes` les branche avec des références de méthode : `.add("GET", "/tasks/{id}", api::get)`.

</details>

<details><summary>Indice 2</summary>

Trois petites aides : `id(params)` (`Long.parseLong`, et une `NumberFormatException` devient `"identifiant invalide : …"`), `intParam(request, name, défaut)`, `requireJson(request)`. Pour la liste : `tasks.page(...).stream().map(t -> (Json) TaskJson.toJson(t)).toList()`, puis `new JsonArray(...)`.

</details>

---

## Étape 7 — L'adaptateur

<details><summary>Indice 1</summary>

`try (exchange) { … }` ferme l'échange quoi qu'il arrive (`HttpExchange` est `AutoCloseable`). Lis le corps avec `exchange.getRequestBody().readNBytes(MAX_BODY_BYTES + 1)`.

</details>

<details><summary>Indice 2</summary>

Pour répondre : d'abord les en-têtes (`exchange.getResponseHeaders().add(…)`), puis `sendResponseHeaders(status, bytes.length)` (ou `-1` sans corps), puis `getResponseBody().write(bytes)`. Les en-têtes ajoutés **après** `sendResponseHeaders` sont perdus. Dans le test des 50 `POST` : une `List<CompletableFuture<HttpResponse<String>>>`, puis `join()` sur chacun.

</details>

---

## Étape 8 — Les mutants

<details><summary>Indice 1</summary>

Un mutant qui survit touche une règle que tes tests ne regardent pas. Relis le tableau de l'indice 2 et cherche le test qui manque.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | plus jamais de 405 : toujours 404 | une méthode refusée |
| 2 | `Allow` dans l'ordre des routes, pas trié | `Allow: DELETE, GET` |
| 3 | `{id}` accepte un segment vide | `/tasks/` donne 404 |
| 4 | un chemin plus long correspond quand même | `/tasks/1/2` donne 404 |
| 5 | le conflit donne 400 au lieu de 409 | la route qui lance `ConflictException` |
| 6 | le 500 envoie le détail de l'exception | le corps exact du 500 |
| 7 | l'erreur interne n'est plus signalée | la liste des erreurs internes |
| 8 | la création rend 200 | le 201 |
| 9 | `Location` sans l'identifiant | `Location: /tasks/8` |
| 10 | 25 tâches par page par défaut | 37 tâches, 20 rendues |
| 11 | `PUT` sans contrôle du `Content-Type` | le 415 d'un `PUT` |
| 12 | `Application/JSON` refusé | le type en majuscules |
| 13 | `DELETE` d'une tâche absente rend 204 | le second `DELETE` |
| 14 | `-3000000000` passe et devient positif | les points hors limites |
| 15 | le JSON invalide donne un 500 | le corps vide |
| 16 | un corps trop gros n'est plus refusé : il est coupé et lu tel quel | le corps trop gros (413) |
| 17 | les valeurs des paramètres ne sont plus décodées | `En%20cours` |
| 18 | on décode avant de couper sur `&` | `q=x%26y%3Dz` |
| 19 | `Content-Type` sans `charset=utf-8` | l'en-tête exact |
| 20 | le corps écrit en ISO-8859-1 | `€` dans un titre |
| 21 | une version plus ancienne n'est pas refusée | le second `PUT` d'Ada et Bob |

</details>
