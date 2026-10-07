# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — L'API et le localisateur

<details><summary>Indice 1</summary>

`ServiceLoader.load(PricingRule.class)` ne fonctionne que dans un module qui déclare `uses pricing.api.PricingRule;`. `.stream()` rend des `ServiceLoader.Provider<PricingRule>` : `get()` crée l'objet, `type()` donne sa classe **sans** le créer.

</details>

<details><summary>Indice 2</summary>

Le masque : pour n règles, `mask` va de 1 à `(1 << n) - 1`. La règle i est dans la combinaison si `(mask & 1 << i) != 0` (chapitre 2, projet 2). Avec 3 règles, il y a 7 combinaisons non vides.

</details>

---

## Étape 2 — Les fournisseurs et le consommateur

<details><summary>Indice 1</summary>

Un fournisseur n'exporte **rien** : personne n'a besoin de connaître ses classes. Il les annonce seulement avec `provides pricing.api.PricingRule with …;`.

</details>

<details><summary>Indice 2</summary>

Une classe fournisseur doit être `public`, avec un constructeur `public` sans argument… **ou** une méthode `public static` nommée `provider()`, qui rend l'objet. Pour la question sur `type()`, regarde le **type rendu** par `provider()`.

</details>

---

## Étape 3 — Le script `build.sh`

<details><summary>Indice 1</summary>

`shop.app` ne requiert **aucun** fournisseur : `javac` ne les trouverait pas tout seul. Il faut les nommer dans `-m`.

</details>

<details><summary>Indice 2</summary>

`--limit-modules shop.app,pricing.basic` garde ces modules **et** ceux qu'ils requièrent. `pricing.premium`, requis par personne, devient invisible.

</details>
