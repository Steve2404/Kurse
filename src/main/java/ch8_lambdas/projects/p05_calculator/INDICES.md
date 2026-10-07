# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les jetons et la table des fonctions

<details><summary>Indice 1</summary>

`classify` : une chaîne `if / else if` sur le premier caractère. `-3` est un nombre (un `-` suivi d'autre chose), alors que `-` seul est un opérateur.

</details>

<details><summary>Indice 2</summary>

`Registry` : deux paires de tableaux parallèles (noms et fonctions), et deux méthodes d'enregistrement `unary(name, f)` et `binary(name, f)`. Ce sont des surcharges, puisque les types des paramètres diffèrent.

</details>

---

## Étape 2 — La gare de triage et l'évaluation

<details><summary>Indice 1</summary>

Un `switch` en flèche sur `t.kind()`, avec `case NUMBER, VAR -> out[n++] = t;` et `case FUNCTION, LEFT -> stack[top++] = t;`.

</details>

<details><summary>Indice 2</summary>

- La condition pour dépiler un opérateur : le sommet est un opérateur **et** sa priorité est plus grande, **ou** égale si le nouveau n'est pas `^`.
- Pour `)` : dépile jusqu'à `(`, fais `top--` pour retirer `(`, puis sors la fonction si le sommet en est une.

</details>

---

## Étape 3 — Les références, une par une

<details><summary>Indice 1</summary>

Les 4 sortes : `Classe::methodeStatic`, `objet::methode`, `Classe::methodeDInstance` (l'objet est le 1er paramètre) et `Classe::new`.

</details>

<details><summary>Indice 2</summary>

Pour chaque référence, écris la lambda à côté. `Token::new` avec `BiFunction<String, Token.Kind, Token>` donne `(t, k) -> new Token(t, k)`.

</details>
