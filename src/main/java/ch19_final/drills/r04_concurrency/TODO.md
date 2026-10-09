# Drill de rappel 4 — La concurrence en conditions réelles, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p04.

**Chrono cible :** 30 min, puis 15 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r04_concurrency`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Ni `Thread.sleep`, ni `LinkedBlockingQueue`. Aucune méthode de plus de 18 lignes.
- Tu n'écris pas de tests : les tests de référence vérifient ton code (avec des `CountDownLatch`, jamais des attentes au hasard).

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1 à 8).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : les défis dans l'ordre, `// D04 : ✗` après 3 minutes bloqué, `Check.java`, puis la carte mémoire, puis ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@FunctionalInterface public interface Sleeper` (`void sleep(Duration duration) throws InterruptedException`) ; `public class RetryableException extends RuntimeException` (constructeur `(String message)`) ; `public final class Retry`, `public static <T> T call(Supplier<T> action, int attempts, Duration first, Sleeper sleeper)` : au plus `attempts` essais ; seules les `RetryableException` sont réessayées ; l'attente commence à `first` et **double** à chaque fois ; après le dernier essai, la dernière exception est relancée avec les précédentes en `suppressed`, dans l'ordre.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Une interruption pendant l'attente : le drapeau est **relevé**, et `IllegalStateException("interrompu")` est lancée.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `public final class BoundedPool`, `public static ThreadPoolExecutor create(int threads, int capacity)` : `threads` fils, une file de `capacity` places, et quand tout est plein, `execute` lance `RejectedExecutionException`.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public final class Async`, `public static CompletableFuture<String> withTimeout(Supplier<String> work, Executor executor, Duration timeout)` : le travail en arrière-plan sur `executor` ; le futur rend `"ok:" + valeur`, `"delai"` si le délai est dépassé, ou `"erreur:" + message de la vraie cause` (déballe la `CompletionException`).
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `public final class OnceCache<K, V>` : `V get(K key, Function<K, V> compute)` calcule chaque clé **une seule fois**, même demandée par 16 fils en même temps ; `int computations()` compte les calculs.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `public final class Shutdown`, `public static int stop(ExecutorService executor, Duration grace) throws InterruptedException` : plus de nouveau travail, `grace` pour finir, puis interrompre ; rend le nombre de travaux jamais commencés (0 si tout a fini).
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Réessayer** : `for (int attempt = 1; ; attempt++) { try { return action.get(); } catch (RetryableException e) { if (attempt >= attempts) { earlier.forEach(e::addSuppressed); throw e; } earlier.add(e); } pause(…); delay = delay.multipliedBy(2); }`.
- **L'interruption** : dans le `catch (InterruptedException e)` : `Thread.currentThread().interrupt();`, puis lancer.
- **Le pool borné** : `new ThreadPoolExecutor(n, n, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(capacity), new ThreadPoolExecutor.AbortPolicy())`.
- **Le délai** : `CompletableFuture.supplyAsync(work, executor).orTimeout(ms, TimeUnit.MILLISECONDS).thenApply(v -> "ok:" + v).exceptionally(…)` ; une erreur du travail arrive en `CompletionException` (sa cause est la vraie), un délai en `TimeoutException` directe.
- **Une seule fois** : `ConcurrentHashMap.computeIfAbsent(clé, k -> { compter ; calculer })`, jamais `containsKey` puis `put`.
- **L'arrêt propre** : `shutdown()`, `if (awaitTermination(…)) return 0;`, `return shutdownNow().size();`.

</details>
