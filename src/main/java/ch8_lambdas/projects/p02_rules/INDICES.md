# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Compiler les règles

<details><summary>Indice 1</summary>

`or()` : `Predicate<String> p = and();`, puis `while (pos < tokens.length && tokens[pos].equals("|")) { pos++; p = p.or(and()); }`. `and()` a la même forme avec `&` et `not()`.

</details>

<details><summary>Indice 2</summary>

- `atom` : pour `len>=8`, `int n = Integer.parseInt(t.substring(5)); return s -> s.length() >= n;`. Pour les autres, sépare le nom et l'argument au `:`, puis un `switch` expression qui rend une lambda.
- Le tableau du `main` est un `Rule[]`, rempli avec `compile(…)::test`.

</details>

---

## Étape 2 — `BiPredicate` et utilitaires

<details><summary>Indice 1</summary>

`containsUser = (pwd, user) -> pwd.toLowerCase().contains(user.toLowerCase())`. `notUser` « fixe » le 2e argument : elle capture `user`.

</details>

<details><summary>Indice 2</summary>

`Predicate.not(p)` et `Predicate.isEqual(x)` sont des méthodes **static** de l'interface. `negate()`, `and()` et `or()` sont des méthodes **default**, appelées sur un prédicat existant.

</details>

---

## Étape 3 — Les correctifs

<details><summary>Indice 1</summary>

`Fix[] fixes = { new Fix("sans espace", s -> s.contains(" "), s -> s.replace(" ", "")), … };`. Chaque correctif contient **deux** lambdas : un `Predicate` et un `UnaryOperator`.

</details>

<details><summary>Indice 2</summary>

Une boucle `for (round…; round < 5 && !strong.test(fixed); …)`. À l'intérieur, une boucle sur `fixes` : le premier dont `problem().test(fixed)` est vrai s'applique (`fixed = f.repair().apply(fixed)`), puis `break`.

</details>
