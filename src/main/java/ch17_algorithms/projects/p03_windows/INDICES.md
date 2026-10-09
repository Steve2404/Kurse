# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Deux pointeurs aux deux bouts

<details><summary>Indice 1</summary>

`i = 0`, `j = sorted.length - 1`, et `while (i < j)` : quand les deux doigts se croisent, toutes les paires ont été éliminées.

</details>

<details><summary>Indice 2</summary>

`long sum = (long) sorted[i] + sorted[j];` : le cast s'applique à `sorted[i]` **avant** l'addition, qui se fait alors en `long`.

</details>

---

## Étape 2 — Deux pointeurs dans le même sens

<details><summary>Indice 1</summary>

La première case est toujours gardée : `kept` démarre à 1, et `i` aussi.

</details>

<details><summary>Indice 2</summary>

Compare `sorted[i]` à `sorted[kept - 1]`, la dernière valeur **gardée** ; si elles diffèrent, `sorted[kept++] = sorted[i]`.

</details>

---

## Étape 3 — La fenêtre de taille fixe

<details><summary>Indice 1</summary>

D'abord la somme des `k` premières cases ; c'est aussi la meilleure jusqu'ici. Puis, pour `i` de `k` à la fin : la case `i` entre, la case `i - k` sort.

</details>

<details><summary>Indice 2</summary>

Pour calculer les cas de test à la main : avec `k = 2` et `{2, 9, -1, 5, 8, -6, 3}`, les fenêtres valent 11, 8, 4, 13, 2, −3.

</details>

---

## Étape 4 — La fenêtre de taille variable

<details><summary>Indice 1</summary>

`Integer previous = lastSeen.put(c, right);` rend l'ancienne position (ou `null`) et enregistre la nouvelle en une seule ligne. Si `previous` n'est pas `null`, la gauche passe à `Math.max(left, previous + 1)`.

</details>

<details><summary>Indice 2</summary>

Pour `shortestAtLeast` : ajoute `positive[right]` à la somme, puis `while (sum >= target)` : note `right - left + 1`, retire `positive[left]` et avance `left`.

</details>

---

## Étape 5 — Les intervalles

<details><summary>Indice 1</summary>

Pour ne pas modifier la liste reçue (qui peut être un `List.of`), copie-la dans une `ArrayList` avant de la trier : `sorted.sort(Comparator.comparingInt(Interval::start))`.

</details>

<details><summary>Indice 2</summary>

Pour `minRooms` : deux tableaux `starts` et `ends`, triés. Pour chaque début (dans l'ordre), libère d'abord toutes les salles dont la fin est `<=` ce début, puis occupe une salle, et retiens le maximum.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel débordement, quelle limite (`k` trop grand, des créneaux qui se touchent, des réunions bout à bout), quel piège (`"abba"`) n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. La somme de `pairWithSum` est calculée en `int` (elle déborde).
2. `pairWithSum` accepte `i == j` (une case avec elle-même).
3. `removeDuplicates` écrit un cran trop tard, la valeur d'avant.
4. Une fenêtre plus grande que le tableau n'est plus refusée.
5. La fenêtre fixe retire la mauvaise case (celle d'après).
6. La gauche de `longestUniqueRun` peut reculer.
7. `shortestAtLeast` n'avance la gauche qu'une fois par tour (`if` au lieu de `while`).
8. Deux créneaux qui se touchent ne fusionnent plus.
9. La fusion garde la fin du dernier créneau, même s'il est inclus dans le précédent.
10. Le glouton trie par début au lieu de fin.
11. Une réunion qui finit à 10 h ne libère pas la salle pour une réunion de 10 h.
12. Un créneau vide `[5, 5[` est accepté.

</details>
