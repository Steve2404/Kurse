# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`MinHeap.java`](MinHeap.java), [`Emergency.java`](Emergency.java), [`Heaps.java`](Heaps.java) et [`MedianFinder.java`](MedianFinder.java) ; les tests de référence dans [`MinHeapTest.java`](MinHeapTest.java) et [`HeapsTest.java`](HeapsTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Le tas binaire

**Question — le plus petit des deux enfants :** l'enfant qui monte devient le **parent** de l'autre enfant. Il doit donc être plus petit que lui. Si l'on échangeait avec le plus grand des deux, celui-ci se retrouverait au-dessus du plus petit : la règle « chaque parent est plus petit que ses enfants » serait cassée, et `poll` rendrait plus tard un mauvais minimum (c'est le mutant 2).

---

## Étape 2 — Les urgences

L'ordre de sortie du test (vérifié) : Bob (5, arrivé 2), Dan (5, arrivé 4), Cid (3), Eve (2, arrivée 0), Ana (2, arrivée 1).

**L'expérience** (vérifiée) : la file s'affiche **`[1, 2, 4, 5, 3]`**, et `peek()` vaut 1. Ce n'est **pas** trié : `toString` (comme un `for`) parcourt le **tableau** du tas, qui n'est ordonné que « parent avant enfants ». Seuls `peek` et `poll` garantissent le minimum. Pour vider une `PriorityQueue` dans l'ordre, il faut des `poll()` successifs.

---

## Étape 3 — Les k plus grands, et la fusion

Les résultats (vérifiés) : `topK({5, 9, 1, 7, 3, 8, 2}, 3)` = `{9, 8, 7}` ; `topK({5, 1, 5, 2}, 2)` = `{5, 5}` ; `topK({1, 3}, 5)` = `{3, 1}`.

---

## Étape 4 — La médiane

Après chaque ajout de 5, 15, 1, 3, 8, 2 : **5**, **10** (5 et 15), **5**, **4** (3 et 5), **5**, **4** (3 et 5).

**Question — toujours passer par les deux tas :** c'est le moyen le plus simple de garantir que **tout** élément de la moitié basse reste plus petit que **tout** élément de la moitié haute. En faisant passer le nouveau par la moitié basse, on en ressort le plus grand des petits ; en l'envoyant en haut, il rejoint sa vraie place. On rééquilibre ensuite les tailles. Pas besoin de comparer `v` aux sommets à la main, ni de traiter des cas. Trois opérations en O(log n), toujours les mêmes.

---

## Étape 5 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié). Le mutant 3 de la première version (`left <= size` au lieu de `left < size`) était **équivalent** : la case juste après la fin du tas contient toujours la valeur qui est en train de descendre, et une comparaison **stricte** avec elle ne change rien.

---

## Expériences de fin de projet

1. Sur 10 millions de nombres au hasard (vérifié, après échauffement) : **619 ms** pour `Arrays.sort`, **149 ms** pour le tas de taille 10. Le tas fait 10 millions d'opérations en O(log 10), le tri 10 millions en O(log 10 000 000). Et le tas n'a pas besoin de copier le tableau.
2. Sans le `(long)` (vérifié), la médiane vaut **−2,0** : `Integer.MAX_VALUE + (Integer.MAX_VALUE - 2)` déborde avant la division (c'est le mutant 12).
