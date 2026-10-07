# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La facture

<details><summary>Indice 1</summary>

`basesByRate()` : `lines.forEach(l -> bases.merge(l.vatPerMille(), l.net(), Long::sum))`, dans une `TreeMap`. La TVA se calcule **par taux**, sur la base cumulée.

</details>

<details><summary>Indice 2</summary>

Les espaces insécables des formats (U+00A0 et U+202F) doivent devenir `_` : une petite méthode `visible(String)` avec deux `replace`.

</details>

---

## Étape 2 — Arrondis et motifs

<details><summary>Indice 1</summary>

`NumberFormat.getIntegerInstance(Locale.US)` arrondit sans décimale. `setRoundingMode(RoundingMode.HALF_UP)` change la règle d'arrondi.

</details>

<details><summary>Indice 2</summary>

Dans un motif `DecimalFormat` : `0` est un chiffre **obligatoire**, `#` un chiffre **facultatif**, `,` le groupement, `;` sépare la partie négative, `'…'` est un texte littéral et `%` multiplie par 100.

</details>

---

## Étape 3 — Compact et lecture

<details><summary>Indice 1</summary>

`NumberFormat.getCompactNumberInstance(locale, NumberFormat.Style.SHORT)` ou `.LONG` (Java 12).

</details>

<details><summary>Indice 2</summary>

`parse` lève une `ParseException` (vérifiée) : un `try`/`catch`. `getErrorOffset()` donne la position où la lecture a échoué.

</details>

---

## Étape 4 — Partage et prêt

<details><summary>Indice 1</summary>

Partage : la part de chacun et le reste de chacun en une boucle. Puis une liste des indices triée par reste décroissant (puis par indice), et +1 centime aux premiers, jusqu'à combler le manque.

</details>

<details><summary>Indice 2</summary>

Prêt : `r = taux / 1000.0 / 12`, la mensualité `Math.round(P * r / (1 - Math.pow(1 + r, -n)))`. Le dernier mois paie `reste + intérêts`, ce qui ramène le reste à 0.

</details>
