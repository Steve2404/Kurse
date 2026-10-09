# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Calculator.java`](Calculator.java), [`Monotonic.java`](Monotonic.java), [`MinStack.java`](MinStack.java) et [`QueueFromStacks.java`](QueueFromStacks.java) ; les tests de référence dans les trois fichiers `…Test.java`.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**.

---

## Étape 1 — Les parenthèses

**L'expérience** (vérifiée) : après `push(1)`, `push(2)`, `push(3)`, la `Stack` s'affiche **`[1, 2, 3]`** et l'`ArrayDeque` **`[3, 2, 1]`** ; leurs `peek()` rendent tous deux **3**. La `Stack` s'affiche du **fond** vers le dessus (c'est un `Vector` déguisé), l'`ArrayDeque` du **dessus** vers le fond. Parcourir une `Stack` avec un `for` donne donc les éléments à l'envers de leur ordre de sortie : un piège de plus.

**Question — la pile vide à la fin :** `"(("` n'a **aucune** fermante en trop : sans la vérification finale, `balanced` le trouverait équilibré. Une ouvrante restée sur la pile n'a jamais été fermée (c'est le mutant 1).

---

## Étape 2 — La notation polonaise inverse

**Le code :** `evalRpn` et `apply`. `5 1 2 + 4 * + 3 -` vaut 5 + ((1 + 2) × 4) − 3 = **14** ; `7 2 /` vaut **3** (division entière).

---

## Étape 3 — La gare de triage

**Question — priorité strictement supérieure :** l'opérateur `-` qui attend ne partirait pas quand le 2e `-` arrive (même priorité) : on obtiendrait `8 3 2 - -`, c'est-à-dire 8 − (3 − 2) = **7** au lieu de 3. Les opérations se feraient de droite à gauche. C'est le mutant 5, tué par le test `8 - 3 - 2`.

---

## Étape 4 — La pile monotone

**Question — pas de O(n²) :** il faut compter les opérations sur **tout** le parcours, pas par tour. Chaque indice est **empilé une fois** et **dépilé au plus une fois** : au total, au plus n empilements et n dépilements, quel que soit leur partage entre les tours. Le `while` peut faire 1000 dépilements à un tour, mais alors ces 1000 éléments ne seront plus jamais dépilés. Total : O(n). C'est une analyse **amortie**.

---

## Étape 5 — Le coût amorti

**Question — un seul minimum :** après un `pop` du minimum, il faudrait retrouver le **nouveau** minimum : relire toute la pile, O(n). La 2e pile garde le minimum **de chaque hauteur** : après un `pop`, le minimum de la hauteur d'en dessous est déjà sur son dessus. Le test pousse 5, 3, 7, 3 : après le retrait d'un des deux 3, le minimum est toujours 3, puis 5.

---

## Étape 6 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié). En écrivant ce projet, la solution comparait d'abord les parenthèses avec `!=` entre deux `Character` : cela marchait, mais seulement grâce au **cache** des petits caractères (le BUG 5 du chapitre 16). C'est corrigé avec `equals`.

---

## Expériences de fin de projet

1. Sans `(long)` (vérifié), le test des 100 000 barres échoue : `expected: <10000000000> but was: <2147432704>`. `100_000 * 100_000` affiche **1410065408** en `int` (le vrai résultat, 10 milliards, a fait le tour). Le test obtient une autre valeur, 2147432704, car l'algorithme calcule les aires de **toutes** les largeurs possibles, toutes débordées différemment, et garde la plus grande de ces valeurs fausses.
2. En versant à chaque fois (vérifié), l'ordre se mélange : le test de la file échoue (`expected: <b> but was: <c>`), tout comme celui du million d'opérations (`expected: <2> but was: <3>`).
