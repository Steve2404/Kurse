# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La classe `Receipt` et son `main`

<details><summary>Indice 1</summary>

La forme varargs s'écrit avec trois points `...`. Ils appartiennent au **type**, pas au nom : regarde la signature de `String.format` dans la Javadoc pour voir où ils se placent.

</details>

<details><summary>Indice 2</summary>

Pour la question : écris chaque variante dans un petit fichier et compile-la. Fais varier une seule chose à la fois : l'ordre `public`/`static`, l'ajout de `final` sur le paramètre, `[]` après le type ou après le nom, le nom du paramètre. Pour l'expérience : compile **puis** lance avec `java` : les deux outils ne vérifient pas la même chose.

</details>

---

## Étape 2 — L'article : une 2e classe dans le même fichier

<details><summary>Indice 1</summary>

Une classe d'article a besoin de trois champs et de deux méthodes : une qui rend le total en centimes, une qui rend la ligne du ticket (un `String`). Écris-la **après** l'accolade fermante de `Receipt`, pas à l'intérieur.

</details>

<details><summary>Indice 2</summary>

Dans le constructeur, quand le paramètre et le champ portent le même nom, le nom seul désigne **toujours le plus proche**, le paramètre. Pour l'expérience : quelle est la valeur par défaut d'un champ `String` jamais affecté ?

</details>

---

## Étape 3 — Lire les arguments

<details><summary>Indice 1</summary>

`args[0]` est le 1er argument, `args[7]` le 8e. Chaque conversion prend **un** `String` et rend un nombre ou un booléen. Construis les deux articles directement avec les valeurs converties.

</details>

<details><summary>Indice 2</summary>

`Integer.valueOf(texte)` rend un `Integer` : enchaîne `.intValue()` sur ce résultat, dans la même expression. Pour la remise, le mot `final` se place devant le type de la variable locale.

</details>

---

## Étape 4 — Les montants

<details><summary>Indice 1</summary>

Pour `3750` : `3750 / 100` donne les euros. Pour un seul chiffre des centimes, combine `/ 10` et `% 10` ; pour l'autre, `% 10` suffit.

</details>

<details><summary>Indice 2</summary>

Une chaîne et un `int` reliés par `+` donnent une chaîne. Donc, une fois que l'expression contient `"."`, chaque `+` suivant **colle** un chiffre au lieu de l'additionner. Pourquoi `cents % 100` seul ne marche-t-il pas pour `905` ?

</details>

---

## Étape 5 — Le ticket : deux text blocks

<details><summary>Indice 1</summary>

Le caractère qui supprime le saut de ligne est le même que celui qui commence une séquence d'échappement. Pour l'en-tête, coupe la ligne de l'adresse après `Brumes ` (l'espace reste avant le caractère).

</details>

<details><summary>Indice 2</summary>

Java retire de chaque ligne la **plus petite** indentation trouvée parmi les lignes de texte **et** la ligne des `"""` fermants. Recule donc les `"""` fermants de 4 colonnes par rapport à la 1re ligne du pied. Attention : si les `"""` sont seuls sur leur ligne, le texte finit par un saut de ligne ; choisis `print` ou `println` en conséquence.

</details>

---

## Étape 6 — Le résumé

<details><summary>Indice 1</summary>

Sous-total = total du 1er article + total du 2e. Remise = sous-total × % / 100, en `int`. Total = sous-total − remise. Le nombre d'articles est la somme des **quantités**.

</details>

<details><summary>Indice 2</summary>

`+` s'évalue de **gauche à droite**. Dans `"Articles : " + 3 + 2`, que vaut la première addition ? Quel est son type ? Mets des parenthèses là où tu veux une vraie addition.

</details>
