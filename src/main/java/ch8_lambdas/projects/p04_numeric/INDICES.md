# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les algorithmes de `Numeric`

<details><summary>Indice 1</summary>

Les interfaces primitives ont des méthodes aux noms précis : `DoubleUnaryOperator.applyAsDouble(double)`, `IntBinaryOperator.applyAsInt(int, int)`, `IntPredicate.test(int)`, `IntUnaryOperator.applyAsInt(int)`.

</details>

<details><summary>Indice 2</summary>

- `derivative(f)` rend une lambda qui **capture** `f` et `h` : `x -> (f.applyAsDouble(x + h) - f.applyAsDouble(x - h)) / (2 * h)`.
- `reduce` : `acc = identity`, puis pour chaque v, `acc = op.applyAsInt(acc, v)`.

</details>

---

## Étape 2 — Racines, intégrales, maximum

<details><summary>Indice 1</summary>

Une fonction mathématique s'écrit directement en lambda : `x -> x * x - 2`. `Math::sin` est une référence de méthode qui convient à `DoubleUnaryOperator` (`double` vers `double`).

</details>

<details><summary>Indice 2</summary>

Toutes les valeurs affichées passent par `Numeric.round`, et les `Result` par leur `toString`. Pour √π, `Math.sqrt(Math.PI)` arrondi.

</details>

---

## Étape 3 — Composer, réduire, filtrer

<details><summary>Indice 1</summary>

`Integer::sum` et `Math::max` conviennent à `IntBinaryOperator` : deux `int`, un `int`. `Numeric::gcd` aussi.

</details>

<details><summary>Indice 2</summary>

`prime` est une lambda **en bloc** : accolades, une boucle `for (d = 2; d * d <= x; …)` et des `return`. `even.and(big)`, `prime.or(even.negate())` : les mêmes combinateurs que `Predicate`.

</details>

---

## Étape 4 — Les autres formes, et Monte-Carlo

<details><summary>Indice 1</summary>

`ToIntFunction<T>` (un objet vers un `int`), `IntFunction<R>` (un `int` vers un objet), `IntToDoubleFunction`, `IntToLongFunction`, `ObjIntConsumer<T>` (un objet et un `int`, rien en retour).

</details>

<details><summary>Indice 2</summary>

- `rnd` modifie un **champ** `static long seed` : c'est permis dans une lambda, contrairement à une variable locale.
- `inside` est un `int[1]`, modifié dans la boucle et lu par `counter`.

</details>
