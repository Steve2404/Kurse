# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Couleurs et rangs

<details><summary>Indice 1</summary>

Un enum avec des champs : les constantes avec leurs arguments, puis **`;`**, puis les champs `private final` et un constructeur (implicitement `private`) qui les affecte.

</details>

<details><summary>Indice 2</summary>

`of(char c)` : `for (Suit s : values()) if (s.symbol == c) return s;`, puis `return null;`. `ordinal()` part de 0, et `compareTo` rend la **différence** des `ordinal()`.

</details>

---

## Étape 2 — Les catégories et les mains

<details><summary>Indice 1</summary>

Une constante avec un corps : `PAIR("paire") { @Override boolean matches(…) { return groups[0] == 2; } },`. La méthode abstraite se déclare après les constantes : `abstract boolean matches(…);`.

</details>

<details><summary>Indice 2</summary>

- Groupes : un tableau `counts[15]` indexé par valeur. Puis, pour `size` de 4 à 1, on ajoute `size` pour chaque valeur qui apparaît `size` fois.
- Le constructeur compact : `public Hand { cards = cards.clone(); key = key.clone(); }`, sans parenthèses après `Hand`.

</details>

---

## Étape 3 — Mélanger et distribuer

<details><summary>Indice 1</summary>

Un champ `private static long seed = Data.SEED;` modifié à chaque `nextInt`. Le jeu : deux for-each imbriqués, `Suit.values()` à l'extérieur et `Rank.values()` à l'intérieur.

</details>

<details><summary>Indice 2</summary>

`for (int i = 51; i > 0; i--) { int j = nextInt(i + 1); échange deck[i] et deck[j]; }`. Le joueur p reçoit `deck[c * PLAYERS + p]`.

</details>

---

## Étape 4 — Texas hold'em

<details><summary>Indice 1</summary>

Deux boucles `a < b` choisissent les 2 cartes à exclure. Une 3e boucle recopie les 5 autres dans `five`.

</details>

<details><summary>Indice 2</summary>

Les 7 cartes : `System.arraycopy(board, 0, seven, 0, 5)`, puis les 2 cartes du joueur aux positions 5 et 6.

</details>
