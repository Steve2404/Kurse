# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les 8 primitifs : tailles et bornes

<details><summary>Indice 1</summary>

Une constante d'enveloppe s'écrit `Classe.CONSTANTE`, sans parenthèses : ce n'est pas une méthode. Six enveloppes portent le nom du primitif avec une majuscule ; deux ont un nom **complet** en anglais.

</details>

<details><summary>Indice 2</summary>

Pour `float` et `double`, ouvre la Javadoc de `Double.MIN_VALUE` et lis la première phrase : il s'agit de la plus petite valeur **positive**. Le plus petit nombre négatif serait `-Double.MAX_VALUE`.

</details>

---

## Étape 2 — Les valeurs par défaut

<details><summary>Indice 1</summary>

Déclare 8 champs `static` (un par primitif) dans la classe, **hors de `main`**, sans `=`. Un `main` `static` peut les lire directement.

</details>

<details><summary>Indice 2</summary>

Un `char` est un nombre non signé sur 16 bits. L'affecter à un `int` est un **élargissement** : aucune information ne peut se perdre, donc pas besoin de cast. Dans l'autre sens, un `int` peut valoir `-1` ou `70000` : que ferait le `char` de ces valeurs ?

</details>

---

## Étape 3 — Un nombre, cinq écritures

<details><summary>Indice 1</summary>

Les préfixes : `0b` pour le binaire, `0` seul pour l'octal, `0x` pour l'hexa. 255 = 8 bits à 1 ; en octal, regroupe les bits par 3 en partant de la droite ; en hexa, par 4.

</details>

<details><summary>Indice 2</summary>

Pour la 2e ligne : `Integer.toBinaryString`, `toOctalString`, `toHexString`. Pour la 3e : le suffixe `L` est obligatoire dès qu'un littéral dépasse `Integer.MAX_VALUE`. Un `_` n'est permis qu'**entre deux chiffres** : ni au début, ni à la fin, ni à côté du point.

</details>

---

## Étape 4 — Les caractères

<details><summary>Indice 1</summary>

`'A'` : toujours **4 chiffres hexa** après `\u`. Le code décimal de `A` est 4 × 16 + 1.

</details>

<details><summary>Indice 2</summary>

Pour le code de `'B'` : la même technique qu'à l'étape 2, affecter le `char` à un `int`. Pour la question : regarde ce que `javac` sait au moment de compiler. Un littéral `65` est une **constante** dont il connaît la valeur ; une variable `n`, non.

</details>

---

## Étape 5 — Les conversions par les classes enveloppes

<details><summary>Indice 1</summary>

Dans une chaîne, `\"` affiche un guillemet. Le texte de chaque ligne est donc l'appel recopié avec ses guillemets échappés, puis `" = "`, puis l'appel réel.

</details>

<details><summary>Indice 2</summary>

`intValue()` sur un `Double` **tronque** (coupe la partie décimale), il n'arrondit pas. `byteValue()` garde les 8 bits de poids faible : on retire 256 jusqu'à tomber entre -128 et 127. Pour `Long.valueOf("42").longValue() + 1`, mets l'addition entre parenthèses, sinon la concaténation l'emporte.

</details>
