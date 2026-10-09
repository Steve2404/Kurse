# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'horloge manuelle

<details><summary>Indice 1</summary>

Un seul champ, `private Instant now;`. `advance` fait `now = now.plus(duration)`.

</details>

<details><summary>Indice 2</summary>

IntelliJ écrit les trois méthodes à ta place : place le curseur sur `ManualClock` (soulignée en rouge), **Alt+Entrée**, *Implement methods*.

</details>

---

## Étape 2 — Le seau à jetons

<details><summary>Indice 1</summary>

Une constante `ONE = 1000` (un jeton en millièmes), un champ `long milliTokens` (plein au départ : `capacity * ONE`) et un champ `Instant last`. Une méthode privée `refill()` appelée au début de chaque méthode publique.

</details>

<details><summary>Indice 2</summary>

Dans `refill` : `long elapsedMillis = Duration.between(last, now).toMillis();` ; s'il est positif, ajoute `elapsedMillis * perSecond` (plafonné) et fais `last = last.plusMillis(elapsedMillis)`, **pas** `last = now`. Pour l'arrondi au-dessus d'une division entière : `(a + b - 1) / b`.

</details>

---

## Étape 3 — Un seau par client

<details><summary>Indice 1</summary>

`buckets.computeIfAbsent(client, c -> new TokenBucket(capacity, perSecond, clock))`, puis `tryAcquire()` ; si c'est non, `timeUntilNext()`.

</details>

<details><summary>Indice 2</summary>

`buckets.values().removeIf(TokenBucket::isFull)` : retirer depuis la vue `values()` retire de la map. Compte la taille avant et après.

</details>

---

## Étape 4 — Le disjoncteur

<details><summary>Indice 1</summary>

Découpe en trois méthodes privées `synchronized` : `beforeCall()` (refuse, ou passe en demi-ouvert), `onSuccess()`, `onFailure()`. `call` n'est pas `synchronized` : `beforeCall()`, puis l'action dans un `try`, puis `onSuccess()` ou `onFailure()`.

</details>

<details><summary>Indice 2</summary>

« Encore ouvert » : `clock.instant().isBefore(openedAt.plus(openFor))`. Le temps restant en secondes, arrondi au-dessus : `(millisecondes + 999) / 1000`. Dans le `catch (RuntimeException e)` : `isFailure.test(e)` choisit entre `onFailure()` et `onSuccess()`, puis `throw e`.

</details>

---

## Étape 5 — Un seul essai à la fois

<details><summary>Indice 1</summary>

Dans `beforeCall`, après le passage éventuel en demi-ouvert : si l'état est `HALF_OPEN`, refuse si `trialRunning`, sinon lève-le.

</details>

<details><summary>Indice 2</summary>

`onSuccess` et `onFailure` baissent `trialRunning` tous les deux. En demi-ouvert, un échec rouvre **même sous le seuil** : `if (state == State.HALF_OPEN || failures >= threshold)`.

</details>

---

## Étape 6 — Les percentiles

<details><summary>Indice 1</summary>

`record` : `window[next] = millis ; next = (next + 1) % window.length ; count = Math.min(count + 1, window.length)`.

</details>

<details><summary>Indice 2</summary>

`percentile` : copie les `count` premières cases (`Arrays.copyOf(window, count)`), trie, puis `rank = (int) Math.ceil(p / 100 * count)` et rends `sorted[rank - 1]`. Quand la fenêtre est pleine, l'ordre des cases n'a pas d'importance : on trie.

</details>

---

## Étape 7 — Le stock protégé

<details><summary>Indice 1</summary>

Un petit `record Known(int quantity, Instant at)` privé, dans une `ConcurrentHashMap<String, Known>`. L'âge : `Duration.between(known.at(), clock.instant()).toSeconds()`.

</details>

<details><summary>Indice 2</summary>

`try { … } catch (IllegalArgumentException e) { throw e; } catch (RuntimeException e) { return fallback(ref, e); } finally { latency.record(…); }` : le premier `catch` doit venir avant le second, sinon il ne serait jamais atteint.

</details>

---

## Étape 8 — Les mutants

<details><summary>Indice 1</summary>

Un mutant qui survit touche une règle que tes tests ne regardent pas. Relis le tableau de l'indice 2 et cherche le test qui manque.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | le seau déborde | une heure d'attente, puis 3 essais |
| 2 | la fraction de milliseconde est perdue | les pas de 1,5 ms (667) |
| 3 | un millième de jeton suffit | la rafale, l'attente de 600 ms |
| 4 | l'attente arrondie en dessous | 334 ms |
| 5 | `evictFull` oublie tous les seaux | l'oubli des seuls seaux pleins |
| 6 | un seau neuf à chaque requête | deux requêtes du même client |
| 7 | l'essai raté ne rouvre pas | l'essai raté rouvre pour 10 s |
| 8 | il faut un échec de plus | le 3e échec ouvre |
| 9 | un succès ne remet pas le compte à zéro | 2 échecs, un succès, 2 échecs |
| 10 | l'essai attend une milliseconde de trop | l'essai à l'instant exact |
| 11 | plusieurs essais en même temps | l'essai unique |
| 12 | une erreur du client compte comme une panne | les « référence inconnue » |
| 13 | le délai restant arrondi en dessous | 4 s après 6,5 s |
| 14 | `Math.round` au lieu de `Math.ceil` | les percentiles et le 0 ms du disjoncteur ouvert |
| 15 | les cases vides comptent dans les percentiles | une seule mesure, p0,1 |
| 16 | la fenêtre repart de 1 quand elle est pleine | une fenêtre de 3 après 5 mesures |
| 17 | le cache garde la première valeur, pas la dernière | `cache (7 s)` |
| 18 | une référence inconnue est cachée par le repli | le scénario du fournisseur (`SADDLE`) |
| 19 | les temps de réponse valent tous 0 | les 30 ms de la réponse fraîche |
| 20 | l'horloge n'avance que par secondes entières | toute avance de moins d'une seconde (400 ms, 100 ms) |

</details>
