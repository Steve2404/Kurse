# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — L'interface `Step`

<details><summary>Indice 1</summary>

`Step` hérite d'`apply(String)` par `UnaryOperator<String>` : elle n'a **aucune** méthode abstraite à déclarer. `then` : `return s -> next.apply(apply(s));`. Le `apply(s)` de droite est celui de `this`.

</details>

<details><summary>Indice 2</summary>

- `of` : `String[] p = command.strip().split(" ");`, puis `return switch (p[0]) { case "trim" -> s -> s.strip(); … };`. La lambda de `replace` capture `p`, qui n'est jamais réaffecté.
- `parse` : `Step result = identity();`, puis `result = result.then(of(command));` pour chaque morceau.

</details>

---

## Étape 2 — Appliquer les pipelines

<details><summary>Indice 1</summary>

`Step step = Step.parse(pipeline);` **une fois** par pipeline, puis `step.apply(text)` pour chaque texte.

</details>

<details><summary>Indice 2</summary>

Pour la question : que fait `caesar 23` à une lettre que `caesar 3` a déjà décalée ? Et que fait `decode` sur ce que `encode` produit ?

</details>

---

## Étape 3 — Composer des `Function`

<details><summary>Indice 1</summary>

`f.andThen(g)` : d'abord f, puis g. `f.compose(g)` : d'abord **g**, puis f.

</details>

<details><summary>Indice 2</summary>

`Function.<String>identity()` : le type entre `<>` est donné **avant** le nom de la méthode. `BiFunction` n'a que `andThen` (pas de `compose`) : le résultat d'une fonction ne peut pas devenir **deux** arguments.

</details>

---

## Étape 4 — État et mémoïsation

<details><summary>Indice 1</summary>

`counted` utilisée deux fois dans la chaîne, donc 2 appels. Le tableau `applied` est **une** référence, jamais réaffectée : seule sa case change.

</details>

<details><summary>Indice 2</summary>

`wrap(f)` : `return s -> { calls++; cherche s dans keys…; sinon result = f.apply(s), mémorise si place, return result; };`. `calls` et `hits` sont des champs du `Memo`, et la lambda y accède par `this`.

</details>
