# Drill de rappel 2 — Executors, `Future`, planification

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall02`** dans le paquet `ch13_concurrency.drills.r02_executors`. Le `main` déclare `throws Exception`.
- Chaque executor est arrêté dans un `finally`.

## Défis

- ☐ **D01.** Un `newSingleThreadExecutor()` : `execute` trois tâches qui ajoutent `a`, `b`, `c` à une liste synchronisée. Puis `shutdown`, `awaitTermination`, et affiche la liste.
  → `D01 : [a, b, c]`
- ☐ **D02.** Un `newFixedThreadPool(3)` (utilisé jusqu'à D06) : `submit(() -> 6 * 7)`. Affiche `get()`, puis `isDone()`.
  → `D02 : 42 true`
- ☐ **D03.** `invokeAll` de `() -> 1`, `() -> 2`, `() -> 3` : affiche la somme des résultats. Puis `invokeAny` d'une tâche qui lève `IllegalStateException` et d'une autre qui rend `"seule reussite"`.
  → `D03 : 6 seule reussite`
- ☐ **D04.** Une tâche qui dort 10 s. Fais `get(10, TimeUnit.MILLISECONDS)` → attrape l'exception (nom simple). Puis `cancel(true)` et `isCancelled()`.
  → `D04 : TimeoutException true true`
- ☐ **D05.** Une tâche qui lève `UnsupportedOperationException("non")`. Dans le `catch` de `get()`, affiche le nom simple et le message de la **cause**.
  → `D05 : UnsupportedOperationException non`
- ☐ **D06.** `submit(() -> System.out.print(""))`, puis `get()`.
  → `D06 : null`
- ☐ **D07.** Après le `shutdown` du pool :
  - `awaitTermination(5 s)` ;
  - puis `execute(() -> { })`, à attraper.
  
  Affiche `isShutdown()`, le résultat de `awaitTermination`, `isTerminated()`, puis le nom de l'exception.
  → `D07 : true true true RejectedExecutionException`
- ☐ **D08.** Un `newCachedThreadPool()` : `invokeAll` d'une tâche qui rend `"vite"` et d'une tâche qui dort 10 s, avec un délai de **100 ms**. Affiche :
  - le résultat de la 1re ;
  - `isCancelled()` de la 2e ;
  - puis `isDone()` des deux.
  → `D08 : vite true true true`
- ☐ **D09.** Un `newSingleThreadScheduledExecutor()` :
  - `schedule(() -> "plus tard", 10, TimeUnit.MILLISECONDS)` ;
  - `scheduleAtFixedRate(…, 0, 2, MILLISECONDS)`, qui incrémente un `AtomicInteger` et décompte une `CountDownLatch(3)`.
  
  Attends la latch, puis `cancel(false)` la tâche périodique. Affiche le résultat différé, `ticks >= 3`, puis `isCancelled()`.
  → `D09 : plus tard true true`

## Expériences (hors sortie attendue)

1. `submit(() -> { Thread.sleep(10); return 1; })` compile-t-il ? Et `execute(() -> { Thread.sleep(10); })` ? Pourquoi ?
2. `invokeAny` d'une liste où **toutes** les tâches échouent : quelle exception ?
3. Que rend `shutdownNow()` ? Les tâches en cours sont-elles arrêtées d'office ?
4. Oublie `shutdown()` dans un `main` lancé seul : que se passe-t-il ?
5. Combien de threads crée `newCachedThreadPool()` ? Quand les libère-t-il ? Pourquoi est-ce risqué avec des milliers de tâches longues ?

## Sortie attendue complète

```
D01 : [a, b, c]
D02 : 42 true
D03 : 6 seule reussite
D04 : TimeoutException true true
D05 : UnsupportedOperationException non
D06 : null
D07 : true true true RejectedExecutionException
D08 : vite true true true
D09 : plus tard true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode | Rend | Remarque |
|---|---|---|
| `execute(Runnable)` | `void` | une exception de la tâche est perdue (affichée par le thread) |
| `submit(Runnable)` / `submit(Callable<T>)` | `Future<?>` / `Future<T>` | l'exception est ENVELOPPÉE dans `ExecutionException` à `get()` |
| `invokeAll(tâches)` | `List<Future<T>>` | attend tout, dans l'ordre de la liste |
| `invokeAll(tâches, n, unité)` | `List<Future<T>>` | au plus n ; les tâches inachevées sont **annulées** (`isCancelled`, et `isDone` vrai) |
| `invokeAny(tâches)` | `T` | le résultat d'une tâche réussie ; les autres sont annulées |
| `shutdown()` | `void` | plus de nouvelles tâches, celles en file finissent |
| `shutdownNow()` | `List<Runnable>` | les tâches jamais lancées ; interrompt celles en cours |
| `awaitTermination(n, unité)` | `boolean` | attend la fin (au plus n) |

**Les fabriques :** `newSingleThreadExecutor()`, `newFixedThreadPool(n)`, `newCachedThreadPool()`, `newSingleThreadScheduledExecutor()`, `newScheduledThreadPool(n)`.

**`Future`** : `get()`, `get(n, unité)` (avec `TimeoutException`), `cancel(mayInterrupt)`, `isDone()`, `isCancelled()`.

**La planification :**
- `schedule(tâche, délai, unité)` ;
- `scheduleAtFixedRate(Runnable, délai initial, période, unité)` : départs à cadence fixe ;
- `scheduleWithFixedDelay(Runnable, délai initial, délai, unité)` : délai fixe **après la fin** de chaque exécution ;
- les deux périodiques n'acceptent que des `Runnable`.

</details>
