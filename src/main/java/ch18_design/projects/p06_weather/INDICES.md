# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'adaptateur

<details><summary>Indice 1</summary>

Deux petites méthodes `static` dans l'adaptateur : `stationCode(String city)` (`city.substring(0, 3).toUpperCase()`) et `toCelsius(double fahrenheit)`. `Math.round` sur un `double` rend un `long` : convertis-le en `int`.

</details>

<details><summary>Indice 2</summary>

Nice : (11 − 32) × 5 / 9 = −11,67. `Math.round(-11.67)` donne −12 ; `(int) -11.67` donne −11 (la troncature va **vers zéro**).

</details>

---

## Étape 2 — Le proxy

<details><summary>Indice 1</summary>

Un petit record privé `Entry(Forecast forecast, Instant expires)` et une `Map<String, Entry>`. À chaque demande : lire l'heure, chercher l'entrée, la rendre si `now.isBefore(entry.expires())`, sinon demander à `inner` et ranger la nouvelle entrée.

</details>

<details><summary>Indice 2</summary>

`MutableClock` : un champ `Instant now`, `advance(Duration d)` fait `now = now.plus(d)`, `instant()` rend `now`, `getZone()` rend `ZoneOffset.UTC`, `withZone(…)` peut rendre `this`. `ScriptedWeather` : une `ArrayDeque<Object>` de réponses (des `Integer` ou des exceptions) ; à chaque appel, retire la suivante (garde la dernière quand il n'en reste qu'une) et lance-la si c'est une exception.

</details>

---

## Étape 3 — Les décorateurs

<details><summary>Indice 1</summary>

`RetryingWeather.forecast` : une variable `RuntimeException last = null;`, une boucle `for` de `attempts` tours, un `try { return inner.forecast(city); } catch (RuntimeException e) { last = e; }`, et `throw last;` après la boucle.

</details>

<details><summary>Indice 2</summary>

Dans `LoggingWeather`, le `catch` ajoute la ligne puis fait `throw e;` : la **même** exception repart. Le test le vérifie avec `assertSame(failure, assertThrows(…))`.

</details>

---

## Étape 4 — Le secours et la moyenne

<details><summary>Indice 1</summary>

Crée l'exception **avant** la boucle : `IllegalStateException failure = new IllegalStateException("aucune source pour " + city);`, puis `failure.addSuppressed(e)` dans chaque `catch`, et `throw failure;` après la boucle.

</details>

<details><summary>Indice 2</summary>

La moyenne : une `List<Integer>` des réponses, puis `answers.stream().mapToInt(Integer::intValue).average().orElseThrow()`, puis `(int) Math.round(average)`. Le composite imbriqué : la moyenne intérieure vaut 15, puis (15 + 30) / 2 = 22,5, arrondi à 23.

</details>

---

## Étape 5 — Tout emboîter

<details><summary>Indice 1</summary>

Écris-le en trois variables, de l'intérieur vers l'extérieur : `meteo` (les tentatives autour de l'adaptateur), `withBackup` (le secours), puis `return new LoggingWeather(new CachingWeather(withBackup, clock, TTL), log);`.

</details>

<details><summary>Indice 2</summary>

Pour la question des tentatives autour du secours : déroule le premier appel à la main. L'adaptateur échoue une fois… et que fait alors le secours, **avant** que les tentatives aient leur mot à dire ?

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Le test d'intégration tue beaucoup de mutants à lui seul : compte les appels au fournisseur (`api.calls()`) et vérifie le journal **entier**.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le code de station n'est plus en majuscules.
2. La conversion tronque au lieu d'arrondir.
3. Le cache resservit encore à 10 minutes pile.
4. Le cache range tout sous une mauvaise clé : il ne sert plus jamais.
5. Le cache garde les prévisions 20 minutes au lieu de 10.
6. Une tentative de moins que demandé.
7. Après le dernier échec, une nouvelle exception remplace la dernière reçue.
8. 0 tentative est accepté.
9. Le journal avale l'exception (rend `null`).
10. Le texte de la ligne d'erreur du journal a changé.
11. Les causes ne sont plus gardées dans l'exception du secours.
12. La moyenne tronque au lieu d'arrondir.
13. La source de la moyenne compte toutes les sources, même celles en panne.
14. Une source en panne fait échouer la moyenne.
15. Le journal est **dans** le cache.
16. 2 tentatives au lieu de 3.
17. Le secours passe **avant** la météo.

</details>
