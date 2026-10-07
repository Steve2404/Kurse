# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Ce qui change, ce qui ne change pas

<details><summary>Indice 1</summary>

La règle unique : Java **copie** chaque argument dans le paramètre. Pour un primitif, il copie la valeur. Pour un objet, il copie la **référence** (la flèche), pas l'objet.

</details>

<details><summary>Indice 2</summary>

Deux questions pour chaque méthode : modifie-t-elle l'**objet** pointé (visible dehors), ou réaffecte-t-elle seulement le **paramètre** (invisible) ? `s += "!"` et `n++` réaffectent le paramètre, car `String` et `Integer` sont immuables.

</details>

---

## Étape 2 — L'autoboxing

<details><summary>Indice 1</summary>

`Integer x = 127;` appelle en réalité `Integer.valueOf(127)`, qui garde un **cache** de −128 à 127. `==` compare les références des objets.

</details>

<details><summary>Indice 2</summary>

`Double d = 5;` demanderait deux conversions à la fois : `int` vers `double` (élargissement), **puis** emballage en `Double`. Java n'en fait qu'une seule automatiquement dans une affectation.

</details>

---

## Étape 3 — Les permutations

<details><summary>Indice 1</summary>

`permute(c, k, out)` : si `k == c.length`, ajoute `c` à `out` et compte. Sinon, `for (int i = k; …)` : `swap(c, k, i)`, puis `permute(c, k + 1, out)`, puis `swap(c, k, i)` pour défaire.

</details>

<details><summary>Indice 2</summary>

- `next(c)` : `i` part de `length - 2` et recule tant que `c[i] >= c[i + 1]`. Si `i < 0`, c'est la dernière permutation. Sinon, `j` part de la fin et recule tant que `c[j] <= c[i]`. Échange, puis inverse `c[i+1..fin]`.
- `rank` : pour `CADB`, C a 2 lettres plus petites après lui (A, B), donc 2 × 3! = 12…

</details>

---

## Étape 4 — Le calendrier et le classement

<details><summary>Indice 1</summary>

Pour 6 équipes, il y a 5 journées et 3 matchs par journée. Après chaque journée : `last = ring[t - 1]`, puis `System.arraycopy(ring, 1, ring, 2, t - 2)`, puis `ring[1] = last`.

</details>

<details><summary>Indice 2</summary>

- Classement : `order` contient les indices 0 à 5. Tri par insertion où « `key` passe devant `order[j]` » si `before(key, order[j], …)`.
- `before` compare les points, puis la différence de buts, puis le nom.

</details>
