# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Découper et compter

<details><summary>Indice 1</summary>

`replace(char, char)` s'enchaîne : `text.replace('.', ' ').replace(',', ' ')…`. Dans une expression régulière, `+` signifie « une fois ou plus » : `" +"` est donc une espace, une fois ou plus.

</details>

<details><summary>Indice 2</summary>

- Pour les voyelles, passe chaque caractère en minuscule avec `Character.toLowerCase(c)` avant le `indexOf`.
- Le plus long et le plus court : un for-each sur les mots, avec deux variables. Le plus court part du premier mot, pas de `""`.

</details>

---

## Étape 2 — Les mots les plus fréquents, sans collection

<details><summary>Indice 1</summary>

Après `Arrays.sort`, parcours le tableau avec deux indices. `i` marque le début d'une série ; `j` avance tant que `sorted[j]` est égal à `sorted[i]`. La longueur de la série vaut `j - i`, puis on continue à partir de `i = j`.

</details>

<details><summary>Indice 2</summary>

Garde 4 variables : meilleur mot et son compte, second mot et son compte. Une série strictement plus longue que la meilleure fait **descendre** l'ancienne meilleure en second. Le `>` strict garde le premier dans l'ordre alphabétique à égalité.

</details>

---

## Étape 3 — Palindromes, positions, censure

<details><summary>Indice 1</summary>

Palindrome : `for (int i = 0, j = w.length() - 1; i < j; i++, j--)`, comparer `charAt(i)` et `charAt(j)` sur le mot en minuscules. Pour « déjà vu », une chaîne `" radar kayak "` et `contains(" " + mot + " ")` : les espaces évitent qu'un mot soit trouvé à l'intérieur d'un autre.

</details>

<details><summary>Indice 2</summary>

- Les positions : `while ((found = text.indexOf(mot, from)) >= 0)`, puis `from = found + 1`.
- La censure : `ligne.replace(mot, "*".repeat(mot.length()))`. Elle porte sur la 3e ligne (`lines[2]`).

</details>

---

## Étape 4 — Mise en titre et tests

<details><summary>Indice 1</summary>

`mot.substring(0, 1).toUpperCase() + mot.substring(1).toLowerCase()`. Découpe la 1re ligne avec `split(" ")` : le `:` et le `.` restent collés aux mots, c'est voulu.

</details>

<details><summary>Indice 2</summary>

Les compteurs : `startsWith("Le ")` et `endsWith(".")` sur chaque ligne. « égal sans casse » : `"KAYAK".equalsIgnoreCase(words[3])`.

</details>

---

## Étape 5 — Nettoyage et méthodes de Java 11 à 15

<details><summary>Indice 1</summary>

Chaque méthode s'applique à `Data.MESSY` et s'affiche entre `[ ]` pour voir les espaces. `translateEscapes()` s'applique au résultat du `strip()`.

</details>

<details><summary>Indice 2</summary>

`indent` rend déjà un texte terminé par `\n` : affiche-le avec `print`, pas `println`. Pour le `12`, compte les lignes de `"   x\n     y\n"`, en incluant celle **après** le dernier `\n`, puis la plus petite indentation parmi elles.

</details>

---

## Étape 6 — Formatage et chaînage

<details><summary>Indice 1</summary>

`%-6s` : un texte sur 6 caractères, aligné à **gauche** (le `-`). `%4d` : un entier sur 4, aligné à droite. `%s` d'un `boolean` affiche `true`.

</details>

<details><summary>Indice 2</summary>

Pour le chaînage, écris une ligne par appel : `strip` → `"Hello World"`, puis… Compte les positions pour `substring(6)`.

</details>
