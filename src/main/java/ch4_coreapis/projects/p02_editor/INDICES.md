# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Analyser une commande

<details><summary>Indice 1</summary>

`String[] parts = command.split(" ", 2);` puis `String rest = parts.length > 1 ? parts[1] : "";`. Les commandes sans paramètre (`REVERSE`, `UNDO`) n'ont qu'une case.

</details>

<details><summary>Indice 2</summary>

Une méthode `run(String command)` qui rend la ligne à afficher. Chaque `case` peut redécouper `rest` à son tour : `rest.split(" ", 3)` pour `REPLACE`.

</details>

---

## Étape 2 — Les modifications

<details><summary>Indice 1</summary>

Le buffer est **un** champ `static final StringBuilder`. `final` empêche de changer d'objet, pas de modifier son contenu. Les méthodes : `append`, `insert(pos, texte)`, `replace(début, fin, texte)`, `delete(début, fin)`, `deleteCharAt(pos)`.

</details>

<details><summary>Indice 2</summary>

Dans `"Bonjour le grand monde"`, `B` est en position 0, et `g` de `grand` en position 11. `grand` a 5 lettres : il occupe les positions 11 à 15, donc l'intervalle `[11, 16)`.

</details>

---

## Étape 3 — Couper, coller, inverser

<details><summary>Indice 1</summary>

Couper : `clipboard = buffer.substring(start, end);` **puis** `buffer.delete(start, end);`. L'ordre compte.

</details>

<details><summary>Indice 2</summary>

Coller : `if (position > buffer.length())` → message de refus. La position égale à la longueur est **valide** : elle signifie « à la fin ».

</details>

---

## Étape 4 — La pile d'annulation dans un tableau

<details><summary>Indice 1</summary>

Sauvegarder : `history[size++] = buffer.toString();`. Avant, si `size == history.length`, `System.arraycopy(history, 1, history, 0, history.length - 1);` puis `size--`.

</details>

<details><summary>Indice 2</summary>

Restaurer : `buffer.setLength(0); buffer.append(history[--size]);`. Appelle la sauvegarde **avant** chaque modification, et **seulement** si la commande modifie vraiment le texte. Un `PASTE` refusé ne sauve rien.

</details>

---

## Étape 5 — Pool de chaînes, égalité, immutabilité

<details><summary>Indice 1</summary>

Déclare chaque chaîne dans sa variable (`a`, `b`, `c = new String("java")`, `d = c.intern()`…), puis affiche les comparaisons entre parenthèses : `"a == b " + (a == b)`.

</details>

<details><summary>Indice 2</summary>

`StringBuilder s3 = s1.append("y");` : `s3` et `s1` sont le même objet. Calcule le texte de `sb.equals` **avant** cet `append`, sinon les contenus diffèrent.

</details>
