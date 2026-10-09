# Projet 9 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Lire, et écrire les règles

<details><summary>Indice 1</summary>

Déroule `updateDay` à la main sur le stock de `Data`, pour le jour 1, et compare avec la sortie. Pour le billet : après `sellIn = sellIn - 1`, `sellIn < 10` veut dire « il restait 10 jours ou moins avant la mise à jour ».

</details>

<details><summary>Indice 2</summary>

Pour le `Comte` à 55 : la branche n'ajoute que « si `quality < 50` ». Pour la `Tomme` à 60 : la branche des autres produits retire sans regarder 50.

</details>

---

## Étape 2 — Le filet

<details><summary>Indice 1</summary>

Pour comparer, transforme chaque côté en `List<String>` : `legacy.stream().map(Data.LegacyItem::toString).toList()` et, pour tes articles, `c.name() + " " + c.sellIn() + " " + c.quality()` (le même format que `LegacyItem.toString`).

</details>

<details><summary>Indice 2</summary>

Le legacy **modifie** ses articles : construis-lui ses propres `LegacyItem` (une copie de ton stock), dans une `ArrayList`, avant la boucle des jours.

</details>

---

## Étape 3 — L'article et les règles

<details><summary>Indice 1</summary>

`raise` : `Math.max(quality, Math.min(MAX_QUALITY, quality + gain))`. Le `Math.min` plafonne à 50 ; le `Math.max` garde la qualité d'origine si elle était déjà plus haute.

</details>

<details><summary>Indice 2</summary>

`DecayRule.age` : `int sellIn = cheese.sellIn() - 1;`, puis la perte (doublée si `sellIn < 0`), puis `cheese.next(Math.max(0, cheese.quality() - loss))`. Le billet : si `sellIn < 0`, `cheese.next(0)` ; sinon un gain de `1 + (sellIn < 10 ? 1 : 0) + (sellIn < 5 ? 1 : 0)`.

</details>

---

## Étape 4 — La fabrique et la cave

<details><summary>Indice 1</summary>

`Map.of("Brie", new DecayRule(2), "Comte", new AgedRule(), …)`, puis `SPECIAL.getOrDefault(name, STANDARD)` avec `STANDARD = new DecayRule(1)`. Le sel : `static final AgingRule SALT = cheese -> cheese;`.

</details>

<details><summary>Indice 2</summary>

`nextDay` : `stock.stream().map(cheese -> RuleBook.ruleFor(cheese.name()).age(cheese)).toList()`.

</details>

---

## Étape 5 — Les produits bio

<details><summary>Indice 1</summary>

Dans `BioRule.age` : `Cheese aged = inner.age(cheese); int lost = cheese.quality() - aged.quality();`. Si `lost > 0`, la qualité devient `Math.max(0, cheese.quality() - 2 * lost)` ; sinon, on rend `aged`.

</details>

<details><summary>Indice 2</summary>

Dans `ruleFor` : `if (name.startsWith("Bio ")) return new BioRule(ruleFor(name.substring(4)));`. L'appel est **récursif** : `"Bio Brie"` cherche la règle de `"Brie"`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Ce que le maître étalon ne voit pas : un article au-dessus de 50, `afterDays`, la validation de `Cheese`, et tout ce qui est bio.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. La perte double déjà le dernier jour avant la date (`sellIn` 0).
2. Un produit qui s'abîme ne descend plus sous 1.
3. La qualité peut monter jusqu'à 51.
4. Un article au-dessus de 50 redescend à 50 au lieu de garder sa qualité.
5. Le comté ne double son gain qu'un jour trop tard.
6. Le billet gagne +2 dès 10 jours restants (au lieu de moins de 10).
7. Le billet gagne +3 dès 5 jours restants (au lieu de moins de 5).
8. Le billet garde sa valeur un jour de trop après l'événement.
9. Le brie ne perd plus que 1 par jour.
10. Le sel perd un jour à chaque nuit.
11. Le préfixe `"Bio "` est mal coupé : `"Bio Brie"` cherche la règle de `" Brie"`.
12. Le bio perd 1 de plus au lieu de perdre le double.
13. Le bio double aussi les **gains**.
14. `afterDays` fait vieillir un jour de trop.
15. Une qualité de −1 est acceptée.

</details>
