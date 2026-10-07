# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les combattants

<details><summary>Indice 1</summary>

`takeDamage` : mémorise `before = hp`, puis `hp = Math.max(0, hp - damage)`, puis `return before - hp`. `heal` fait de même avec `Math.min(maxHp, …)`.

</details>

<details><summary>Indice 2</summary>

- Copie : dans `Warrior`, `private Warrior(Warrior other) { super(other); }` et `public Warrior copy() { return new Warrior(this); }`. Le type de retour est `Warrior`, pas `Fighter`.
- `act` par défaut : `target = weakest(enemies)`, puis `dealt = target.takeDamage(damageTo(target))`.

</details>

---

## Étape 2 — Le combat

<details><summary>Indice 1</summary>

`all` : `System.arraycopy` de a puis de b. Tri par insertion sur `getSpeed()` avec `<` strict : à égalité, A reste devant.

</details>

<details><summary>Indice 2</summary>

- Pour savoir si `f` est dans A : une boucle `for (Fighter x : a)` qui compare les **références** (`x == f`).
- La boucle des tours : `while (alive(a) && alive(b) && round <= MAX_ROUNDS)`, avec à l'intérieur un `continue` pour les morts et quand une équipe vient de tomber.

</details>

---

## Étape 3 — La revanche avec les copies

<details><summary>Indice 1</summary>

Les copies ont été faites **avant** le premier combat : elles ont gardé les pv de départ. `battle(b2, a2, false)` : B joue le rôle de « a ».

</details>

<details><summary>Indice 2</summary>

Pour la question : quel est le type **déclaré** du résultat de `conan.copy()` quand `conan` est un `Warrior` ? Et quand la variable est un `Fighter` ?

</details>
