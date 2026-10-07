# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Player.java`](Player.java), [`Interval.java`](Interval.java) et [`Ranking.java`](Ranking.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Ordre naturel et comparateurs

**Le code :** [`Player.java`](Player.java), et le début du `main` jusqu'aux rangs.

**`Comparable` contre `Comparator` :**
- `Comparable` donne **un** ordre naturel, écrit **dans** la classe (`compareTo`). Il est utilisé par `Collections.sort(liste)`, `TreeSet` et `TreeMap` sans argument.
- `Comparator` donne **autant d'ordres qu'on veut**, écrits **à l'extérieur**.

**`board` se lit de gauche à droite :** score décroissant, puis âge, puis nom (l'ordre naturel). Hugo, Ines et Bob ont tous 1500. Hugo (25 ans) passe avant Ines (31), qui passe avant Bob (37).

**`reversed()` porte sur tout ce qui précède :** `comparingInt(score).reversed()` inverse seulement le score, puisqu'il est appelé **avant** les `thenComparing`. Ajouté à la fin, il inverserait **toute** la chaîne.

**`nullsLast` :** sans lui, comparer un bonus `null` lèverait une `NullPointerException`.

**Les rangs :**
- « compétition » donne 1, 1, 1, **4** : les ex æquo « consomment » des places ;
- « dense » donne 1, 1, 1, **2** : pas de trou.

---

## Étape 2 — Le piège et la navigation

**Le code :** le `TreeSet` par score, le `NavigableMap` et le `NavigableSet`.

**Question — pourquoi seulement 4 joueurs ?** Un `TreeSet` n'utilise **que son comparateur** pour décider si deux éléments sont égaux. Deux joueurs de même score donnent `compare(…) == 0`, donc ils sont « égaux », et le second est **refusé**. Il reste **un** joueur par score distinct : 1500, 1200, 980 et 870. Ce sont les **premiers ajoutés** dans l'ordre de `players` (déjà trié par `board`) : Hugo, Zoe, Adam et Emma.

**La règle :** un comparateur utilisé dans un `TreeSet` ou une `TreeMap` doit être **cohérent avec `equals`**. Il ne doit rendre 0 que pour des éléments vraiment identiques. Sinon, ajoute un critère de départage (le nom, par exemple).

**La navigation :**

| Méthode | Sens | Exemple |
|---|---|---|
| `floorKey(1300)` | plus grande ≤ | 1200 |
| `ceilingKey(1300)` | plus petite ≥ | 1500 |
| `lowerKey(1200)` | plus grande **<** | 980 |
| `higherKey(1500)` | plus petite **>** | `null` (aucune) |

**Les vues :** `headMap(1200)` contient les clés < 1200, `tailMap(1200)` les clés ≥ 1200, et `subMap(900, 1300)` l'intervalle [900, 1300[. Ce sont des **vues** : modifier la map les modifie.

**`pollFirst` et l'ordre d'évaluation :** la concaténation s'évalue de gauche à droite. `ages` est donc affiché **avant** `pollFirst()` (avec 25), et le second `ages` **après** (sans 25).

---

## Étape 3 — Intervalles et médiane

**Le code :** [`Interval.java`](Interval.java) et la fin du `main`.

**La fusion :** trier par **début** garantit qu'un intervalle ne peut chevaucher que le **dernier** fusionné. Ainsi, `[1,3]` et `[2,6]` donnent `[1,6]`, puis `[5,7]` donne `[1,7]`.

**Question — pourquoi trier par fin pour le planning ?** Choisir l'intervalle qui **finit le plus tôt** laisse le plus de place aux suivants : c'est le glouton optimal. Trier par début peut choisir un intervalle très long qui bloque tout. Vérifié sur `[1,10] [2,3] [4,5]` :
- par début, on garde `[[1, 10]]` (1 intervalle) ;
- par fin, on garde `[[2, 3], [4, 5]]` (2).

Sur les données du projet, les deux tris donnent 4 par chance : il faut tester plusieurs cas.

**La médiane avec deux tas :**
- `low` est un tas **max**, qui contient la moitié basse : son sommet est le plus grand des petits.
- `high` est un tas **min**, qui contient la moitié haute : son sommet est le plus petit des grands.
- En gardant les tailles équilibrées, la médiane est toujours au sommet, en **O(log n)** par valeur. Retrier à chaque valeur coûterait O(n log n).

`Collections.reverseOrder()` inverse l'ordre naturel. C'est ce qui transforme la `PriorityQueue` (un tas min par défaut) en tas max.
