# Projet 5 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 4. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — La moyenne et le piège du cast

<details><summary>Indice 1</summary>

Les notes sont des `double` (`Double.parseDouble`), les coefficients des `int`. `double * int` est un `double`. La somme des points est donc un `double`, et la somme des coefficients reste un `int`.

</details>

<details><summary>Indice 2</summary>

Un cast porte sur ce qui le **suit immédiatement** : un nom, un littéral ou une parenthèse. Dans `(double) whole / coefficients`, le cast porte sur `whole` seul. Dans `(double) (whole / coefficients)`, il porte sur le résultat **déjà tronqué**.

</details>

---

## Étape 2 — Pénalité, options et bonus

<details><summary>Indice 1</summary>

`Integer.parseInt(args[7], 2)` lit `"101"` en binaire, soit 5. Les masques : `DELEGATE = 1`, `SPORT = 1 << 1`, `LATIN = 1 << 2`. Projet 2, étape 1, pour le test de bit.

</details>

<details><summary>Indice 2</summary>

Pénalité : `absences > 3 ? (absences - 3) * 0.25 : 0`. Puis `average -= penalty;`, `average += (options & LATIN) != 0 ? 0.5 : 0;`, et de même pour le délégué. Le plafond est un ternaire.

</details>

---

## Étape 3 — Arrondi, mention, écart, lettre

<details><summary>Indice 1</summary>

L'arrondi : `(int) (value * 10 + 0.5) / 10.0`. La mention : 5 issues, donc 4 conditions, de la plus haute (`>= 16`) à la plus basse.

</details>

<details><summary>Indice 2</summary>

- Le rang de la lettre : `(int) ((20 - moyenne) / 4)`, ajouté à `'A'`, puis le cast `(char)` sur le tout.
- L'écart : arrondis `moyenne - 11.5` avec la même méthode, puis `(gap >= 0 ? "+" : "")`.

</details>
