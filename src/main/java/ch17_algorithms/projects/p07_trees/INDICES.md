# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Ajouter et chercher

<details><summary>Indice 1</summary>

`private static final class Node<K>` avec `final K key; Node<K> left; Node<K> right;`. Pour `contains` : `n = cmp < 0 ? n.left : n.right;` jusqu'à trouver ou tomber sur `null`.

</details>

<details><summary>Indice 2</summary>

Pour `add` : traite d'abord l'arbre vide (la clé devient la racine). Puis descends ; quand l'enfant du côté choisi est `null`, crée le nœud **là**, augmente `size`, et rends `true`.

</details>

---

## Étape 2 — Minimum, maximum, plancher, plafond

<details><summary>Indice 1</summary>

`min` : depuis la racine, `while (n.left != null) n = n.left;`.

</details>

<details><summary>Indice 2</summary>

Pour `floor` : une variable `best = null`. Égal : rends la clé. Plus petit que le nœud : à gauche (le nœud est trop grand). Plus grand : `best = n.key`, puis à droite.

</details>

---

## Étape 3 — Les parcours

<details><summary>Indice 1</summary>

Une méthode privée récursive par parcours, qui remplit une liste : `inOrder(n.left, out); out.add(n.key); inOrder(n.right, out);` (et l'ordre change pour le préfixe).

</details>

<details><summary>Indice 2</summary>

Par niveaux : `Deque<Node<K>> file = new ArrayDeque<>();`, `file.add(root)` si la racine existe, puis `while (!file.isEmpty())` : `poll()`, ajoute la clé, puis `add` des enfants non nuls.

</details>

---

## Étape 4 — Supprimer

<details><summary>Indice 1</summary>

`root = remove(root, key);` et, dans la méthode récursive : `null` → `null` ; plus petit → `n.left = remove(n.left, key); return n;` ; plus grand → pareil à droite.

</details>

<details><summary>Indice 2</summary>

Deux enfants : trouve le successeur (`n.right`, puis tout à gauche) ; comme la clé d'un nœud est `final`, crée un **nouveau** nœud avec la clé du successeur, la gauche de `n`, et comme droite `remove(n.right, cléDuSuccesseur)`. La taille ne diminue qu'une fois : dans le retrait du successeur.

</details>

---

## Étape 5 — Élaguer

<details><summary>Indice 1</summary>

`rangeCount` récursif : `null` → 0 ; nœud `< lo` → seulement la droite ; nœud `> hi` → seulement la gauche ; sinon `1 + gauche + droite`.

</details>

<details><summary>Indice 2</summary>

Pour l'ancêtre : vérifie d'abord que les deux clés sont présentes avec `contains`, puis une boucle depuis la racine.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel cas de suppression, quel parcours exact, quelle borne d'intervalle, quel arbre vide n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Un doublon refusé augmente quand même la taille.
2. La hauteur ne regarde que le côté gauche.
3. `floor` ne retient jamais de candidat.
4. `ceiling` retient le mauvais candidat (en allant à droite).
5. Le parcours préfixe devient infixe.
6. Le parcours par niveaux utilise une pile au lieu d'une file.
7. Retirer un nœud sans enfant gauche perd son sous-arbre droit.
8. Le « successeur » est cherché tout à droite au lieu de tout à gauche.
9. Le nœud qui remplace perd le sous-arbre gauche.
10. `rangeCount` exclut la borne basse.
11. L'ancêtre part à droite dès qu'**une** des clés est plus grande.
12. L'ancêtre ne vérifie que la présence de la 1re clé.
13. `min` sur un arbre vide rend `null` au lieu de lancer l'exception.

</details>
