# Drill de rappel 7 (BONUS) — `CompletableFuture`, Fork/Join, `ThreadLocal`, `Semaphore`, `CountDownLatch`

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md). À faire après le projet bonus p08.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall07`** dans le paquet `ch13_concurrency.drills.r07_bonus`. Son `main` déclare `throws Exception`.
- Elle a un champ `static final ThreadLocal<StringBuilder> BUFFER = ThreadLocal.withInitial(StringBuilder::new)`.
- Dans le même fichier, crée **`SumTask extends RecursiveTask<Long>`** :
  - elle somme les entiers de `[from, to]` ;
  - sous 10 000 éléments, elle calcule directement ;
  - sinon `left.fork()`, puis `droite.compute() + left.join()`.

## Défis

- ☐ **D01.**
  - `supplyAsync(() -> 20).thenApply(n -> n + 1).thenApply(n -> n * 2).join()` ;
  - puis `supplyAsync(() -> 3).thenCombine(supplyAsync(() -> 4), (x, y) -> x * y).join()`.
  → `D01 : 42 12`
- ☐ **D02.** `supplyAsync(() -> "id-7").thenCompose(id -> supplyAsync(() -> "profil de " + id))`.
  → `D02 : profil de id-7`
- ☐ **D03.** `broken = supplyAsync(() -> Integer.parseInt("x"))`. Affiche :
  - `exceptionally(e -> -1)` ;
  - `handle` (`"erreur " + nom simple de la cause`) ;
  - la liste remplie par un `whenComplete` (`valeur` ou `exception`) ;
  - le nom de l'exception levée par le `join()` de ce `whenComplete`.
  → `D03 : -1 | erreur NumberFormatException | [exception] CompletionException`
- ☐ **D04.** 5 futures `n * n` (n de 1 à 5), `allOf(...).join()`. Affiche la somme des `join()`, puis si toutes sont `isDone()`.
  → `D04 : 55 true`
- ☐ **D05.** `ForkJoinPool.commonPool().invoke(new SumTask(1, 1_000_000))`, puis la comparaison avec la formule n(n+1)/2.
  → `D05 : 500000500000 true`
- ☐ **D06.** `main` ajoute `"main"` à son `BUFFER`. Un autre thread ajoute `"autre"` au sien, et le garde dans une case. Affiche les deux, puis si ce sont deux objets différents.
  → `D06 : main autre true`
- ☐ **D07.** Deux parties :
  - `Semaphore(3)` : `tryAcquire(2)` deux fois, `availablePermits()`, `release(2)`, puis de nouveau `availablePermits()` ;
  - ` | `, puis `CountDownLatch(2)` : `countDown()`, `getCount()`, encore deux `countDown()`, puis `getCount()`.
  → `D07 : true false 1 3 | 1 0`

## Expériences (hors sortie attendue)

1. `thenApply` à la place de `thenCompose` dans D02 : quel est le type de la variable ? Que vaut `join()` ?
2. Dans `SumTask`, remplace `droite.compute()` par un 2e `fork()` + `join()` : le résultat change-t-il ? Et la performance ?
3. `release()` sans `acquire()` préalable : erreur ou non ? Que devient `availablePermits()` ?
4. Pourquoi faut-il appeler `remove()` sur un `ThreadLocal` dans un pool de threads ?

## Sortie attendue complète

```
D01 : 42 12
D02 : profil de id-7
D03 : -1 | erreur NumberFormatException | [exception] CompletionException
D04 : 55 true
D05 : 500000500000 true
D06 : main autre true
D07 : true false 1 3 | 1 0
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| `CompletableFuture` | Rôle |
|---|---|
| `supplyAsync(s[, ex])` / `runAsync(r[, ex])` | lancer (par défaut sur le `ForkJoinPool.commonPool()`) |
| `thenApply(f)` | transformer le résultat |
| `thenCompose(f)` | enchaîner une fonction qui rend une `CompletableFuture` (aplatit) |
| `thenCombine(autre, bf)` | combiner deux résultats indépendants |
| `thenAccept(c)` / `thenRun(r)` | consommer, ou agir après |
| `exceptionally(f)` | valeur de repli en cas d'erreur |
| `handle(bf)` | (résultat, exception) → nouvelle valeur |
| `whenComplete(bc)` | observer, sans changer le résultat |
| `allOf(...)` / `anyOf(...)` | tout attendre / le premier terminé |
| `join()` / `get()` | `CompletionException` (non vérifiée) / `ExecutionException` (vérifiée) |
| `completeOnTimeout(v, n, u)` / `orTimeout(n, u)` | valeur par défaut / échec après un délai |

- **Fork/Join :** `RecursiveTask<T>` (`compute()` rend T), `RecursiveAction` (rien). On fait `fork()` d'une moitié, `compute()` de l'autre, puis `join()`. `pool.invoke(tâche)` est l'appel racine.
- **`ThreadLocal`** : une copie par thread ; `withInitial`, `get`, `set`, `remove`. Dans un pool, les threads sont réutilisés : pense à `remove()`.
- **`Semaphore(n)`** : `acquire`/`release` (dans un `finally`), `tryAcquire`, `availablePermits`.
- **`CountDownLatch(n)`** : `countDown` décrémente (pas sous 0), `await` attend 0. Elle n'est **pas** réutilisable (contrairement à `CyclicBarrier`).

</details>
