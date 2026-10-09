# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le premier cycle, puis le deuxième

<details><summary>Indice 1</summary>

Au cycle 2, le code le plus simple qui garde `toRoman(1)` vert et rend `toRoman(2)` vert peut être un simple `if (n == 2) return "II";`. C'est permis : le cycle 3 te forcera à généraliser.

</details>

<details><summary>Indice 2</summary>

Au cycle 3, la généralisation : `"I".repeat(n)` (chapitre 4), ou une boucle qui ajoute `n` fois `"I"`.

</details>

---

## Étape 2 — Le 5 et le 4

<details><summary>Indice 1</summary>

Le glouton : pour 14, la plus grande valeur qui rentre est 10 (`X`), il reste 4 ; la plus grande qui rentre dans 4 est 4 (`IV`) ; il reste 0. Résultat : `XIV`.

</details>

<details><summary>Indice 2</summary>

Deux tableaux de même longueur : `{10, 9, 5, 4, 1}` et `{"X", "IX", "V", "IV", "I"}` pour commencer. Une boucle `for` sur les indices, et dedans un `while (reste >= valeurs[i])`.

</details>

---

## Étape 3 — Jusqu'à 3999

<details><summary>Indice 1</summary>

Chaque nouveau cas ne demande qu'une nouvelle paire dans les tableaux, **à sa place** dans l'ordre décroissant : 40 entre 50 et 10, 900 entre 1000 et 500…

</details>

<details><summary>Indice 2</summary>

Le contrôle des limites se fait en tout premier : `if (n < 1 || n > 3999) throw new IllegalArgumentException("hors limites : " + n);`.

</details>

---

## Étape 4 — Dans l'autre sens

<details><summary>Indice 1</summary>

Une petite méthode privée qui rend la valeur d'**un** caractère avec un `switch` (`'I'` → 1, `'V'` → 5…). Elle servira aussi à refuser un caractère inconnu, à l'étape 5.

</details>

<details><summary>Indice 2</summary>

Pour chaque indice `i` : la valeur du caractère, et celle du suivant (`i + 1 < s.length() ? … : 0`). Si la valeur est plus petite que la suivante, on la retire du total, sinon on l'ajoute.

</details>

---

## Étape 5 — Refuser ce qui est mal écrit

<details><summary>Indice 1</summary>

Le `default` du `switch` de l'étape 4 lance l'exception « chiffre romain invalide ». Il te faut donc la chaîne entière dans cette méthode : passe-la en 2e paramètre.

</details>

<details><summary>Indice 2</summary>

`if (total < 1 || total > 3999 || !toRoman(total).equals(s))` : l'ordre compte. `||` s'arrête au premier `true` (chapitre 2), donc `toRoman` n'est jamais appelé avec 0 ou 4000.

</details>

---

## Étape 6 — L'aller-retour

<details><summary>Indice 1</summary>

`IntStream.rangeClosed(1, 3999).forEach(n -> assertEquals(n, …))` : la vérification est **dans** la lambda du `forEach`.

</details>

<details><summary>Indice 2</summary>

Le tout va dans le 2e argument de `assertTimeout(Duration.ofSeconds(1), () -> …)`. Import : `java.time.Duration`.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Si un mutant survit, cherche quel nombre ou quelle chaîne donnerait un autre résultat avec ce bug, et ajoute-le à tes tableaux.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. 900 s'écrit `DCCCC` au lieu de `CM`.
2. 4 s'écrit `IIII` au lieu de `IV`.
3. 4000 est accepté par `toRoman`.
4. 0 est accepté par `toRoman`.
5. La vérification « `toRoman(total)` redonne la chaîne » disparaît.
6. Deux symboles égaux à la suite se soustraient (`<=` au lieu de `<`).
7. Le dernier caractère n'est plus comparé à son voisin : l'avant-dernier ne se soustrait jamais.
8. Le message « hors limites » ne contient plus le nombre.
9. Le total 0 n'est plus refusé par `fromRoman`.
10. `L` vaut 40 au lieu de 50.
11. Chaque symbole n'est utilisé qu'une fois au plus (`if` au lieu de `while`).

</details>
