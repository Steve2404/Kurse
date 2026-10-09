# Drill de rappel 5 — Limiteur, disjoncteur, percentiles, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p05.

**Chrono cible :** 30 min, puis 15 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r05_resilience`.
- Le temps ne se lit **que** sur une `Clock` : ni `Thread.sleep`, ni `System.currentTimeMillis`, ni `System.nanoTime`, ni `Instant.now()`. Pas de `double` dans `Bucket`. Aucune méthode de plus de 18 lignes.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 1 à 7).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : les défis dans l'ordre, `// D04 : ✗` après 3 minutes bloqué, `Check.java`, puis la carte mémoire, puis ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `public final class ManualClock extends Clock`, construite avec un `Instant` : `void advance(Duration)`, `instant()`, `getZone()` (UTC), `withZone` → `UnsupportedOperationException`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `public final class Bucket`, construite avec `(int capacity, int perSecond, Clock clock)`, plein au départ : `boolean tryAcquire()` ; il se remplit de `perSecond` jetons par seconde sans dépasser `capacity` ; compte en **millièmes** de jeton dans un `long`.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** Le reste de milliseconde est **gardé** (des pas de 1,5 ms : le jeton revient au 667e pas) ; `long waitMillis()` : les millisecondes avant le prochain jeton, arrondies **au-dessus** (334 pour 3 jetons par seconde), 0 s'il y en a un.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public final class Breaker`, avec `public enum State { CLOSED, OPEN, HALF_OPEN }`, construit avec `(int threshold, Duration openFor, Clock clock)` : `<T> T call(Supplier<T> action)` et `State state()`. Fermé : `threshold` échecs (une `RuntimeException`) **de suite** l'ouvrent, un succès remet le compte à zéro. Ouvert : `call` lance `BreakerOpenException` **sans** appeler l'action, message `"ouvert : encore 4 s"` (le temps restant arrondi **au-dessus**) ; `public class BreakerOpenException extends RuntimeException`, construite avec `(long seconds)`.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Après `openFor` : demi-ouvert, et l'appel d'essai passe. Réussi : fermé ; raté : ouvert pour un nouveau `openFor` entier.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `public final class Percentiles`, `public static long of(long[] values, double p)` : le rang le plus proche, sans modifier le tableau reçu ; vide → `IllegalArgumentException("aucune valeur")`, `p` hors de `]0, 100]` → `IllegalArgumentException`. Et `public final class LastKnown<K, V>` : `V get(K key, Function<K, V> source)` rend la valeur de la source (et la retient), ou, si la source lance une `RuntimeException`, la dernière valeur connue pour cette clé ; sans valeur connue, l'exception de la source.
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

- **Le remplissage** : `elapsed = Duration.between(last, clock.instant()).toMillis()` ; s'il est positif : `milli = Math.min(capacity, milli + elapsed * perSecond)` et `last = last.plusMillis(elapsed)` (**pas** `last = now`).
- **L'arrondi au-dessus** d'une division entière : `(a + b - 1) / b` ; pour des secondes depuis des millisecondes : `(ms + 999) / 1000`.
- **Le disjoncteur** : `call` n'est pas `synchronized` ; trois petites méthodes le sont : `before()` (ouvert : refuser ou passer en demi-ouvert), `success()`, `failure()` (en demi-ouvert, un échec rouvre même sous le seuil).
- **Le percentile** : `values.clone()`, `Arrays.sort`, puis `sorted[(int) Math.ceil(p / 100 * n) - 1]`.
- **Le repli** : `try { v = source.apply(k); known.put(k, v); return v; } catch (RuntimeException e) { … }`.

</details>
