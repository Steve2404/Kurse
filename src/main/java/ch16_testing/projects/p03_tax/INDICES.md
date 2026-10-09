# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le barème par tranches

<details><summary>Indice 1</summary>

Deux tableaux en constantes : les limites (`10_000, 25_000, 60_000`) et les taux (`0, 10, 25, 40`). Il y a **un taux de plus** que de limites : la dernière tranche n'a pas de plafond.

</details>

<details><summary>Indice 2</summary>

Dans la boucle, garde le **bas** de la tranche courante dans une variable. Le haut de la dernière tranche peut être `Long.MAX_VALUE`. Ajoute `(Math.min(income, haut) - bas) * taux` seulement si `income > bas`, puis le haut devient le bas suivant.

</details>

---

## Étape 2 — Un tableau de cas

<details><summary>Indice 1</summary>

`@ParameterizedTest` et `@CsvSource` ne sont pas dans le même paquet que `@Test` : `org.junit.jupiter.params` et `org.junit.jupiter.params.provider`. La méthode reçoit un paramètre par colonne, dans l'ordre.

</details>

<details><summary>Indice 2</summary>

Dans un `textBlock`, chaque ligne est un cas : `10000,   0`. Les espaces autour des valeurs sont ignorés. Ne mets ni guillemets ni virgule en fin de ligne.

</details>

---

## Étape 3 — Une colonne, et une propriété

<details><summary>Indice 1</summary>

Le message d'un revenu négatif contient la valeur : construis la chaîne attendue avec le paramètre, `"revenu negatif : " + income`.

</details>

<details><summary>Indice 2</summary>

La propriété : `long supplement = TaxCalculator.tax(income + 1) - TaxCalculator.tax(income);` puis `assertTrue(supplement >= 0 && supplement <= 1, …)`.

</details>

---

## Étape 4 — Des cas complexes

<details><summary>Indice 1</summary>

Les demi-parts : `2 * adults + Math.min(children, 2) + 2 * Math.max(0, children - 2)`. Vérifie à la main : 2 adultes et 3 enfants font 8 demi-parts, soit 4 parts.

</details>

<details><summary>Indice 2</summary>

Pour calculer un cas à la main : `income * 2 / demiParts` (division entière), puis `tax` de ce revenu (utilise le barème de l'étape 1, avec l'arrondi), puis `× demiParts / 2`.

</details>

---

## Étape 5 — Les mots de passe

<details><summary>Indice 1</summary>

Commence par le cas `null` ou vide, avec un `return` immédiat : sinon `password.length()` lancerait une `NullPointerException` sur `null`. Ensuite, une liste qu'on remplit règle par règle, dans l'ordre.

</details>

<details><summary>Indice 2</summary>

Pour un cas qui casse **une seule** règle, pars d'un mot de passe valide (`Abcdefghij1!`) et change **une** chose : enlève le chiffre, mets tout en minuscules, retire un caractère…

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quelle limite, quelle règle, quel message n'est vérifié par aucun de tes cas ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. L'impôt est tronqué au lieu d'être arrondi à l'euro le plus proche.
2. Au-delà de 60 000, le taux marginal rendu est 25 % au lieu de 40 %.
3. La 3e limite est à 50 000 au lieu de 60 000.
4. Le taux de la dernière tranche est 45 % au lieu de 40 %.
5. Une limite appartient à la tranche du dessus dans `marginalRate` (`<` au lieu de `<=`).
6. À partir du 3e enfant, chaque enfant ne compte qu'une demi-part.
7. Le 3e enfant compte une demi-part au lieu d'une part.
8. Zéro adulte est accepté.
9. Le revenu −1 est accepté.
10. Tout le revenu est taxé au taux de sa tranche (pas de barème marginal).
11. 11 caractères suffisent.
12. 64 caractères sont refusés.
13. `null` n'est plus traité à part (une `NullPointerException` part).
14. Un chiffre compte comme un symbole.
15. Les espaces ne sont plus interdits.
16. Un mot de passe sans minuscule mais avec des majuscules passe la règle « sans minuscule ».
17. Un mot de passe vide donne « trop court » au lieu de « vide ».

</details>
