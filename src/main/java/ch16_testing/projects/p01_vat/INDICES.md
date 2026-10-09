# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le code : la TVA en centimes

<details><summary>Indice 1</summary>

Une classe utilitaire : `final`, un constructeur `private` vide, et seulement des méthodes `static`. Les deux contrôles se font **avant** le calcul.

</details>

<details><summary>Indice 2</summary>

`gross` ne refait pas le calcul : elle appelle `vat`. Ainsi, les contrôles et l'arrondi ne sont écrits qu'une fois.

</details>

---

## Étape 2 — Ton premier test

<details><summary>Indice 1</summary>

Pas de flèche verte dans la marge ? Vérifie trois choses : l'import `org.junit.jupiter.api.Test` (et non `org.junit.Test`, l'ancien JUnit 4), l'annotation `@Test` sur la méthode, et le projet Maven rechargé.

</details>

<details><summary>Indice 2</summary>

Le test qui relie les deux méthodes : calcule `VatCalculator.gross(1999, 20) - 1999` d'un côté, `VatCalculator.vat(1999, 20)` de l'autre, et compare-les avec `assertEquals`. Tu n'as pas besoin de connaître la valeur exacte.

</details>

---

## Étape 3 — Tester les exceptions

<details><summary>Indice 1</summary>

`assertThrows(Type.class, () -> appel)` : l'appel est **dans** la lambda. Si tu appelles `VatCalculator.vat(-1, 20)` en dehors, l'exception part avant que JUnit puisse l'attraper.

</details>

<details><summary>Indice 2</summary>

Range le résultat : `IllegalArgumentException e = assertThrows(…);` puis `assertEquals("montant negatif : -1", e.getMessage());`.

</details>

---

## Étape 4 — Les valeurs limites

<details><summary>Indice 1</summary>

Pour une règle « de 0 à 100 compris », les valeurs utiles sont celles qui sont **juste sur** la limite et **juste à côté**. Les valeurs dehors sont déjà testées à l'étape 3 ; il manque celles de dedans.

</details>

<details><summary>Indice 2</summary>

Un seul test peut contenir plusieurs `assertEquals` quand ils vérifient la même idée : `gross(1000, 0)` puis `gross(1000, 100)`. Fais les calculs sur papier d'abord.

</details>

---

## Étape 5 — Les catégories et l'affichage

<details><summary>Indice 1</summary>

Un `switch` en expression (chapitre 3) : `return switch (cle) { case "standard" -> 20; … default -> throw new IllegalArgumentException(…); };`. Le message utilise `category`, pas la clé nettoyée.

</details>

<details><summary>Indice 2</summary>

Pour `format` : `String signe = cents < 0 ? "-" : "";` puis `long abs = Math.abs(cents);`. Les euros sont `abs / 100`, les centimes `abs % 100`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque mutant survivant, demande-toi : « quelle entrée donnerait un résultat **différent** avec ce bug ? ». Puis écris le test qui utilise cette entrée.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. L'arrondi tronque au lieu d'arrondir au plus proche (le `+ 50` disparaît).
2. Le taux 100 est refusé (`>= 100` au lieu de `> 100`).
3. Le montant 0 est refusé (`<= 0` au lieu de `< 0`).
4. Un taux négatif est accepté (le contrôle `ratePercent < 0` disparaît).
5. Les centimes ne sont plus sur deux chiffres (`%d` au lieu de `%02d`).
6. Le signe moins disparaît de l'affichage.
7. La catégorie n'est plus passée en minuscules.
8. Le taux « reduit » vaut 10 au lieu de 5.
9. Le message du montant négatif ne contient plus la valeur.

</details>
