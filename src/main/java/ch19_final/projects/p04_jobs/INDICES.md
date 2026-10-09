# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'opérateur, derrière un port

<details><summary>Indice 1</summary>

`deliver` déclare `throws InterruptedException` : ton `send` (qui ne le déclare pas) doit l'attraper. Trois `catch` à la suite : `IllegalStateException`, `IllegalArgumentException`, `InterruptedException`.

</details>

<details><summary>Indice 2</summary>

Dans le test d'interruption, un `try { … } finally { Thread.interrupted(); }` remet le drapeau à zéro quoi qu'il arrive. `Thread.interrupted()` (statique) **lit et baisse** le drapeau ; `isInterrupted()` le lit seulement.

</details>

---

## Étape 2 — Le recul exponentiel

<details><summary>Indice 1</summary>

`firstDelay.toMillis() * Math.pow(multiplier, failures - 1)` donne un `double` ; compare-le au plafond **avant** d'arrondir.

</details>

<details><summary>Indice 2</summary>

Une constante dans une interface est `public static final` d'office : `Sleeper REAL = duration -> Thread.sleep(duration.toMillis());` suffit. La lambda a le droit de lancer `InterruptedException`, puisque la méthode de l'interface la déclare.

</details>

---

## Étape 3 — Réessayer

<details><summary>Indice 1</summary>

Une boucle `for (int attempt = 1; ; attempt++)` sans condition : on en sort par un `return` (réussite) ou un `throw`. Dans le `catch (GatewayException e)` : si on doit abandonner, on ajoute les échecs précédents à `e` et on la relance ; sinon on la range dans une liste `earlier`.

</details>

<details><summary>Indice 2</summary>

L'attente dans une méthode à part, `pause(int failures, List<GatewayException> earlier)`, avec son propre `try`/`catch (InterruptedException e)`. Pour tester l'interruption : `new Retrier(policy, d -> { throw new InterruptedException(); }, metrics)`.

</details>

---

## Étape 4 — Compter

<details><summary>Indice 1</summary>

`increment()` pour ajouter 1, `sum()` pour lire. Le record `Snapshot` imbriqué se crée avec les cinq `sum()`.

</details>

<details><summary>Indice 2</summary>

Les méthodes qui comptent sont « accès paquet » (sans `public`) : seuls `Retrier` et `ReminderService`, dans le même paquet, comptent ; tout le monde peut lire `snapshot()`.

</details>

---

## Étape 5 — Le pool borné

<details><summary>Indice 1</summary>

Une méthode privée `start(Notification n)` : un `try` autour de `CompletableFuture.supplyAsync(() -> deliver(n), executor)`, et un `catch (RejectedExecutionException e)` qui rend `CompletableFuture.completedFuture(new SendResult(n.taskId(), "refuse", "file pleine"))`.

</details>

<details><summary>Indice 2</summary>

`sendAll` : `notifications.stream().map(this::send).toList()`, puis `CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join()`, puis les `join()` de chacun, triés avec `Comparator.comparingLong(SendResult::taskId)`.

</details>

---

## Étape 6 — Le délai et les erreurs emballées

<details><summary>Indice 1</summary>

Enchaîne sur le futur de `supplyAsync` : `.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS).exceptionally(e -> failure(n, e))`.

</details>

<details><summary>Indice 2</summary>

Dans `failure` : `Throwable cause = e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;`, puis `if (cause instanceof TimeoutException)`.

</details>

---

## Étape 7 — L'idempotence

<details><summary>Indice 1</summary>

`accepted.computeIfAbsent(n.taskId(), id -> start(n))`. Puis, si le futur rendu est déjà terminé et que son statut est `refuse`, `accepted.remove(n.taskId(), result)` : la version à deux arguments n'efface que si la valeur est **encore** celle-là.

</details>

<details><summary>Indice 2</summary>

Pour les 16 fils : un `ExecutorService` de test, et pour chacun `CompletableFuture.supplyAsync(() -> { gate.await(); return service.send(…); }, callers)`. Un seul `gate.countDown()` les libère tous ensemble. Ferme ce pool dans un `finally`.

</details>

---

## Étape 8 — L'arrêt propre, les mutants

<details><summary>Indice 1</summary>

`shutdown()`, puis `if (executor.awaitTermination(…)) return 0;`, sinon `return executor.shutdownNow().size();`.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | l'attente commence à 200 ms | deux échecs puis réussite : `[100, 200]` |
| 2 | plus de plafond | 5 échecs : 1 000 ms |
| 3 | `maxAttempts = 0` accepté | le refus de la politique |
| 4 | les erreurs définitives sont réessayées | un numéro inconnu |
| 5 | la dernière erreur n'a plus les précédentes | les `suppressed` |
| 6 | les échecs précédents ne sont pas retenus | les `suppressed` après une interruption |
| 7 | le drapeau d'interruption est perdu | `isInterrupted()` après l'interruption |
| 8 | les nouveaux essais ne sont plus comptés | les métriques |
| 9 | l'attente ne grandit plus | `[100, 200]` |
| 10 | plus d'idempotence | le même rappel deux fois |
| 11 | un refus est retenu pour toujours | le rappel refusé, redemandé |
| 12 | la file n'a plus de borne : jamais de refus | la file pleine |
| 13 | le délai dépassé devient un échec | `delai depasse` |
| 14 | l'erreur n'est plus déballée | le message de l'échec |
| 15 | les résultats ne sont plus triés | `sendAll` dans le désordre |
| 16 | `shutdown` rend toujours 0 | les 2 jamais commencés |
| 17 | les refus ne sont plus comptés | les métriques de la file pleine |
| 18 | « occupé » devient définitif | l'adaptateur |
| 19 | l'adaptateur perd le drapeau | l'interruption de l'adaptateur |
| 20 | deux compteurs inversés dans `snapshot` | les métriques |

</details>
