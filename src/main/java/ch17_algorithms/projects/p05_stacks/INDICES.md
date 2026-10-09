# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les parenthèses

<details><summary>Indice 1</summary>

Une `Map<Character, Character>` de chaque fermante vers son ouvrante (`')'` → `'('`…) évite trois `if`. Une ouvrante se pose sur la pile ; une fermante doit trouver **son** ouvrante sur le dessus.

</details>

<details><summary>Indice 2</summary>

Compare deux `Character` avec `equals`, pas avec `!=` : ce sont des objets (chapitre 16, projet 7, BUG 5).

</details>

---

## Étape 2 — La notation polonaise inverse

<details><summary>Indice 1</summary>

`expr.trim().split("\\s+")` découpe aux espaces. Un nombre se reconnaît avec `token.matches("\\d+")`.

</details>

<details><summary>Indice 2</summary>

`long right = stack.pop(); long left = stack.pop();` **dans cet ordre**, puis un `switch` sur l'opérateur.

</details>

---

## Étape 3 — La gare de triage

<details><summary>Indice 1</summary>

Lis la chaîne caractère par caractère avec un indice `i`. Pour un chiffre, avance tant que les caractères sont des chiffres, et ajoute le morceau entier (`substring(debut, i)`) à la sortie.

</details>

<details><summary>Indice 2</summary>

Une `Map<String, Integer>` des priorités. Pour un opérateur : `while (pile non vide && dessus != "(" && priorité(dessus) >= priorité(op))`, fais partir le dessus vers la sortie ; puis pose `op`.

</details>

---

## Étape 4 — La pile monotone

<details><summary>Indice 1</summary>

La pile contient des **indices** (pas des températures) : pour un jour dépilé `avant`, la réponse est `jour - avant`.

</details>

<details><summary>Indice 2</summary>

Pour le rectangle : parcours `i` de 0 à `heights.length` **inclus**, avec `h = (i == heights.length) ? 0 : heights[i]`. Pour une barre dépilée, la largeur vaut `i - gauche - 1`, où `gauche` est l'indice resté sur le dessus de la pile (ou −1 si elle est vide).

</details>

---

## Étape 5 — Le coût amorti

<details><summary>Indice 1</summary>

Pour `MinStack` : à chaque `push(v)`, pose aussi sur la 2e pile `min(v, minimum actuel)` ; à chaque `pop`, retire des deux piles.

</details>

<details><summary>Indice 2</summary>

Pour la file : une méthode privée `refill()` qui verse `in` dans `out` **seulement si `out` est vide** ; `poll` et `peek` l'appellent d'abord.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quelle parenthèse restée ouverte, quel ordre d'opérandes, quelle égalité de priorité, quelle égalité de température, quel grand nombre n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. `balanced` oublie de vérifier que la pile est vide à la fin.
2. `balanced` accepte n'importe quelle fermante pour n'importe quelle ouvrante.
3. `evalRpn` inverse les deux opérandes.
4. `evalRpn` accepte qu'il reste plusieurs nombres à la fin.
5. La gare de triage ne fait partir que les opérateurs de priorité **strictement** supérieure.
6. Toutes les opérations ont la même priorité.
7. Une fermante sans ouvrante n'est plus refusée.
8. Un jour aussi chaud compte comme « plus chaud ».
9. Le rectangle oublie la barre fictive de la fin (la pile n'est pas vidée).
10. L'aire du rectangle est calculée en `int` (elle déborde).
11. `MinStack` ne retient pas le minimum à chaque hauteur.
12. La file verse à chaque fois, même quand la sortie n'est pas vide.
13. La taille de la file oublie la pile de sortie.

</details>
