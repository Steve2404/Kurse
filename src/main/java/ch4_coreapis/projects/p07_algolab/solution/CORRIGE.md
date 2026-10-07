# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet, avec une méthode par étape, est dans [`AlgoLab.java`](AlgoLab.java) : `sorting`, `searching`, `sums`, `sieve`, `matrices`, `pascal` et `maze`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Deux tris à la main, puis dédoublonner

**Le code :** la méthode `sorting()` de la solution. Les points clés :

```java
while (j >= 0 && a[j] > key) {      // insertion : > strict
    a[j + 1] = a[j];
    j--;
    shifts++;
}
a[j + 1] = key;

if (min != i) { … swaps++; }       // sélection : seulement les vrais échanges

int k = 1;                          // dédoublonnage en place
for (int i = 1; i < a.length; i++) {
    if (a[i] != a[k - 1]) {
        a[k++] = a[i];
    }
}
```

**Question — pourquoi `>` rend le tri par insertion stable ?** Avec `>`, un élément **s'arrête** devant un élément égal : il ne le dépasse jamais. Deux valeurs égales gardent donc leur **ordre d'origine** (c'est la définition de la stabilité). Avec `>=`, il passerait **devant** les égaux, ce qui inverse leur ordre et coûte des décalages inutiles. Vérifié : avec `>=`, il y a **26** décalages au lieu de **24**, un de plus pour chaque paire de doublons (les deux 3 et les deux 8). Sur des `int`, on ne « voit » pas la différence d'ordre, mais elle compte dès qu'on trie des objets par une seule de leurs propriétés.

**Question — combien de comparaisons pour le tri par sélection ?** **n(n − 1)/2**, toujours, même sur un tableau déjà trié : chercher le minimum du reste demande de **tout** parcourir. Vérifié : 45 comparaisons pour 10 éléments déjà triés. L'insertion, elle, ne fait que n − 1 comparaisons sur un tableau trié.

**`Arrays.copyOf` contre `clone()` :** les deux créent une copie **superficielle** (*shallow*) d'un tableau. Pour un `int[]`, c'est une vraie copie indépendante.

**Le dédoublonnage :** `k` est à la fois le nombre de valeurs uniques et la case où écrire la suivante. Comme le tableau est trié, un doublon est toujours **égal au dernier gardé** (`a[k - 1]`).

---

## Étape 2 — Deux pointeurs et borne inférieure

**Le code :** la méthode `searching()` de la solution.

**Les deux pointeurs, sur `[1, 3, 3, 8, 8, 15, 17, 23, 29, 42]` avec une cible de 32 :**

| Étape | left | right | somme | action |
|---|---|---|---|---|
| 1 | 1 | 42 | 43 | trop grand, `right--` |
| 2 | 1 | 29 | 30 | trop petit, `left++` |
| 3 | 3 | 29 | **32** | paire `3+29`, saute les deux 3 et le 29 |
| 4 | 8 | 23 | 31 | trop petit, `left++` |
| 5 | 8 | 23 | 31 | trop petit, `left++` |
| 6 | 15 | 23 | 38 | trop grand, `right--` |
| 7 | 15 | 17 | **32** | paire `15+17` |

**7 étapes**, en O(n) au lieu de O(n²).

**La borne inférieure :**
- 8 donne **3**, le **premier** 8.
- 9 donne **5** (premier ≥ 9, le 15).
- 0 donne **0**.
- 50 donne **10**, la longueur, car aucune valeur n'est ≥ 50.

L'intervalle `[low, high[` et `high = mid` (pas `mid - 1`) : quand `sorted[mid] >= q`, `mid` **peut** être la réponse, donc on le garde.

**Question — pourquoi `(low + high) / 2` peut déborder ?** Si `low + high` dépasse `Integer.MAX_VALUE` (environ 2,1 milliards), la somme devient **négative**. Vérifié avec 1,5 et 2 milliards : `/ 2` donne **−397483648**, alors que `>>> 1` donne 1750000000. `>>>` relit le bit de signe comme un bit ordinaire, et le résultat redevient juste.

---

## Étape 3 — Sommes préfixes, fenêtre glissante, Kadane

**Le code :** la méthode `sums()` de la solution.

**Les sommes préfixes :** `NUMBERS` vaut `29 3 17 8 3 42 15 8 1 23`, donc `prefix` vaut `0 29 32 49 57 60 102 117 125 126 149`. Pour l'intervalle `[0,4]`, on calcule `prefix[5] - prefix[0]` = 60 − 0 = **60**.

**La fenêtre de 3 :** les sommes successives, vérifiées, sont 49, 28, 28, 53, 60, **65** (42 + 15 + 8, à partir de l'indice 5), 24 et 32. Avec `>` strict, en cas d'égalité, c'est la **première** fenêtre qui reste.

**Kadane sur `-2 1 -3 4 -1 2 1 -5 4` :**

| i | p[i] | current | décision | max |
|---|---|---|---|---|
| 0 | −2 | −2 | — | −2 |
| 1 | 1 | 1 | recommence (−2 + 1 < 1) | 1 |
| 2 | −3 | −2 | prolonge | 1 |
| 3 | 4 | 4 | recommence | 4 |
| 4 | −1 | 3 | prolonge | 4 |
| 5 | 2 | 5 | prolonge | 5 |
| 6 | 1 | **6** | prolonge | **6** (jours 3 à 6) |
| 7 | −5 | 1 | prolonge | 6 |
| 8 | 4 | 5 | prolonge | 6 |

**Question — les complexités :**

| Méthode | Naïf | Ici |
|---|---|---|
| somme d'un intervalle | O(longueur) par requête | O(n) une fois, puis **O(1)** par requête |
| fenêtre glissante | O(n × k) | **O(n)** |
| Kadane | O(n²) (tous les intervalles) | **O(n)** |

---

## Étape 4 — Le crible d'Ératosthène

**Le code :** la méthode `sieve()` de la solution.

**Question — pourquoi partir de `i * i` ?** Un multiple `i × m` avec `m < i` a déjà été barré quand on a traité `m` (ou un diviseur de `m`). Par exemple, pour i = 5 : 10 = 5 × 2 (barré par 2), 15 = 5 × 3 (barré par 3), 20 = 5 × 4 (barré par 2). Le premier multiple **nouveau** est 25 = 5 × 5.

**Pourquoi s'arrêter quand `i * i` dépasse la limite ?** C'est le même raisonnement : un nombre composé ≤ 60 a un facteur ≤ √60 ≈ 7.7. Après 7, tous les composés sont déjà barrés.

**Les jumeaux jusqu'à 60 :** (3,5), (5,7), (11,13), (17,19), (29,31), (41,43), soit **6 paires**.

---

## Étape 5 — Matrices

**Le code :** les méthodes `matrices()` et `transpose()` de la solution.

**La rotation :** `[r][c]` part en `[c][rows - 1 - r]`. Par exemple, le 1 (0,0) va en (0,2) et le 9 (2,0) va en (0,0). La première ligne de la rotation est donc la première **colonne** lue de bas en haut : `[9, 5, 1]`.

**Expérience — la spirale sans les deux tests, sur une seule ligne :** les éléments sont lus **deux fois**. Vérifié avec `{{1, 2, 3, 4}}` : on obtient `1 2 3 4 3 2 1`. Après la ligne du haut, `top` dépasse déjà `bottom`. Sans le test, la boucle « ligne du bas » relit la même ligne à l'envers.

**La diagonale :** 1 + 6 + 11 = **18**, sur `min(rows, cols)` = 3 cases.

**Question — pourquoi `Arrays.equals` ne suffit pas ?** Il compare les cases d'un seul niveau. Or, ici, ces cases sont des **références** de lignes (`int[]`), et des tableaux distincts ne sont jamais égaux pour lui. Vérifié : `Arrays.equals({{1, 2}}, {{1, 2}})` vaut `false`, alors que `deepEquals` vaut `true`.

---

## Étape 6 — Le triangle de Pascal

**Le code :** la méthode `pascal()` de la solution.

**Le centrage :** la dernière ligne `[1, 6, 15, 20, 15, 6, 1]` fait 24 caractères (vérifié). `[1]` en fait 3, donc (24 − 3) / 2 = 10 espaces. La division entière arrondit vers le bas quand l'écart est impair.

**Question — pourquoi `(int) Math.pow(2, 6)` ?** `Math.pow` rend **toujours un `double`** (`64.0`). La comparaison `sum == Math.pow(2, 6)` compilerait aussi, car `sum` serait promu en `double`. Vérifié : elle vaut `true`. Le cast rend explicite qu'on compare deux **entiers**. Il serait obligatoire pour ranger le résultat dans un `int` : `int p = Math.pow(2, 6);` ne compile pas. Les puissances de 2 sont exactes en `double`, donc le cast ne perd rien ici.

**La propriété :** la somme de la ligne n vaut 2ⁿ (le nombre de sous-ensembles d'un ensemble à n éléments).

---

## Étape 7 — Le plus court chemin dans un labyrinthe (BFS)

**Le code :** la méthode `maze()` de la solution.

**Question — pourquoi le BFS trouve-t-il le plus court chemin ?** La file traite les cases **par distance croissante** : d'abord le départ (0), puis toutes les cases à 1 pas, puis toutes celles à 2 pas… Une case reçoit sa distance la **première** fois qu'on l'atteint, donc par le plus court chemin possible. Aucune case à distance d ne peut être traitée avant une case à distance d − 1. Un parcours en **profondeur** (DFS) trouverait un chemin, pas forcément le plus court.

**Question — marquer au défilement plutôt qu'à l'enfilage :** la même case peut alors être **enfilée plusieurs fois**, par plusieurs voisins, avant d'être traitée.
- Dans ce labyrinthe étroit, on passe de 45 à **47** enfilages, pour les mêmes 45 cases explorées (vérifié).
- Sur une grille ouverte de 10 × 10, on passe de 100 à **181** enfilages pour 100 cases (vérifié).

La file de taille `rows * cols` peut alors **déborder** (`ArrayIndexOutOfBoundsException`), et le travail explose sur de grandes grilles. Marquer à l'enfilage garantit qu'une case entre **une seule fois** dans la file.

**`toCharArray()` :** il donne une **copie** modifiable de la ligne. Un `String` étant immuable, on ne peut pas y poser les `*` directement. `new String(char[])` refait un `String` pour l'afficher.
